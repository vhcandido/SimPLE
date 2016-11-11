/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Color;
import windows.Graphics2DReal;

/**
 *
 * @author marci
 */
public class setColor extends SceneObject{
    private int r,g,b,a; 
    public setColor(String key) {
        super(key);
    }
    @Override
    public void update(String value) {
        String v[] = value.split(";");
        r = Integer.parseInt(v[0]);
        g = Integer.parseInt(v[1]);
        b = Integer.parseInt(v[2]);
        a = Integer.parseInt(v[3]);
    }
    @Override
    public void paint(Graphics2DReal gr) {
        gr.g2.setColor(new Color(r, g, b, a));
    }
    @Override
    public String toString() {
        return "["+r+","+g+","+b+","+a+"]";
    }
}
