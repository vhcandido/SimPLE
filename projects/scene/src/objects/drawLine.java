/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import windows.Graphics2DReal;

/**
 *
 * @author marci
 */
public class drawLine extends SceneObject{
    private int x1,y1,x2,y2; 
    public drawLine(String key) {
        super(key);
    }
    @Override
    public void update(String value) {
        String v[] = value.split(";");
        x1 = Unit(Double.parseDouble(v[0]));
        y1 = Unit(Double.parseDouble(v[1]));
        x2 = Unit(Double.parseDouble(v[2]));
        y2 = Unit(Double.parseDouble(v[3]));
    }
    @Override
    public void paint(Graphics2DReal gr) {
        gr.g2.drawLine(x1, y1, x2, y2);
    }
    @Override
    public String toString() {
        return "["+x1+","+y1+","+x2+","+y2+"]";
    }
}
