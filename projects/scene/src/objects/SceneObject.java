/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import windows.Graphics2DReal;
import java.awt.Color;

/**
 *
 * @author marci
 */
public abstract class SceneObject {
    
    private final String key;
    protected Color fill;
    protected Color draw;

    public SceneObject(String key, Color fill, Color draw) {
        this.key = key;
        this.fill = fill;
        this.draw = draw;
    }
    public SceneObject(String key) {
        this(key, Color.WHITE, Color.BLACK);
    }
    
    public abstract void update(String value) throws Throwable;
    public abstract void paint(Graphics2DReal gr);

    public static int Unit(double x){
        return (int)(x*100 + (x<0 ? -0.5 : +0.5));
    }
    
    public void setDraw(Color draw) {
        this.draw = draw;
    }
    public void setFill(Color fill) {
        this.fill = fill;
    }
    
    public static void main(String[] args) {
        double x = 0.011;
        System.out.println(Unit(x));
        System.out.println(Unit(-x));
        
    }
}
