 To read any information from global memory the childs needs print and 
 read the following informations on standard output/input:
 <pre>
 |--------[general example in C]--------------|
 | 1)  printf("read.%s\n", key);              |
 | 2)  flush(stdout);                         |
 | 3)  scanf("%s", &value);                   |
 |--------[to use the termometer]-------------|
 | 1)  printf("read.myApp.temperature\n");    |
 | 2)  flush(stdout);                         |
 | 3)  scanf("%f\n", &temperature);           |
 |--------------------------------------------|
 </pre>

 To run the SimPLE aplication use the following command line:
 <pre>
 \>\> java -jar SimPLE.jar -file sim-example.txt
 </pre>
 
 There are too several optional parameters, each argument is composeds 
 by pair [-name] [value], the full list is discribed bellow:
 
 <pre>
  -fast                default = false, this true disable all delays to a fast initialization
  -disableWarnnings    default = false, this true disable all warnning menssages
  -delayFirstDebug     default = 2000 ms, wait a little to perform a best verification if all modules is fine
  -delayStartListen    default = 1000 ms, wait a little to listem the modules (\*)
  -delayActivity       default = 2000 ms, wait a little to start the activity verification on modules (\*)
  -delayUserInterface  default = 3000 ms, wait a little to start the user interface (\*)
  -sleepWaitRequest    default = 100  ms, time to try again on solve a fail request (please use >= 10 ms)
  -sleepActivity       default = 1000 ms, time between activity verification on modules (please use >= 10 ms)
  -streamOutputSize    default = 8000 chars, number of characters on buffer of standar output stream 
  -streamErrorSize     default = 8000 chars, number of characters on buffer of standar error stream 
  (\*) (usefull only to best prints visualization)
 </pre>
 
### First Setup
 First donwload the last release version of SimPLE, [Latest release](https://github.com/marcio-da-silva-arantes/SimPLE/releases/latest). Now extract it to a most possible simple path, we sujest as sample for Windows on C:/SimPLE/. On this directory there are severals simulations test files with names like simXX-XXX.txt we sujest to use the [Notepad++](https://notepad-plus-plus.org/) to open and edit this files for Windows or [Geany](https://www.geany.org/) for Linux. To perform the first setup you need to change the paths from E:/Dropbox/GitHub/SimPLE/ to C:/SimPLE/ as example on this files before use them. Note that this paths point to the exacly codes that will run, some times this is sufficient condition to run the SimPLE.jar aplication as explained above. But to avoid some portability problems we recomend to compile again each of this codes to your computer, to do that go to each module poited by the simXX-XXX.txt file and compile again the code.
 
### Getting Started
 TODO...