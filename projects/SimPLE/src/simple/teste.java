/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import java.util.concurrent.Executors;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


/**
 *
 * @author marci
 */
public class teste {
    
    
    
    public static final BlockedHash<String> hash = new BlockedHash<>();
    
    //public static final ConcurrentLinkedDeque<String> bloked = new ConcurrentLinkedDeque<>();
    
    public static void main(String[] args) throws ScriptException {

        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        
        //String foo = "var x = 10;x += 5;if(x<10){x = -1;}else{x = -2;}x-10;";
        String foo = "Math.PI";
        System.out.println(engine.eval(foo));
        
    }
    public static void main1(String[] args) {
        
        
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            int n=0;
            @Override
            public void run() {
                try {
                    while(true){
                        hash.lock("threadA");
                        for(int i=0; i<10; i++){
                            hash.put("["+i+"]", ""+(n));
                            Thread.sleep(40);
                        }
                        hash.unlock("threadA");
                        //Thread.sleep(100);
                        n++;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true){
                        System.out.println("---------------------");
                        for(BlockedEntry e : hash.entrySet()){
                            System.out.println(e);
                            Thread.sleep(30);
                        }
                        Thread.sleep(100);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
