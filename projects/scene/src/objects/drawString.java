/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import javax.script.ScriptException;
import windows.Graphics2DReal;

/**
 *
 * @author marci
 */
public class drawString extends SceneObject{
    private String str;
    private int x, y; 
    public drawString(String key) {
        super(key);
    }
    @Override
    public void update(String value){
        String v[] = value.split(";");
        str = v[0];
        x = Unit(Double.parseDouble(v[1]));
        y = Unit(Double.parseDouble(v[2]));
    }
    @Override
    public void paint(Graphics2DReal gr) {
        gr.g2.drawString(str, x, y);
    }
    @Override
    public String toString() {
        return "["+str+","+x+","+y+"]";
    }
}
