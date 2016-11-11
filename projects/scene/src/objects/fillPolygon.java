/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Color;
import java.awt.Polygon;
import windows.Graphics2DReal;

/**
 *
 * @author marci
 */
public class fillPolygon extends SceneObject{
    private final Polygon poly = new Polygon(); 
    public fillPolygon(String key) {
        super(key);
    }
    @Override
    public void update(String value) {
        String v[] = value.split(";");
        int n_point = Integer.parseInt(v[v.length-1]);
        poly.reset();
        for(int n=0; n<n_point; n++){
            poly.addPoint(Unit(Double.parseDouble(v[2*n])), Unit(Double.parseDouble(v[2*n+1])));
        }
    }
    @Override
    public void paint(Graphics2DReal gr) {
        gr.g2.fillPolygon(poly);
    }
    @Override
    public String toString() {
        return "fill[poly]";
    }
}
