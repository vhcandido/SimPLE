/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author marci
 */
public class ChildProcess {
    
    public final String name;
    public final String cmd;
    public final File dir;
    
    private Process process;
    
    public ChildProcess(String name, String cmd, File dir) {
        this.name = name;
        this.cmd = cmd;
        this.dir = dir;
    }
    public String getThreadIO(){
        return name+".thread.I/O";
    }
    public String getThreadErr(){
        return name+".thread.err";
    }
    public void start() throws IOException{
        if(process==null){
            process = Runtime.getRuntime().exec(cmd, null, dir);
        }
    }
    public Process getProcess() {
        return process;
    }
    
    private static boolean matches(String line, String var){
        return line.matches("(\\s*)("+var+")(\\s*)=(\\s*)\\[(.*)\\];");
    }
    public static ChildProcess read(Scanner sc) throws Exception{
        if(sc.hasNextLine()){
            String line = sc.nextLine();
            while(!line.startsWith("module") && sc.hasNextLine()){
                line = sc.nextLine();
            }
            if(sc.hasNextLine()){
                String name = null;
                String cmd = null;
                String dir = null;
                line = sc.nextLine();
                while(!line.startsWith("end") && sc.hasNextLine()){
                    //System.out.println(line);
                    if(matches(line, "name")){
                        name = line.substring(line.indexOf("[")+1, line.indexOf("]"));
                    }else if(matches(line, "cmd")){
                        cmd = line.substring(line.indexOf("[")+1, line.indexOf("]"));
                    }else if(matches(line, "dir")){
                        dir = line.substring(line.indexOf("[")+1, line.indexOf("]"));
                    }else{
                        throw new Exception("line: '"+line+"' not valid");
                    }
                    line = sc.nextLine();
                }
                File f = new File(dir);
                if(!f.exists()){
                    throw new Exception("Can't find the directory: "+f);
                }
                if(!f.isDirectory()){
                    throw new Exception("Is not a directory: "+f.getCanonicalPath());
                }
                
                return new ChildProcess(name, cmd, f);
            }
        }
        return null;
    }
    
    
    private long IO_counts = 0;
    public synchronized void addIO_counts(long value){
        IO_counts += value;
    }
    public synchronized void clearIO_counts(){
        IO_counts = 0;
    }
    public synchronized long getIO_counts(){
        return IO_counts;
    }

    private long Err_counts = 0;
    public synchronized void addErr_counts(long value){
        Err_counts += value;
    }
    public synchronized void clearErr_counts(){
        Err_counts = 0;
    }
    public synchronized long getErr_counts(){
        return Err_counts;
    }
}
