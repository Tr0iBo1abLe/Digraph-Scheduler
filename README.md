# SE306
Build status ![](https://circleci.com/gh/qhua948/SE306.png?circle-token=3c376333dfa42e6f783718d8741ade5adb351b21)
# Usage
usage: HDNW [-h] [-l {gs,old}] [-a {as,bnb}] -p N [-r M] FILENAME

A GPU Scheduling program

positional arguments:
  FILENAME               Filename to process

optional arguments:
  -h, --help             show this help message and exit
  -l {gs,old}, --library {gs,old}
                         Choose library  to  use.  gs  ->  Use  graphstream
                         library, old -> use  old  parser and datastructure
                         (default: gs)
  -a {as,bnb}, --algorithm {as,bnb}
                         Choose the algorithm to use (default: as)
  -p N, --processors N   Processor count
  -r M, --parallel M     Use parallel processing (default: 1)
