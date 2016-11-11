/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.PatternSyntaxException;


/**
 * SimPLE (Simulation Process with Less Encode) is a aplication used to 
 * intercomunicate other aplications using a simple hashtable(key,value) 
 * to map the information globaly. 
 * Each child aplication can be programmed on any programming language, 
 * the SimPLE only comunicate by string using the standard input/output.
 * <br><br>
 * To write any information on global memory the childs needs print the 
 * following informations on standard output:exit
 * 
 * <pre>
 * |--------[general example in C]--------------|
 * | 1)  printf("write.%s\n", key);             |
 * | 2)  printf("%s\n", value);                 |
 * | 3)  flush(stdout);                         |
 * |--------[exemple of termometer]-------------|
 * | 1)  printf("write.myApp.temperature\n");   |
 * | 2)  printf("%f\n", temperature);           |
 * | 3)  flush(stdout);                         |
 * |--------------------------------------------|
 * </pre>
 * To read any information from global memory the childs needs print and 
 * read the following informations on standard output/input:
 * <pre>
 * |--------[general example in C]--------------|
 * | 1)  printf("read.%s\n", key);              |
 * | 2)  flush(stdout);                         |
 * | 3)  scanf("%s", &value);                   |
 * |--------[to use the termometer]-------------|
 * | 1)  printf("read.myApp.temperature\n");    |
 * | 2)  flush(stdout);                         |
 * | 3)  scanf("%f\n", &temperature);           |
 * |--------------------------------------------|
 * </pre>
 * 
 * 
 * @author marcio
 */
public class SimPLE {
    public static final GlobalValues [] global = new GlobalValues[]{
        new GlobalValues("global.currentTimeMillis") {
            @Override
            public String toString() {
                return ""+System.currentTimeMillis();
            }
        },
        new GlobalValues("global.date") {
            @Override
            public String toString() {
                return ""+new Date().toString();
            }
        },
    };
    public static final Comparator compartor = new Comparator<BlockedEntry>() {
        @Override
        public int compare(BlockedEntry o1, BlockedEntry o2) {
            return o1.getKey().compareTo(o2.getKey());
        }
    };
    
    public static final BlockedHash<Object> hash = new BlockedHash<>();
    public static final LinkedList<ChildProcess> process = new LinkedList<>();
    
