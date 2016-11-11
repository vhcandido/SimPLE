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
public class setRotate extends SceneObject{
    private double theta;
    public setRotate(String key) {
        super(key);
    }
    @Override
    public void update(String value) {
        theta = Double.parseDouble(value);
    }
    @Override
    public void paint(Graphics2DReal gr) {
        gr.g2.rotate(theta);
    }
    @Override
    public String toString() {
        return "["+theta+"]";
    }
}
