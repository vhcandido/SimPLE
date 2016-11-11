/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JTabbedPane;

/**
 *
 * @author marcio
 */
public class sTabbedPane extends JTabbedPane implements iDimensions{
    
    @Override
    public void Config(int w, int h) {
        this.setPreferredSize(new Dimension(w, h));
        for(Component c : getComponents()){
            if(c instanceof iDimensions){
                ((iDimensions)c).Config(w+3, h-26);
            }
        }
    }
    private boolean fristSise = true;
    @Override
    public void setPreferredSize(Dimension preferredSize) {
        if(fristSise){
            fristSise = false;
            Original(preferredSize.width, preferredSize.height);
            super.setPreferredSize(preferredSize);
        }else{
            super.setPreferredSize(preferredSize);
        }
    }
    private int w, h;
    @Override
    public void Original(int w, int h) {
        this.w = w;
        this.h = h;
    }
    @Override
    public int oWidth() {
        return w;
    }
    @Override
    public int oHeight() {
        return h;
    }
    @Override
    public Dimension oDimension() {
        return new Dimension(w, h);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getPreferredSize().width-1, getPreferredSize().height-1);
    }
}
