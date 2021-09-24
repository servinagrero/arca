(defproject arca "0.1.0"
  :description "Tool to obtain the latest publications from arxiv.org based on a keyword."
  :url "http://github.com/servinagrero/arca"
  :license {:name "MIT LICENSE"
            :url "https://choosealicense.com/licenses/mit"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "1.0.206"]
                 [enlive "1.1.6"]
                 [http-kit "2.3.0"]]
  :main ^:skip-aot arca.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
