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
public class fillArc extends SceneObject{
    private int x,y,w,h,startAngle, arcAngle;
    public fillArc(String key) {
        super(key);
    }
    @Override
    public void update(String value) {
        String v[] = value.split(";");
        x = Unit(Double.parseDouble(v[0]));
        y = Unit(Double.parseDouble(v[1]));
        w = Unit(Double.parseDouble(v[2]));
        h = Unit(Double.parseDouble(v[3]));
        startAngle = Integer.parseInt(v[4]);
        arcAngle = Integer.parseInt(v[5]);
    }
    @Override
    public void paint(Graphics2DReal gr) {
        gr.g2.fillArc(x, y, w, h, startAngle, arcAngle);
    }
    @Override
    public String toString() {
        return "["+x+","+y+","+w+","+h+","+startAngle+","+arcAngle+"]";
    }
}
