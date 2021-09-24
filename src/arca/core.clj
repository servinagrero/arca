(ns arca.core
  (:import (java.io BufferedWriter FileWriter))
  (:require [net.cgrand.enlive-html :as html]
            [clojure.java.io :as io]
            [org.httpkit.client :as http]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as string]
            [clojure.pprint :as pp])
  (:gen-class))

(def cli-options
  [["-s" "--size SIZE" "Maximum number of results"
    :default 50
    :parse-fn #(Integer/parseInt %)
    :validate [#(#{25 50 100 200} %) "Must be one of 25, 50, 100, or 200"]]
   ["-r" "--root ROOT" "Base path to download the files"
    :default "."]
   ["-h" "--help"]])

(defn usage [options-usage]
  (->> ["Usage: arca [options] word [,word]"
        "Download information about the latest publications on arxiv.org based on a query."
        ""
        "Options:"
        options-usage
        ""]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn validate-args
  "Validate command line arguments. Either return a map indicating the program
  should exit (with a error message, and optional ok status), or a map
  indicating the action the program should take and the options provided."
  [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options)
      {:exit-message (usage summary) :ok? true}

      errors
      {:exit-message (error-msg errors)}

      (>= (count arguments) 1)
      {:words arguments :options options}

      :else
      {:exit-message (usage summary)})))

(defrecord Paper [title date link])

(defn get-dom
  "Extract and parse the dom from arxiv given a keword to search."
  [word size]
  (let [base-url "https://arxiv.org/search/?searchtype=all&query=%s&size=%s&order=-announced_date_first"
        word-urlencoded (string/replace word #" " "%20")
        query (format base-url word-urlencoded size)]
    (html/html-snippet (:body @(http/get query {:insecure? false})))))

(defn get-raw-papers
  "Filter the dom to extract just the papers."
  [dom]
  (html/select dom [:li.arxiv-result]))

(defn extract-title
  "Extract the title name from a given paper."
  [paper]
  (let [title (first (html/select paper [:p.title]))]
    (->> title :content first string/trim)))

(defn extract-date
  "Extract the submission date from a paper."
  [paper]
  (let [date (first (html/select paper [:p.is-size-7]))]
    (->> date :content second string/trim drop-last (apply str))))

(defn extract-link
  "Extract the link to the paper pdf file"
  [paper]
  (let [links (html/select paper [:p.list-title :span :a])]
    (first (html/attr-values (first links) :href))))

(defn write-papers
  "Write a list of papers to a file.

  The file is stored as <root>/dd-MM-yyyy/<keword>.md
  where <root> is the base path specified in the options
  and <keyword> is the word to search."
  [papers root word]
  (let [today (.format (java.text.SimpleDateFormat. "dd-MM-yyyy") (java.util.Date.))
        dir (io/file root today)
        file-name (io/file dir (str word ".md"))
        paper-fmt "# %s\n - %s\n - %s\n\n"]

    (when-not (.exists dir)
      (println (format "Creating %s" dir))
      (.mkdir dir))

    ; Write the papers to the file
    (println (format "Writing keyword: %s" word))
    (with-open [wtr (BufferedWriter. (FileWriter. file-name))]
      (doseq [p papers]
        (.write wtr (format paper-fmt (:title p) (:date p) (:link p)))))))

(defn get-papers
  "Obtain a list of papers based on the search provided and write the information to a file."
  [options word]
  (let [dom (get-dom word (:size options))
        data (get-raw-papers dom)
        papers (map #(Paper. (extract-title %) (extract-date %) (extract-link %)) data)]
    (write-papers
      (filter #(seq (:title %)) papers)
      (:root options)
      word)))

(defn -main [& args]
  (let [{:keys [words options exit-message ok?]} (validate-args args)]
    (when exit-message (exit (if ok? 0 1) exit-message))
    (mapcat (partial get-papers options) words)))
