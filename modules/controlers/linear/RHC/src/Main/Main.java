/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import RHC.RHC;



/**
 *
 * @author marci
 */
public class Main {
    public static void main(String[] args) throws Throwable {
        RHC module = new RHC("controler");
        module.run(args);
    }
}
