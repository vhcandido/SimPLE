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
public class setCamera extends SceneObject{
    public int x,y; 
    public setCamera(String key) {
        super(key);
    }
    @Override
    public void update(String value) {
        String v[] = value.split(";");
        x = Unit(Double.parseDouble(v[0]));
        y = Unit(Double.parseDouble(v[1]));
    }
    @Override
    public void paint(Graphics2DReal gr) {
        //gr.g2.drawRect(x, y, w, h);
    }
    @Override
    public String toString() {
        return "["+x+","+y+"]";
    }
}
