/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JScrollPane;

/**
 *
 * @author Márcio
 */
public class sScrollPane extends JScrollPane implements iDimensions{
    public void Config(int w, int h) {
        this.setPreferredSize(new Dimension(w, h));
        for(Component c : getViewport().getComponents()){
            if(c instanceof iDimensions){
                ((iDimensions)c).Config(w-24, h-6);
            }
            //setViewportView(c);
        }
    }
    /*@Override
    public void setEnabled(boolean enabled) {
        if(enabled){
            this.changeToDefaultBackground();
        }else{
            this.setBackground(Color.LIGHT_GRAY);
        }
        for(Component c : getComponents()){
            if(c instanceof JLabel){
                
            }else{
                c.setEnabled(enabled);
            }
        }
    }*/
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
}
