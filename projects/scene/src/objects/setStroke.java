/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.BasicStroke;
import windows.Graphics2DReal;

/**
 *
 * @author marci
 */
public class setStroke extends SceneObject{
    private double with;
    public setStroke(String key) {
        super(key);
    }
    @Override
    public void update(String value) {
        with = Double.parseDouble(value);
    }
    @Override
    public void paint(Graphics2DReal gr) {
        gr.g2.setStroke(new BasicStroke((float)with));
    }
    @Override
    public String toString() {
        return "["+with+"]";
    }
}
