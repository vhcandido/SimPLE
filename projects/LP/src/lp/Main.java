/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lp;

import Linear.LP;
import Plan.Plan;
import RHC.RHC;

/**
 *
 * @author marci
 */
public class Main {
    public static void main(String[] args) throws Throwable {
        LP module = new LP("controler", false);
        //LP module = new LP("controler", true);
        //RHC module = new RHC("controler");
        //Plan module = new Plan("controler");
        module.run(args);
    }
}
