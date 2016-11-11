/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import static java.awt.RenderingHints.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Locale;
import javax.swing.JFrame;
import objects.setCamera;
/**
 *
 * @author Marcio
 */
public abstract class sPanelDraw extends sPanel {
    
    private final double taxaZoon = 1.125;

    private double zoom = 1.0;
    private double Cx = 0;
    private double Cy = 0;
    private double dx = 0;
    private double dy = 0;
    private double Xt;
    private double Yt;
    private boolean translated = false;

    private int width;
    private int height;

    private boolean anti_aliasing = true;
    private float composite = 1.0f;

    public sPanelDraw(Color default_color) {
        super(default_color);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setPreferredSize(new Dimension(width, height));
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                try{
                    MouseClicked(e);
                }catch(Throwable ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                try{
                    MousePressed(e);
                }catch(Throwable ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                try{
                    MouseReleased(e);
                }catch(Throwable ex){
                    ex.printStackTrace();
                }
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                try{
                    MouseMoved(e);
                }catch(Throwable ex){
                    ex.printStackTrace();
                }
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                try{
                    MouseDragged(e);
                }catch(Throwable ex){
                    ex.printStackTrace();
                }
            }
        });
        this.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                try{
                    MouseWheelMoved(e);
                }catch(Throwable ex){
                    ex.printStackTrace();
                }
            }
        });

        Init();
    }

    @Override
    public final void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        this.width = preferredSize.width;
        this.height = preferredSize.height;
        Xt = width / 2 - Cx * zoom;
        Yt = height / 2 - Cy * zoom;
        changeSize();
        repaint();
    }

    public void changeSize(){
        
    }
    
    public static void main(String args[]){
        JFrame frame = new JFrame("draw");
        
        sPanelDraw draw = new sPanelDraw(Color.WHITE) {
            @Override
            protected void paintComponentBefore(Graphics2D g2) {
                g2.drawRect(0, 0, width()-1, height()-1);
                
                /*
                g2.setColor(Color.red);
                g2.drawRect(width()/2-45, height()/2-45, 90, 90);
                
                g2.drawOval(width()/2-45, -100+height()/2-45, 90, 90);
                */
                
                //System.out.println(width() + " : "+height());
            }
            @Override
            protected void paintComponentAfter(Graphics2D g2) {
                g2.drawRect(-40, -40, 80, 80);
                
                g2.drawOval(-40, -140, 80, 80);
            }
            protected boolean MouseClicked(MouseEvent e, int X, int Y) {
                if(e.getButton() == MouseEvent.BUTTON1){
                    goTo(+0, -40, 40, 40);
                    return false;
                }
                return true;
            }
            
        };
        
        
        frame.add(draw);
        
        frame.setSize(1000, 700);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        draw.Config(900, 600);
        draw.ReiniciarSistema();
    }
    
    
    public void goTo(double x, double y, double w, double h){
        //ReiniciarSistema();
        setCentro(x+w/2, y+h/2);
        
        zoom = Math.min(width/w, height/h);
        
        repaint();
    }
    
    public void camera(double x, double y){
        this.Cx = x;
        this.Cy = y;
        Xt = width / 2 - Cx * zoom;
        Yt = height / 2 - Cy * zoom;
    }
    public void setCamera(setCamera cam){
        camera(cam.x, cam.y);
    }
    
    public int width() {
        return width;
    }
    public int height() {
        return height;
    }
    public double zoom(){
        return zoom;
    }
    public double Cx(){
        return Cx;
    }
    public double Cy(){
        return Cy;
    }
    
    public double Xi(){
        return getXReal(0);
    }
    public double Yi(){
        return getYReal(0);
    }
    public double Xf(){
        return getXReal(width);
    }
    public double Yf(){
        return getYReal(height);
    }
    
    //g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        //g2.setRenderingHint(KEY_RENDERING, VALUE_RENDER_SPEED);
        //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

    public void setAntiAliasing(boolean flag){
        anti_aliasing = flag;
    }
    public void setComposite(float composite){
        this.composite = composite;
    }

    protected final void Init() {
    }
    //------------------------------------------metodos privates--------------------------------

    private void MouseClicked(MouseEvent e) throws Throwable{
        boolean next = this.MouseClicked(e, e.getX(), e.getY());
        if (e.getButton() == MouseEvent.BUTTON3) {
            this.Cx = getXReal(e.getX());
            this.Cy = getYReal(e.getY());
            Xt = width / 2 - Cx * zoom;
            Yt = height / 2 - Cy * zoom;
            repaint();
        }else{
            if(next){
                this.MouseClicked(e, getXReal(e.getX()), getYReal(e.getY()));
            }
        } 
        //repaint();
    }

    private void MouseMoved(MouseEvent e) throws Throwable{
        if (width != getWidth() || height != getHeight()) {
            width = getWidth();
            height = getHeight();
        }
        this.MouseMoved(e, getXReal(e.getX()), getYReal(e.getY()));
        //repaint();
    }

    private void MouseWheelMoved(MouseWheelEvent e) throws Throwable{
        if(e.isAltDown() || e.isShiftDown() || e.isControlDown()){
            this.MouseWheelMoved(e, getXReal(e.getX()), getYReal(e.getY()));
        }else{
            zoom = zoom * (e.getWheelRotation() < 0 ? taxaZoon : 1.0 / taxaZoon);
            Xt = width / 2 - Cx * zoom;
            Yt = height / 2 - Cy * zoom;
            repaint();
        }
    }

    private Cursor before = null;
    private void MousePressed(MouseEvent e) throws Throwable{
        //this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

        //System.out.println(e.getButton() + " , " + MouseEvent.BUTTON3);
        if (e.getButton() == MouseEvent.BUTTON3) {
            this.before = this.getCursor();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            this.dx = getXReal(e.getX());
            this.dy = getYReal(e.getY());
            translated = true;
            repaint();
        }
        MousePressed(e, getXReal(e.getX()), getYReal(e.getY()));
        //repaint();
    }

    private void MouseReleased(MouseEvent e) throws Throwable{
        if(e.getButton() == MouseEvent.BUTTON3){
            this.setCursor(before);
        }
        translated = false;
        MouseReleased(e, getXReal(e.getX()), getYReal(e.getY()));
        //repaint();
    }

    private void MouseDragged(MouseEvent e) throws Throwable{
        if (translated) {
            //this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            this.Cx += dx - getXReal(e.getX());
            this.Cy += dy - getYReal(e.getY());
            this.dx = getXReal(e.getX());
            this.dy = getYReal(e.getY());

            Xt = width / 2 - Cx * zoom;
            Yt = height / 2 - Cy * zoom;
            repaint();
        }else{
            MouseDragged(e, getXReal(e.getX()), getYReal(e.getY()));
        }
        //repaint();
    }

    //------------------------------------------metodos publics e protecteds-------------------------------
    /**
     * @return retorna um ponto X com as cordenadas reais, de onde se clicou apos ter calculado o zoon
     */
    protected double getXReal(double X) {
        return this.Cx + (X - width / 2) / zoom;
    }

    /**
     * @return retorna um ponto Y com as cordenadas reais, de onde se clicou apos ter calculado o zoon
     */
    protected double getYReal(double Y) {
        return this.Cy + (Y - height / 2) / zoom;
    }
    
    protected double getXOrg(double X){
        return (X-Cx)*zoom + width/2;
    }
    protected double getYOrg(double Y){
        return (Y-Cy)*zoom + height/2;
    }

    protected void setCentro(double Cx, double Cy) {
        this.Cx = Cx;
        this.Cy = Cy;
        Xt = width / 2 - Cx * zoom;
        Yt = height / 2 - Cy * zoom;
        repaint();
    }
    protected void ReiniciarSistema() {
        this.Cx = 0;
        this.Cy = 0;
        dx = 0;
        dy = 0;
        zoom = 1;
        Xt = width / 2 - Cx * zoom;
        Yt = height / 2 - Cy * zoom;
        repaint();
    }



    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        if(anti_aliasing){
            g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        }
        if(composite<1.0f){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, composite));
        }
        //g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        //g2.setRenderingHint(KEY_RENDERING, VALUE_RENDER_SPEED);
        //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        //g2.scale(1, -1);
        
        
        
        g2.translate(Xt, Yt);
        g2.scale(zoom, zoom);
        g2.setStroke(new BasicStroke(1));
        paintComponentAfter(g2);

        g2.scale(1.0 / zoom, 1.0 / zoom);
        g2.translate(-Xt, -Yt);
        
       
        g2.setStroke(new BasicStroke(1));
        paintComponentBefore(g2);
