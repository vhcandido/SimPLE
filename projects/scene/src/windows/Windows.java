/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import lib.sFrame;
import scene.World;

/**
 *
 * @author marcio
 */
public class Windows extends sFrame{
    private static final UIManager.LookAndFeelInfo[] LookAndFeels = UIManager.getInstalledLookAndFeels();
    
    public final DrawSpace draw;
    
    public Windows(final World world){
        super(new FlowLayout(FlowLayout.CENTER, 5, 5), "Draw for Simple Simulation");
        draw = new DrawSpace(Color.WHITE, world);
        add(draw);


        int index = -1;
        for(int i=0; i<LookAndFeels.length; i++){
            if(LookAndFeels[i].getName().contains("Nimbus")){
                index = i;
            }
            //System.out.println("Name  : " + LookAndFeels[i].getName());
            //System.out.println("Class : " + LookAndFeels[i].getClassName());
            
        }
        if(index!=-1) setLookAndFeel(LookAndFeels[index].getClassName());
    }
    

    @Override
    public void Config(int w, int h) {
        setSize(w, h);
        //tabbedPane.Config(w-30, 100);
        //progressBar.setPreferredSize(new Dimension(w-30, 23));
        //rigth.Config(280, h-53);
        //scrollPane.setPreferredSize(new Dimension(280-12, h-153));
    
        draw.Config(w-22, h-53);
    }
    
    
    
    
    public void setLookAndFeel(String lookAndFeelds){
        try {
            UIManager.setLookAndFeel(lookAndFeelds);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