    private static String[] standardArgs(String[] args){
        //Defining standard input
        if(args==null || args.length == 0){
            //args = new String[]{"-dir", "./modules/"};
            args = new String[]{"-file", "./modules5.txt", "-fast", "true"};
        }
        return args;
    }
    /**
     * usage:   java -jar SimPLE.jar -dir ./modules/ -o
     *          java -jar SimPLE.jar -file modules.txt  -o
     * optional(-o):
     * -fast                default = false, this true disable all delays to a fast initialization
     * -disableWarnnings    default = false, this true disable all warnning menssages
     * -delayFirstDebug     default = 2000 ms, wait a little to perform a best verification if all modules is fine
     * -delayStartListen    default = 1000 ms, wait a little to listem the modules (*)
     * -delayActivity       default = 2000 ms, wait a little to start the activity verification on modules (*)
     * -delayUserInterface  default = 3000 ms, wait a little to start the user interface (*)
     * -sleepWaitRequest    default = 100  ms, time to try again on solve a fail request (please use >= 10 ms)
     * -sleepActivity       default = 1000 ms, time between activity verification on modules (please use >= 10 ms)
     * -streamOutputSize    default = 8000 chars, number of characters on buffer of standar output stream 
     * -streamErrorSize     default = 8000 chars, number of characters on buffer of standar error stream 
     * (*) (usefull only to best prints visualization)
     * @param args          several pairs (-param, value)
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        args = standardArgs(args);
        validating(args);
        
    
        
        //----------------[ Reading the parameters ]------------------
        final boolean fast = boolParam(args, "-fast", false);
        final boolean disableWarnnings = boolParam(args, "-disableWarnnings", false);
        final int delayFirstDebug = intParam(args, "-delayFirstDebug", 2000);
        final int delayStartListen = intParam(args, "-delayStartListen", 1000);
        final int delayActivity = intParam(args, "-delayActivity", 2000);
        final int delayUserInterface = intParam(args, "-delayUserInterface", 3000);
        final int sleepWaitRequest = intParam(args, "-sleepWaitRequest", 100);
        final int sleepActivity = intParam(args, "-sleepActivity", 1000);
        final int streamOutputSize = intParam(args, "-streamOutputSize", 8000);
        final int streamErrorSize  = intParam(args, "-streamErrorSize", 8000);
        
        //----------------[ adding global keys ]------------------
        for(GlobalValues g : global){
            hash.put(g.key, g);
        }
        //----------------[ Reading the child process to start ]------------------
        if(args[0].equals("-dir")){
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(".exe") || name.contains(".jar");
                }
            };
            File dir = new File(args[1]);
            File files[] = dir.listFiles(filter);
            if(files.length==0){
                throw new Exception("Can't find any file in the directory: "+dir.getCanonicalPath()+"\n that matche with filter: *.exe | *.jar");
            }
            for(File f : files){ 
                
                String cmd_temp = "";
                if(f.getName().contains(".exe")){
                    cmd_temp = f.getCanonicalPath();
                }else if(f.getName().contains(".jar")){
                    cmd_temp = "java -jar "+f.getName();
                }else{
                    throw new Exception("executable module '"+f.getName()+"' extension not valid");
                }
                
                ChildProcess child = new ChildProcess(f.getName(), cmd_temp, dir);
                System.out.println("Found module{");
                System.out.println("\t name = '"+child.name+"'");
                System.out.println("\t cmd  = '"+child.cmd+"'");
                System.out.println("\t dir  = '"+child.dir+"'");
                System.out.println("}");
                
                process.addLast(child);
            }
        }else if(args[0].equals("-file")){
            Scanner sc = new Scanner(new File(args[1]));
            ChildProcess child = ChildProcess.read(sc);
            while(child!=null){
                if(!child.dir.exists()){
                    throw new Exception("Can't find the directory: "+child.dir.getCanonicalPath());
                }
                System.out.println("Found module{");
                System.out.println("\t name = '"+child.name+"'");
                System.out.println("\t cmd  = '"+child.cmd+"'");
                System.out.println("\t dir  = '"+child.dir+"'");
                System.out.println("}");
                
                process.addLast(child);
                child = ChildProcess.read(sc);
            }
            sc.close();
        }else{
            throw new Exception("Frist arg not valid, arg found: ["+args[0]+"]\n usage: -dir or -file");
        }
        
        try{
            //-----------------[ Starting the modules ]-----------------------
            System.out.println("Starting the modules:");
            for(ChildProcess c : process){
                System.out.println("\t"+c.name);
                c.start();
            }

            //-----------------[ First Debug on modules ]-----------------------
            if(!fast && delayFirstDebug>0){
                //System.out.println("Performing delayFirstDebug = "+delayFirstDebug+" parameter");
                Thread.sleep(delayFirstDebug);
            }

            System.out.println("First Debug on modules:");
            for(ChildProcess c : process){
                if(c.getProcess().isAlive()){
                    System.out.println("\t"+c.name+" is alive");
                }else{
                    System.out.println("\t"+c.name+" is not alive, exitValue = "+c.getProcess().exitValue());
                }
            }

            //-----------------[ Start to listen the modules ]----------------------

            System.out.println("Start to listen the modules:");
            for(ChildProcess c : process){
                Executors.newSingleThreadExecutor().execute(new RunnableException() {
                    @Override
                    public void exit_error(Throwable ex) {
                        hash.put(c.getThreadIO()+".state", "error");
                        System.err.printf("exit %s by following error:\n", c.getThreadIO());
                        ex.printStackTrace(System.err);
                        c.clearIO_counts();
                    }
                    @Override
                    public void running() throws Throwable{
                        if(!fast && delayStartListen>0){
                            //System.out.println("\t"+c.getThreadIO()+": performing delayStartListen = "+delayStartListen+" parameter");
                            Thread.sleep(delayStartListen);
                        }

                        System.out.println("\t"+c.getThreadIO()+": start to listen");
                        hash.put(c.getThreadIO()+".state", "started");

                        Scanner in = new Scanner(c.getProcess().getInputStream());
                        PrintStream out = new PrintStream(c.getProcess().getOutputStream());

                        c.clearIO_counts();
                        while(in.hasNextLine()){
                            String line = in.nextLine();
                            c.addIO_counts(1);
                            //System.out.println(line);

                            if(line.startsWith("read.")){
                                Object value = hash.get(line.substring(5));
                                while(value==null){
                                    if(!disableWarnnings){
                                        System.err.println("warrning: node "+c.name+" is waitinng for "+line.substring(5));
                                    }
                                    if(sleepWaitRequest>0){
                                        sleep(sleepWaitRequest);
                                    }

                                    value = hash.get(line.substring(5));

                                    //value = "0";    //use default value "0"
                                }
                                //System.out.println(value);
                                out.println(value);
                                out.flush();
                            }else if(line.startsWith("write.")){
                                String value = in.nextLine();
                                if(line.substring(6).startsWith("global.")){
                                    if(!disableWarnnings){
                                        System.err.println("warrning: node "+c.name+" try to write a global reseverd value using key = "+line.substring(6));
                                    }
                                }else{
                                    hash.put(line.substring(6), value);
                                }
                            }else if(line.equals("lock")){
                                hash.lock(c.name);
                            }else if(line.equals("unlock")){
                                hash.unlock(c.name);
                            }else if(line.equals("hashtable")){
                                String filter = in.nextLine();
                                try{
                                    LinkedList<BlockedEntry> list = new LinkedList<>();
                                    for(BlockedEntry e : hash.entrySet()){
                                        if(e.getKey().matches(filter)){
                                            list.addLast(e);
                                        }
                                    }
                                    BlockedEntry array[] = list.toArray(new BlockedEntry[list.size()]);
                                    Arrays.sort(array, compartor);
                                    for(BlockedEntry e : array){
                                        out.println(e);
                                    }
                                    out.println("<end>");
                                    out.flush();
                                }catch(PatternSyntaxException ex){
                                    if(!disableWarnnings){
                                        System.err.println("warrning: node "+c.name+" send a pattern with syntax error: "+filter);
                                    }
                                    out.println(new BlockedEntry("Pattern", filter));
                                    out.println(new BlockedEntry("Exception", ex.getDescription().replaceAll("\n", "@")));
                                    out.println("<end>");
                                    out.flush();
                                }
                            }else if(line.equals("stream")){
                                String filter = in.nextLine();
                                for(BlockedEntry e : hash.entrySet()){
                                    if(e.getKey().matches(filter)){
                                        out.println(e);
                                        hash.put(e.getKey(), "");
                                    }
                                }
                                out.println("<end>");
                                out.flush();
                            }else{
                                Object obj = hash.get(c.getThreadIO()+".stream");
                                String value;
                                if(obj==null){
                                    value = line;
                                }else{
                                    value = obj.toString()+"@"+line;
                                }
                                if(value.length()>streamOutputSize){
                                    //hash.put(c.name+".cuts", value);
                                    value = value.substring(value.length()-streamOutputSize);
                                }
                                hash.put(c.getThreadIO()+".stream", value);

                                //System.err.println("line = '"+line+"' not valid");
                            }
                        }
                        System.out.println("\t"+c.getThreadIO()+": finished");
                        hash.put(c.getThreadIO()+".state", "finished");
                    }
                });
                Executors.newSingleThreadExecutor().execute(new RunnableException() {
                    @Override
                    public void exit_error(Throwable ex) {
                        hash.put(c.getThreadErr()+".state", "error");
                        System.err.printf("exit %s by following error:\n", c.getThreadErr());
                        ex.printStackTrace(System.err);
                        c.clearErr_counts();
                    }
                    @Override
                    public void running() throws InterruptedException {
                        if(!fast && delayStartListen>0){
                            //System.out.println("\t"+c.getThreadErr()+": performing delayStartListen = "+delayStartListen+" parameter");
                            Thread.sleep(delayStartListen);
                        }
                        System.out.println("\t"+c.getThreadErr()+": start to listen");
                        hash.put(c.getThreadErr()+".state", "started");

                        Scanner in = new Scanner(c.getProcess().getErrorStream());
                        c.clearErr_counts();
                        while(in.hasNextLine()){
                            String line = in.nextLine();
                            c.addErr_counts(1);

                            Object obj = hash.get(c.getThreadErr()+".stream");
                            String value;
                            if(obj==null){
                                value = line;
                            }else{
                                value = obj.toString()+"@"+line;
                            }
                            if(value.length()>streamErrorSize){
                                //hash.put(c.name+".cuts", value);
                                value = value.substring(value.length()-streamErrorSize);
                            }
                            hash.put(c.getThreadErr()+".stream", value);
                        }
                        System.out.println("\t"+c.getThreadErr()+": finished");

                        hash.put(c.getThreadErr()+".state", "finished");
                    }
                });
            }

            if(!fast && delayActivity>0){
                //System.out.println("Performing delayActivity = "+delayActivity+" parameter");
                Thread.sleep(delayActivity);
            }

            System.out.println("Start loop for activity verification:");
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        while(true){
                            if(sleepActivity>0){
                                Thread.sleep(sleepActivity);
                            }

                            for(ChildProcess c : process){
                                hash.put(c.name+".alive", c.getProcess().isAlive()?"1":"0");
                                if(!c.getProcess().isAlive()){
                                    hash.put(c.name+".exit", ""+c.getProcess().exitValue());
                                }
                                hash.put(c.getThreadIO()+".activity", ""+c.getIO_counts());
                                hash.put(c.getThreadErr()+".activity", ""+c.getErr_counts());
                                c.clearIO_counts();
                                c.clearErr_counts();
                            }
                        }
                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
            });

            if(!fast && delayActivity>0){
                //System.out.println("Performing delayUserInterface = "+delayUserInterface+" parameter");
                Thread.sleep(delayUserInterface);
            }

            System.out.println("Start user interface:");

            Scanner input = new Scanner(System.in);
            String line;
            do{
                System.out.println("-------------------------------[ options ]---------------------------");
                System.out.println(">> print        # to print the current hashtable state");
                System.out.println(">> exit         # to shutdown propley this aplication");
                System.out.println("---------------------------------------------------------------------");
                System.out.print(">> ");
                line = input.nextLine();
                if(line.equals("print")){
                    System.out.println("--------------------------------[HASH]-------------------------------");
                    LinkedList<BlockedEntry> list = hash.entrySet();
                    BlockedEntry array[] = list.toArray(new BlockedEntry[list.size()]);
                    Arrays.sort(array, compartor);

                    for(BlockedEntry e : array){
                        String value = e.getValue().toString();
                        if(value.contains("@")){
                            System.out.println(e.getKey()+":");
                            String v[] = value.split("@");
                            if(v.length>10){
                                for(int i=0; i<5; i++){
                                    System.out.println("\t"+v[i]);
                                }
                                System.out.println("\tresumed(...)");
                                for(int i=v.length-5; i<v.length; i++){
                                    System.out.println("\t"+v[i]);
                                }
                            }else{
                                for(int i=0; i<v.length; i++){
                                    System.out.println("\t"+v[i]);
                                }
                            }
                        }else{
                            System.out.println(e);
                        }
                    }
                    System.out.println("---------------------------------------------------------------------");
                }
            }while(!line.equals("exit"));
        }catch(Throwable ex){
            ex.printStackTrace();
        }
        shutdown();
    }
    
    public static void shutdown(){
        System.out.println("Shutdown:");
        for(ChildProcess c : process){
            try {
                if(c!=null && c.getProcess()!=null){
                    System.out.println("\tcall destroy to "+c.name+", current status alive = "+c.getProcess().isAlive());
                    c.getProcess().destroy();
                    c.getProcess().getErrorStream().close();
                    c.getProcess().getInputStream().close();
                    c.getProcess().getOutputStream().close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        System.out.println("Waiting for destroy child process sucess:");
        boolean sucess = true;
        for(ChildProcess c : process){
            try {
                if(c!=null && c.getProcess()!=null){
                    boolean temp = c.getProcess().waitFor(2, TimeUnit.SECONDS);
                    sucess = sucess && temp;
                    System.out.println("\tprocess "+c.name+" sucess destroy = "+temp);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        if(sucess){
            System.out.println("Exiting with full sucess on shutdown:");
        }else{
            System.out.println("Exiting without sucess on shutdown, please verify and close the child aplications by your self");
        }
        System.exit(0); 
    }
    
    public static int intParam(String[] args, String param, int default_value){
        for(int n=2; n<args.length; n+=2){
            if(args[n].equals(param)){
                return Integer.parseInt(args[n+1]);
            }
        }
        return default_value;
    }
    public static boolean boolParam(String[] args, String param, boolean default_value){
        for(int n=2; n<args.length; n+=2){
            if(args[n].equals(param)){
                return Boolean.parseBoolean(args[n+1]);
            }
        }
        return default_value;
    }
    
    private static void validating(String[] args) throws Exception{
        //validating the input
        if(args[0].equals("-dir")){
            File modules = new File(args[1]);
            if(!modules.exists()){
                throw new Exception("Can't find the directory: "+modules.getCanonicalPath());
            }else if(!modules.isDirectory()){
                throw new Exception("Is not a directory: "+modules.getCanonicalPath());
            }
        }else if(args[0].equals("-file")){
            File modules = new File(args[1]);
            if(!modules.exists()){
                throw new Exception("Can't find the file: "+modules.getCanonicalPath());
            }else if(!modules.isFile()){
                throw new Exception("Is not a file: "+modules.getCanonicalPath());
            }
        }else{
            throw new Exception("Frist arg not valid, arg found: ["+args[0]+"]\n usage: -dir or -file");
        }
    }
}
