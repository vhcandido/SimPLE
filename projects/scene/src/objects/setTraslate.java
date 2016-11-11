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
public class setTraslate extends SceneObject{
    private int x,y;
    public setTraslate(String key) {
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
        gr.g2.translate(x, y);
    }
    @Override
    public String toString() {
        return "["+x+","+y+"]";
    }
}
