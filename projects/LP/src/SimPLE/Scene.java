/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimPLE;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Point2D;

/**
 *
 * @author marci
 */
public final class Scene {
    
    private int ordScene = 0;
    private final String ID; 
    public Scene(String ID) {
        this.ID = ID;
    }
    
    private String toStr(int ord){
        ord = ord<0 ? 0 : (ord>999999 ? 999999 : ord);
        return String.format("%6d", ord).replaceAll(" ", "0");
    }
    
    public void beginScene(int ord){
        ordScene = ord;
    }
    public int endScene(){
        return ordScene;
    }
    
    public int setTranslate(String name, double x, double y){
        System.out.printf("write.scene.%s."+ID+".%s.translate\n", toStr(ordScene++), name);
        System.out.printf("%g;%g\n", x, y);
        System.out.flush();
        return ordScene;
    }
    
    /**
    theta - the angle of rotation in radians
    */
    public int setRotate(String name, double theta){
        System.out.printf("write.scene.%s."+ID+".%s.rotate\n", toStr(ordScene++), name);
        System.out.printf("%g\n", theta);
        System.out.flush();
        return ordScene;
    }
    public int setStroke(String name, double with){
        System.out.printf("write.scene.%s."+ID+".%s.setStroke\n", toStr(ordScene++), name);
        System.out.printf("%g\n", with);
        System.out.flush();
        return ordScene;
    }
    public int setColor(String name, Color color){
        System.out.printf("write.scene.%s."+ID+".%s.setColor\n", toStr(ordScene++), name);
        System.out.printf("%d;%d;%d;%d\n", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        System.out.flush();
        return ordScene;
    }

    public int drawRect(String name, double x, double y, double w, double h){
        System.out.printf("write.scene.%s."+ID+".%s.drawRect\n", toStr(ordScene++), name);
        System.out.printf("%g;%g;%g;%g\n", x, y, w, h);
        System.out.flush();
        return ordScene;
    }
    public int fillRect(String name, double x, double y, double w, double h){
        System.out.printf("write.scene.%s."+ID+".%s.fillRect\n", toStr(ordScene++), name);
        System.out.printf("%g;%g;%g;%g\n", x, y, w, h);
        System.out.flush();
        return ordScene;
    }
    public int drawOval(String name, double x, double y, double w, double h){
        System.out.printf("write.scene.%s."+ID+".%s.drawOval\n", toStr(ordScene++), name);
        System.out.printf("%g;%g;%g;%g\n", x, y, w, h);
        System.out.flush();
        return ordScene;
    }
    public int fillOval(String name, double x, double y, double w, double h){
        System.out.printf("write.scene.%s."+ID+".%s.fillOval\n", toStr(ordScene++), name);
        System.out.printf("%g;%g;%g;%g\n", x, y, w, h);
        System.out.flush();
        return ordScene;
    }
    public int drawString(String name, String str, double x, double y){
        //TODO
        //if "=" in string:
        //    raise NameError("the string '"+string+"' contains forbid char '='");
        System.out.printf("write.scene.%s."+ID+".%s.drawString\n", toStr(ordScene++), name);
        System.out.printf("%s;%g;%g\n", str, x, y);
        System.out.flush();
        return ordScene;
    }
    public int drawStringExpr(String name, String str, String x, String y) throws Exception{
        validate(name, str, x, y);
        System.out.printf("write.scene.%s."+ID+".%s.drawString\n", toStr(ordScene++), name);
        System.out.printf("%s;%s;%s\n", str, x, y);
        System.out.flush();
        return ordScene;
    }
    public int drawImage(String name, String path, double x, double y, double w, double h){
        System.out.printf("write.scene.%s."+ID+".%s.drawImage\n", toStr(ordScene++), name);
        System.out.printf("%s;%g;%g;%g;%g\n", path, x, y, w, h);
        System.out.flush();
        return ordScene;
    }
    public int drawArc(String name, double x, double y, double w, double h, int startAngle, int arcAngle){
        System.out.printf("write.scene.%s."+ID+".%s.drawArc\n", toStr(ordScene++), name);
        System.out.printf("%g;%g;%g;%g;%d;%d\n", x, y, w, h, startAngle, arcAngle);
        System.out.flush();
        return ordScene;
    }
    public int fillArc(String name, double x, double y, double w, double h, int startAngle, int arcAngle){
        System.out.printf("write.scene.%s."+ID+".%s.fillArc\n", toStr(ordScene++), name);
        System.out.printf("%g;%g;%g;%g;%d;%d\n", x, y, w, h, startAngle, arcAngle);
        System.out.flush();
        return ordScene;
    }
    public int drawLine(String name, double x1, double y1, double x2, double y2){
        System.out.printf("write.scene.%s."+ID+".%s.drawLine\n", toStr(ordScene++), name);
        System.out.printf("%g;%g;%g;%g\n", x1, y1, x2, y2);
        System.out.flush();
        return ordScene;
    }

    
    public int drawPolygon(String name, double x[], double y[], int n_points){
        System.out.printf("write.scene.%s."+ID+".%s.drawPolygon\n", toStr(ordScene++), name);
        int i;
        for(i=0; i<n_points; i++){
            System.out.printf("%g;%g;", x[i], y[i]);
        }
        System.out.printf("%d\n", n_points);
        System.out.flush();
        return ordScene;
    }
    public int fillPolygon(String name, double x[], double y[], int n_points){
        System.out.printf("write.scene.%s."+ID+".%s.fillPolygon\n", toStr(ordScene++), name);
        int i;
        for(i=0; i<n_points; i++){
            System.out.printf("%g;%g;", x[i], y[i]);
        }
        System.out.printf("%d\n", n_points);
        System.out.flush();
        return ordScene;
    }
    private double [][] points(Point2D... points){
        double x[] = new double[points.length];
        double y[] = new double[points.length];
        for(int i=0; i<points.length; i++){
            x[i] = points[i].getX();
            y[i] = points[i].getY();
        }
        return new double[][]{x, y};
    }
    public int drawPolygon(String name, Point2D... points){
        double p[][] = points(points);
        return drawPolygon(name, p[0], p[1], points.length);
    }
    public int fillPolygon(String name, Point2D... points){
        double p[][] = points(points);
        return fillPolygon(name, p[0], p[1], points.length);
    }
    
    /**
    paint a rectangle with center on (x,y) coordenate and with radiun (rx, ry) them
    fill with (f) color and draw the border with (d) color.
    */
    public void paintRect(String name, double x, double y, double rx, double ry, Color fill, Color draw){
        setColor(name, fill);
        fillRect(name, x-rx, y-ry, 2*rx, 2*ry);
        setColor(name, draw);
        drawRect(name, x-rx, y-ry, 2*rx, 2*ry);
    }
    /**
    paint a oval with center on (x,y) coordenate and with radiun (rx, ry) them
    fill with (f) color and draw the border with (d) color.
    */
    public void paintOval(String name, double x, double y, double rx, double ry, Color fill, Color draw){
        setColor(name, fill);
        fillOval(name, x-rx, y-ry, 2*rx, 2*ry);
        setColor(name, draw);
        drawOval(name, x-rx, y-ry, 2*rx, 2*ry);
    }
    /**
    paint a poligon them fill with (f) color and draw the border with (d) color.
    */
    public void paintPolygon(String name, double x[], double y[], int n_points, Color fill, Color draw){
        setColor(name, fill);
        fillPolygon(name, x, y, n_points);
        setColor(name, draw);
        drawPolygon(name, x, y, n_points);
    }
    
    
    
    private void validate(String... values) throws Exception{
        for(String v : values){
            if(v.contains("=") || v.contains(";") || v.contains("\n")){
                throw new Exception("the string '"+v+"' contains forbid chars ['=' | ';' | '\\n']");
            }
        }
    }
}
