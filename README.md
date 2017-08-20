# SE306
Build status ![](https://circleci.com/gh/qhua948/SE306.png?circle-token=3c376333dfa42e6f783718d8741ade5adb351b21)

![](assets/3.png)
# Usage

usage: Scheduler [-h] [-g] [-a {as,bnb}] -p N [-r M] INFILENAME
                 [OUTFILENAME]

A GPU Scheduling program

positional arguments:
  INFILENAME             Filename to process
  OUTFILENAME            Output file name, write to STDOUT if non-specified

optional arguments:
  -h, --help             show this help message and exit
  -g, --gui              Choose whether to use  GUI(Not  implemented at the
                         moment) (default: false)
  -a {as,bnb}, --algorithm {as,bnb}
                         Choose the algorithm to use (default: as)
  -p N, --processors N   Processor count
  -r M, --parallel M     Use parallel processing (default: [1])
