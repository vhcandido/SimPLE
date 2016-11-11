/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Linear.RLP;

/**
 *
 * @author marci
 */
public class Main {
    public static void main(String[] args) throws Throwable {
        RLP module = new RLP("controler");
        module.run(args);
    }
}
