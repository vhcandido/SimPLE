/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

/**
 *
 * @author marci
 */
public abstract class RunnableException implements Runnable{
    public abstract void running() throws Throwable;
    public abstract void exit_error(Throwable ex);
    
    @Override
    public final void run() {
        try {
            running();
        } catch (Throwable ex) {
            exit_error(ex);
        }
    }
    
    public void sleep(long miniseconds) throws InterruptedException{
        //try {
            Thread.sleep(miniseconds);
        //} catch (InterruptedException ex) {
        //    ex.printStackTrace();
        //}
    }
}
