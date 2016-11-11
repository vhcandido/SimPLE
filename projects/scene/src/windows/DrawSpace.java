/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.sPanelDraw;
import scene.World;

/**
 *
 * @author marcio
 */
public class DrawSpace extends sPanelDraw{
    public static final int GRID_NOT        = 0;
    public static final int GRID_BEFORE     = 1;
    public static final int GRID_AFTER      = 2;

    public final Cursor CursorHand = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    public final Cursor CursorDefault = Cursor.getDefaultCursor();
    
    
    private int grid  = GRID_NOT;
    
    private final Random rnd = new Random();
    
    private final World sim;
    
    public DrawSpace(Color default_color, World sim) {
        super(default_color);
        this.sim = sim;
    }
    private static double dist(double x1, double y1, double x2, double y2){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }
    public void clear() {
        repaint();
    }
    public String change_grid(){
        grid = (grid +1) % 3;
        repaint();
        return "Grid [ "+grid+" ]";
    }
    
    @Override
    protected void MouseClicked(MouseEvent e, double X, double Y) throws Throwable {
        
    }
    @Override
    protected void MouseMoved(MouseEvent e, double X, double Y) {
        
    }
    public static final int Grid = 100;

    //private double xo,yo;
    @Override
    protected void MousePressed(MouseEvent e, double X, double Y) {

    }

    @Override
    protected void MouseDragged(MouseEvent e, double X, double Y) {
        
    }

    @Override
    protected void MouseReleased(MouseEvent e, double X, double Y) {
        
    }
    
    
    
    @Override
    protected void paintComponentBefore(Graphics2D g2) {
        paintRuler(g2, 1);
        if(grid == GRID_NOT){
            g2.setColor(Color.WHITE);
            g2.fillRect(10, 10, 20, 20);
            
            g2.setColor(Color.BLACK);
            g2.drawRect(10, 10, 20, 20);
        }else if(grid == GRID_BEFORE){
            g2.setColor(Color.WHITE);
            g2.fillRect(10, 10, 20, 20);
            
            g2.setColor(Color.BLACK);
            g2.drawRect(10, 10, 10, 10);
            g2.drawRect(10, 20, 10, 10);
            g2.drawRect(20, 10, 10, 10);
            g2.drawRect(20, 20, 10, 10);
            
            g2.setColor(Color.ORANGE);
            g2.fillRect(15, 15, 11, 11);
        }else{
            g2.setColor(Color.WHITE);
            g2.fillRect(10, 10, 20, 20);
            
            g2.setColor(Color.ORANGE);
            g2.fillRect(15, 15, 11, 11);
            
            g2.setColor(Color.BLACK);
            g2.drawRect(10, 10, 10, 10);
            g2.drawRect(10, 20, 10, 10);
            g2.drawRect(20, 10, 10, 10);
            g2.drawRect(20, 20, 10, 10);
        }
    }
    @Override
    protected void paintComponentAfter(Graphics2D g2) {
        if(grid == GRID_BEFORE){
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1));
            int xi = sim.lower(Xi());
            int xf = sim.uper(Xf());
            int yi = sim.lower(Yi());
            int yf = sim.uper(Yf());
            for(int x = xi; x<=xf; x+=sim.size){
                g2.drawLine(x, yi, x, yf);
            }
            for(int y=yi; y<=yf; y+=sim.size){
                g2.drawLine(xi, y, xf, y);
            }
        }
        
        sim.paint(g2);
        
        if(grid == GRID_AFTER){
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1));
            int xi = sim.lower(Xi());
            int xf = sim.uper(Xf());
            int yi = sim.lower(Yi());
            int yf = sim.uper(Yf());
            for(int x = xi; x<=xf; x+=sim.size){
                g2.drawLine(x, yi, x, yf);
            }
            for(int y=yi; y<=yf; y+=sim.size){
                g2.drawLine(xi, y, xf, y);
            }
        }
    }

    
}
