# SimPLE

 SimPLE (Simulation Process with Less Encode) is a aplication used to 
 intercomunicate other aplications using a simple hashtable(key,value) 
 to map the information globaly. 
 Each child aplication can be programmed on any programming language, 
 the SimPLE only comunicate by string using the standard input/output.
 <br><br>
 To write any information on global memory the childs needs print the 
 following informations on standard output:exit
 
 <pre>
 |--------[general example in C]--------------|
 | 1)  printf("write.%s\n", key);             |
 | 2)  printf("%s\n", value);                 |
 | 3)  flush(stdout);                         |
 |--------[exemple of termometer]-------------|
 | 1)  printf("write.myApp.temperature\n");   |
 | 2)  printf("%f\n", temperature);           |
 | 3)  flush(stdout);                         |
 |--------------------------------------------|
 </pre>
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
 
 >> java -jar SimPLE.jar -file sim-example.txt
 
 There hare too several optional parameters, each argument is composeds 
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
 
