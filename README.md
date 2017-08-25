# SE306
Build status ![](https://circleci.com/gh/qhua948/SE306.png?circle-token=3c376333dfa42e6f783718d8741ade5adb351b21)

<p align="center"> <img src = assets/3.png/></p>
# Usage

```

usage: Scheduler [-h] [-v] [-p M] [-o [OUTFILENAME]] INFILENAME P

A GPU Scheduling program

positional arguments:
  INFILENAME             Filename to process
  P                      Processor count

optional arguments:
  -h, --help             show this help message and exit
  -v                     Choose whether to use GUI (default: false)
  -p M, --parallel M     Use parallel processing (default: [1])
  -o [OUTFILENAME]       Output file name, write to STDOUT if non-specified
usage: Scheduler [-h] [-g] [-a {as,bnb}] -p N [-r M] INFILENAME
                 [OUTFILENAME]

```
