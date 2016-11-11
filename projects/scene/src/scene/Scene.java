/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import SimPLE.Module;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JFrame;
import windows.Windows;

/**
 *
 * @author marci
 */
public class Scene extends Module{

    public static void main(String[] args) throws Throwable {
        Scene scene = new Scene("scene");
        scene.run(args);
    }
    
    private final World world;
    private final Windows win;
    public Scene(String ID) {
        super(ID);
        world = new World();
        win = new Windows(world);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.Config(1000, 700);
        win.setVisible(true);
//        win.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                
////                if(e.getKeyCode()==KeyEvent.VK_UP){
////                    thrust += TH;
////                }
////                if(e.getKeyCode()==KeyEvent.VK_DOWN){
////                    thrust -= TH;
////                }
////                if(e.getKeyCode()==KeyEvent.VK_RIGHT){
////                    epsilon += EP;
////                }
////                if(e.getKeyCode()==KeyEvent.VK_LEFT){
////                    epsilon -= EP;
////                }
////                thrust = Math.min(thrust, 1);
////                thrust = Math.max(thrust, 0);
////                
////                epsilon = Math.min(epsilon, +Math.PI/4);
////                epsilon = Math.max(epsilon, -Math.PI/4);
////                
////                writeDbl("thrust", thrust);
////                writeDbl("epsilon", epsilon);
//                System.out.println(e.getKeyCode());
////                System.out.print("thrust = "+thrust+" | ");
////                System.out.println("epsilon = "+epsilon);
////                writeDbl("epsilon", 0.0);
//                eventInt("keyboard", e.getKeyCode());
//            }
//        });
    }
    
    @Override
    public void start() throws Throwable {
        
    }
    @Override
    public void loop() throws Throwable {
        
        ArrayList<Map.Entry<String, String>> list = hashtable("(.)*scene(.)*");
        //ArrayList<Map.Entry<String, String>> list = hashtable("(.)*");
        world.update(list);
        if(world.camera!=null){
            win.draw.setCamera(world.camera);
        }
        win.draw.repaint();
        //world.print();
        sleep_ms(10);
    }
}
