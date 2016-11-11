/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import javax.script.ScriptException;
import windows.Graphics2DReal;

/**
 *
 * @author marci
 */
public class drawImage extends SceneObject{
    private Image img;
    private File file;
    private int x, y, w, h; 
    public drawImage(String key) {
        super(key);
    }
    @Override
    public void update(String value) {
        String v[] = value.split(";");
        file = new File(v[0]);
        x = Unit(Double.parseDouble(v[1]));
        y = Unit(Double.parseDouble(v[2]));
        w = Unit(Double.parseDouble(v[3]));
        h = Unit(Double.parseDouble(v[4]));
        img = Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
    }
    @Override
    public void paint(Graphics2DReal gr) {
        if(file.exists() && file.isFile()){
            gr.g2.drawImage(img, x, y, w, h, null);
        }else{
            gr.g2.drawRect(x, y, w, h);
            gr.g2.drawString("not found: "+file.getAbsolutePath(), x+5, y+12);
        }
    }
    @Override
    public String toString() {
        return "["+file+","+x+","+y+"]";
    }
}
