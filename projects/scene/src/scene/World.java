/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import objects.SceneObject;
import windows.Graphics2DReal;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import objects.EvaluateExpression;
import objects.drawArc;
import objects.drawImage;
import objects.drawLine;
import objects.drawOval;
import objects.drawPolygon;
import objects.drawRect;
import objects.drawString;
import objects.fillArc;
import objects.fillOval;
import objects.fillPolygon;
import objects.fillRect;
import objects.setCamera;
import objects.setColor;
import objects.setRotate;
import objects.setStroke;
import objects.setTraslate;

/**
 *
 * @author marci
 */
public class World{
    public final int size = 100;
    public setCamera camera = null;
    private final ConcurrentHashMap<String, SceneObject> hash;
    
    public World() {
        this.hash = new ConcurrentHashMap<>();
    }
    public int grid(double v){
        return size*((int)((v)/size));
    }
    public int lower(double v){
        return grid(v-size+1);
    }
    public int uper(double v){
        return grid(v+size-1);
    }
//    public int convert(double v){
//        return (int)(size*v);
//    }

    
    public void update(ArrayList<Map.Entry<String, String>> list) throws Throwable {
        
        //change to a more eficient structure
        HashMap<String,String> cur = new HashMap();
        for(Map.Entry<String, String> e : list){
            cur.put(e.getKey(), e.getValue());
        }
        
        //list old ignored objects to be removed
        LinkedList<String> rem = new LinkedList<>();
        for(Map.Entry<String, SceneObject> e : hash.entrySet()){
            if(!cur.containsKey(e.getKey())){
                rem.addLast(e.getKey());
            }
        }
        
        //System.out.println("objects to be removed:");
        //System.out.println(rem);
        
        //remove object selecteds
        for(String key : rem){
            hash.remove(key);
        }
        /*
        EvaluateExpression eval = new EvaluateExpression() {
            private ScriptEngineManager mgr = new ScriptEngineManager();
            private ScriptEngine engine = mgr.getEngineByName("JavaScript");
            @Override
            public Object evaluate(String expr) throws ScriptException {
                return engine.eval(expr);
            }
            @Override
            public Double parseDouble(String expr) throws ScriptException {
                try{
                    return Double.parseDouble(expr);
                }catch(NumberFormatException ex){
                    return (Double)evaluate(expr);
                }
            }
            @Override
            public Integer parseInteger(String expr) throws ScriptException {
                try{
                    return Integer.parseInt(expr);
                }catch(NumberFormatException ex){
                    return (Integer)evaluate(expr);
                }
            }
        };
        */
        
        for(Map.Entry<String, String> e : list){
            SceneObject obj = hash.get(e.getKey());
            if(obj==null){
                //add new objects
                obj = create(e.getKey());
                //System.out.println("try create key = "+e.getKey()+" with result = "+obj);
                if(obj!=null){
                    obj.update(e.getValue());
                    hash.put(e.getKey(), obj);
                }
            }else{
                //update all existing objects
                obj.update(e.getValue());
            }
        }
        
        
    }
    
    public SceneObject create(String key){
        if(key.endsWith(".translate")){
            return new setTraslate(key);
        }else if(key.endsWith(".rotate")){
            return new setRotate(key);
        }else if(key.endsWith(".setStroke")){
            return new setStroke(key);
        }else if(key.endsWith(".setColor")){
            return new setColor(key);
        }else if(key.endsWith(".drawRect")){
            return new drawRect(key);
        }else if(key.endsWith(".fillRect")){
            return new fillRect(key);
        }else if(key.endsWith(".drawOval")){
            return new drawOval(key);
        }else if(key.endsWith(".fillOval")){
            return new fillOval(key);
        }else if(key.endsWith(".drawArc")){
            return new drawArc(key);
        }else if(key.endsWith(".fillArc")){
            return new fillArc(key);
        }else if(key.endsWith(".drawLine")){
            return new drawLine(key);
        }else if(key.endsWith(".drawPolygon")){
            return new drawPolygon(key);
        }else if(key.endsWith(".fillPolygon")){
            return new fillPolygon(key);
        }else if(key.endsWith(".drawString")){
            return new drawString(key);
        }else if(key.endsWith(".drawImage")){
            return new drawImage(key);
        }else if(key.endsWith(".setCamera")){
            this.camera = new setCamera(key);
            return camera;
        }
        
//        if(key.endsWith(".paintRect")){
//            return new SceneRect(key);
//        }
        return null;
    }
//    public void put(String key, String value) {
//        //"write.scene.robot.body.paintRect\n"
//        if(key.endsWith(".drawRect")){
//            SceneObject obj = hash.get(key);
//            if(obj==null){
//                obj = new SceneRect(key);
//                obj.update(value);
//                hash.put(key, obj);
//            }else{
//                obj.update(value);
//            }
//        }else{
//            System.err.println("key '"+key+"' not valid");
//        }
//    }
//    public void clear(){
//        hash.clear();
//    }
    public void print(){
        System.out.println("--------------------------------[SCENE]------------------------------");
        for(Map.Entry<String,SceneObject> e : hash.entrySet()){
            System.out.println(e);
        }
        System.out.println("---------------------------------------------------------------------");
    }
//    public void update() {
//        for(Map.Entry<String,SceneObject> e : hash.entrySet()){
//            String key = e.getKey();
//            if(key.startsWith("scene.")){
//                if(key.substring(6).startsWith("drawRect.")){
//                    
//                }
//            }
//        }
//    }
    public static final Comparator compartor = new Comparator<Map.Entry<String,SceneObject>>() {
        @Override
        public int compare(Map.Entry<String, SceneObject> o1, Map.Entry<String, SceneObject> o2) {
            return o1.getKey().compareTo(o2.getKey());
        }
    };
    
    public void paint(Graphics2D g2) {
        Graphics2DReal gr = new Graphics2DReal(g2, size);
        
        Map.Entry<String,SceneObject> array[] = hash.entrySet().toArray(new Map.Entry[hash.entrySet().size()]);
        Arrays.sort(array, compartor);
        
        for(Map.Entry<String,SceneObject> e : array){
            e.getValue().paint(gr); 
        }
    }

    

    

    
}
