<h1 align='center'>ARCA</h1>

<h3 align='center'>Download information about the latest publications on arxiv.org</h3>

## Installation

Clone the repository and install with lein or grab the standalone release.

## Usage

To download the information just add the keword to search. Multiple keywords can be added at the same time with spaces.

The results will be written to `<root>/dd-MM-YYYY/<word>.md`:
  - `<root>` is the base path specified with `-r`.
  - `dd-MM-YYYY` is the current date in that format.
  - `<word>` is the keyword provided.

The information will be written to a markdown file with the format:

```
# Title of the document
  - <date of submission>
  - <link to the pdf>
```

```shell
$ java -jar arca-0.1.0-standalone.jar [options] word [,word]
$ java -jar arca-0.1.0-standalone.jar -h
Usage: arca [options] word [,word]
Download information about the latest publications on arxiv.org based on a query.

Options:
  -s, --size SIZE  50  Maximum number of results
  -r, --root ROOT  .   Base path to download the files
  -h, --help
```

For more information about how to use the tool run:
```shell
$ java -jar arca-0.1.0-standalone.jar -h
```

## License

MIT License

Copyright © 2021 Sergio Vinagrero

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
