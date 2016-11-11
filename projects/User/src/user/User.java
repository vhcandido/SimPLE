/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import SimPLE.Module;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

/**
 *
 * @author marci
 */
public class User extends Module{
    private final double TH = 0.1;
    private final double EP = Math.PI/20;
    
    private double thrust = 0;
    private double epsilon = 0;
    
    private final JFrame frame;
    public User(String ID) {
        super(ID);
        frame = new JFrame("controler");
        frame.setSize(100, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_UP){
                    thrust += TH;
                }
                if(e.getKeyCode()==KeyEvent.VK_DOWN){
                    thrust -= TH;
                }
                if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                    epsilon += EP;
                }
                if(e.getKeyCode()==KeyEvent.VK_LEFT){
                    epsilon -= EP;
                }
                thrust = Math.min(thrust, 1);
                thrust = Math.max(thrust, 0);
                
                epsilon = Math.min(epsilon, +Math.PI/4);
                epsilon = Math.max(epsilon, -Math.PI/4);
                
                writeDbl("thrust", thrust);
                writeDbl("epsilon", epsilon);
                //System.out.println(e);
                //System.out.print("thrust = "+thrust+" | ");
                //System.out.println("epsilon = "+epsilon);
                //writeDbl("epsilon", 0.0);
            }
        });
        frame.setVisible(true);
    }
    
    @Override
    public void start() throws Throwable {
        writeDbl("thrust", 0.0);
        writeDbl("epsilon", 0.0);
    }
    
    @Override
    public void loop() throws Throwable {
        sleep_ms(1000);
    }
}
