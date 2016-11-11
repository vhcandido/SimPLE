# SimPLE
SimPLE (Simulation Process with Less Encode)

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
