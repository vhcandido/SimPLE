/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimPLE;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 *
 * @author marci
 */
public abstract class Module {
    
    public abstract void start() throws Throwable;
    public abstract void loop() throws Throwable;

    private final String ID;
    private final Scanner in;
    private final long start_time;
    public Module(String ID) {
        this.ID = ID;
        this.in = new Scanner(System.in);
        start_time = System.currentTimeMillis();
    }
    
    
    public final void run(String[] args) throws Throwable {
        Locale.setDefault(Locale.ENGLISH);
        
        writeInt("started", 0);
        writeInt("loops", 0);

        start();

        writeInt("started", 1);

        int n=1;
        while(true){
            writeInt("loops", n);
            loop();
            n++;
        }
    }
    public final void sleep_ms(int milliseconds) throws InterruptedException{
        Thread.sleep(milliseconds);
    }
    public final double time_sec(){
        return (System.currentTimeMillis() - start_time)/1000.0;
    }
    
    private double time_mark = -1;
    public final double time_elapsed(){
        double now = time_sec();
        double elapsed;
        if(time_mark<0){
            elapsed = now;
        }else{
            elapsed = now-time_mark;
        }
        time_mark = now;
        return elapsed;
    }
    public final ArrayList<Entry<String, String>> hashtable(String filter){
        System.out.printf("hashtable\n");
        System.out.printf("%s\n", filter);
        System.out.flush();
        
        ArrayList<Entry<String, String>> list = new ArrayList<>();
        String line = in.nextLine();
        while(!line.equals("<end>")){
            String args[] = line.split("=");
            list.add(new AbstractMap.SimpleEntry(args[0], args[1]));
            line = in.nextLine();
        }
        return list;
    }
    public final void writeDbl(String name, double value){
        System.out.printf("write.%s.%s\n", ID, name);
        System.out.printf("%g\n", value);
        System.out.flush();
    }
    public final void writeInt(String name, int value){
        System.out.printf("write.%s.%s\n", ID, name);
        System.out.printf("%d\n", value);
        System.out.flush();
    }
    public final double readDbl(String path){
        System.out.printf("read.%s\n", path);
        System.out.flush();
        return Double.parseDouble(in.nextLine());
    }
    public final int readInt(String path){
        System.out.printf("read.%s\n", path);
        System.out.flush();
        return Integer.parseInt(in.nextLine());
    }
//    public final void paintRect(String name, double x, double y, double rx, double ry){
//        printf("write.scene."ID".%s.paintRect\n", name);
//        printf("%lf;%lf;%lf;%lf\n", x, y, rx, ry);
//        fflush(stdout);
//    }
}
