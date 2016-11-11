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
public abstract class GlobalValues{
    public final String key;
    public GlobalValues(String key) {
        this.key = key;
    }
    @Override
    public abstract String toString();
}