//        g2.setStroke(new BasicStroke(1));
//        paintComponentBefore(g2);
    }
    private static Font fontRuler = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    protected void paintRuler(Graphics2D g2, int pixels_for_unit) {
        g2.setFont(fontRuler);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, width(), 40);
        g2.fillRect(0, 0, 40, height());
        g2.setColor(Color.black);
        g2.drawRect(0, 0, width(), 40);
        g2.drawRect(0, 0, 40, height());
        
        drawScaleNumeric(g2, width(), Cx(), true, pixels_for_unit);
        drawScaleNumeric(g2, height(), Cy(), false, pixels_for_unit);

        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, 40, 40);
        g2.setColor(Color.black);
        g2.drawRect(0, 0, 40, 40);
    }
    private void drawScaleNumeric(Graphics2D g2, int dimension, double center, boolean isX, int pixels_for_unit) {
        double dim1 = center - dimension * (zoom()) / 2;
        double dim2 = center + dimension * (zoom()) / 2;
        double delta = (dim2 - dim1) * 100 / dimension;
        double incr = (int) Math.log10(delta);
        incr = delta > Math.pow(10, incr + 1) / 2.0 ? incr + 1 : incr;
        incr = (int) Math.pow(10.0, incr);
        incr = incr / 100;
        delta = delta / incr;

        int i = 0;
        while ((int) (dimension / 2 - center * zoom() + i * delta) > 40) {
            i--;
        }
        while ((int) (dimension / 2 - center * zoom() + i * delta) < 40) {
            i++;
        }
        while ((int) (dimension / 2 - center * zoom() + i * delta) < dimension) {
            g2.setColor(Color.BLACK);
            if (isX) {
                g2.drawLine((int) (dimension / 2 - center * zoom() + i * delta), 40 - 1,
                        (int) (dimension / 2 - center * zoom() + i * delta), 30 - 1);
                g2.setColor(Color.GRAY);
                g2.drawLine((int) (dimension / 2 - center * zoom() + i * delta) + 1, 40 - 1,
                        (int) (dimension / 2 - center * zoom() + i * delta) + 1, 30 - 1);
                g2.setColor(Color.BLACK);
                double val = (double)((int)(i * 100 / (incr)))/pixels_for_unit;
                if(1/zoom() < 1){
                     g2.drawString(String.format(Locale.ENGLISH, "%3.1f", val),
                        (int) (dimension / 2 - center * zoom() + i * delta), 20);
                }else if(1/zoom() < 10){
                    g2.drawString(String.format(Locale.ENGLISH, "%3.0f", val),
                        (int) (dimension / 2 - center * zoom() + i * delta), 20);
                }else if(1/zoom() < 100){
                    g2.drawString(String.format(Locale.ENGLISH, "%3.0f", val),
                        (int) (dimension / 2 - center * zoom() + i * delta), 20);
                }else if(1/zoom() < 100000){
                    g2.drawString(String.format(Locale.ENGLISH, "%3.0f", val),
                        (int) (dimension / 2 - center * zoom() + i * delta), 20);
                }else{
                    g2.drawString(String.format(Locale.ENGLISH, "%g", val),
                        (int) (dimension / 2 - center * zoom() + i * delta), 20);
                }
                //g2.drawString(String.format(Locale.ENGLISH, "%2g", 1/zoom()),
                //        (int) (dimension / 2 - center * zoom() + i * delta), 20);
            } else {
                g2.drawLine(30 - 1, (int) (dimension / 2 - center * zoom() + i * delta),
                        40 - 1, (int) (dimension / 2 - center * zoom() + i * delta));
                g2.setColor(Color.GRAY);
                g2.drawLine(30 - 1, (int) (dimension / 2 - center * zoom() + i * delta) + 1,
                        40 - 1, (int) (dimension / 2 - center * zoom() + i * delta) + 1);
                g2.setColor(Color.BLACK);
                
                double val = (double)((int)(i * 100 / (incr)))/pixels_for_unit;
                if(1/zoom() < 1){
                     g2.drawString(String.format(Locale.ENGLISH, "%3.1f", val),
                        5, (int) (dimension / 2 - center * zoom() + i * delta));
                }else if(1/zoom() < 10){
                    g2.drawString(String.format(Locale.ENGLISH, "%3.0f", val),
                        5, (int) (dimension / 2 - center * zoom() + i * delta));
                }else if(1/zoom() < 100){
                    g2.drawString(String.format(Locale.ENGLISH, "%3.0f", val),
                        5, (int) (dimension / 2 - center * zoom() + i * delta));
                }else if(1/zoom() < 100000){
                    g2.drawString(String.format(Locale.ENGLISH, "%3.0f", val),
                        5, (int) (dimension / 2 - center * zoom() + i * delta));
                }else{
                    g2.drawString(String.format(Locale.ENGLISH, "%g", val),
                        5, (int) (dimension / 2 - center * zoom() + i * delta));
                }
                
                //g2.drawString(String.format(Locale.ENGLISH, "%2.2f", (double)((int)(i * 100 / (incr)))/pixels_for_unit), 5,
                //        (int) (dimension / 2 - center * zoom() + i * delta));
            }
            i++;
        }
    }

    
    //------------------------------------------metodos abstratos-----------------------------
    /**
     * @param e MouseEvent
     * @param X Posição X real, de onde se clicou, considerando o zoon
     * @param Y Posição Y real, de onde se clicou, considerando o zoon
     */
    protected void MouseClicked(MouseEvent e, double X, double Y) throws Throwable{
    }
    protected boolean MouseClicked(MouseEvent e, int X, int Y) throws Throwable{
        return true;
    }
    /**
     * @param e MouseEvent
     * @param X Posição X real, de onde se clicou, considerando o zoon
     * @param Y Posição Y real, de onde se clicou, considerando o zoon
     */
    protected void MouseMoved(MouseEvent e, double X, double Y) throws Throwable{
    }
    

    /**
     * @param e MouseEvent
     * @param X Posição X real, de onde se clicou, considerando o zoon
     * @param Y Posição Y real, de onde se clicou, considerando o zoon
     */
    protected void MouseWheelMoved(MouseWheelEvent e, double X, double Y) throws Throwable{
    }

    protected void MousePressed(MouseEvent e, double X, double Y) throws Throwable{
    }

    protected void MouseReleased(MouseEvent e, double X, double Y) throws Throwable{
    }
    protected void MouseDragged(MouseEvent e, double X, double Y) throws Throwable{
    }



    /**Pinta no Graphics antes da mudança de cordenadas devido a translação e o zoon
     * que será aplicado.
     * @param g2
     */
    protected void paintComponentBefore(Graphics2D g2) {
    }

    /**Pinta no Graphics depois da mudança de cordenadas devido a translação e o zoon
     * que será aplicado.(apos)<Br>
     * g2.translate(width/2-Cx*zoon, height/2-Cy*zoon);<br>
     * g2.scale(zoon, zoon);<br>
     * @param g2
     */
    protected void paintComponentAfter(Graphics2D g2) {
    }
}
