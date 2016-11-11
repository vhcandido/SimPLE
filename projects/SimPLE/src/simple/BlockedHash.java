/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author marci
 */
public class BlockedHash<V> {
    private static final boolean PRINT = false;
    
    private final ConcurrentHashMap<String, V> hash = new ConcurrentHashMap<>();
    private final LinkedList<String> ids = new LinkedList<>();
    private final ScriptEngineManager mgr = new ScriptEngineManager();
    private final ScriptEngine engine = mgr.getEngineByName("JavaScript");
    
    
    private synchronized void waitForUnloked() throws InterruptedException{
        while(isLocked()){
            wait();
        }
    }
    
    public synchronized V put(String key, V value){
        return hash.put(key, value);
    }
    public synchronized V get(String key) throws InterruptedException{
        waitForUnloked();
        return hash.get(key);
    }
    
    private String[] findExpr(String args) throws Exception{
        //find by patherns: $(expr)
        LinkedList<String> list = new LinkedList<>();
        while(args.contains("$") && list.size()<=1000){
            if(PRINT)System.out.println("$args = "+args);
            args = args.substring(args.indexOf("$")+2);
            int n=1;
            int i=0;
            while(n>0 && i<args.length()){
                if(args.charAt(i)==')'){
                    n--;
                }else if(args.charAt(i)=='('){
                    n++;
                }
                i++;
            }
            
            if(i<=args.length()){
                String expr = args.substring(0, i-1);
                if(PRINT)System.out.println("expr = "+expr);
                args = args.substring(expr.length()+1);
                if(!list.contains(expr)){
                    list.add(expr);
                }
            }else{
                throw new Exception("check '(' and ')' in the expresion:");
            }
        }
        return list.toArray(new String[list.size()]);
    }
    private String[] findKeys(String args){
        //find by patherns: &(key)
        LinkedList<String> list = new LinkedList<>();
        while(args.contains("&") && list.size()<=1000){
            if(PRINT)System.out.println("arg = "+args);
            args = args.substring(args.indexOf("&")+2);
            String key = args.substring(0, args.indexOf(")"));
            args = args.substring(args.indexOf(")")+1);
            if(PRINT)System.out.println("key = "+key);
            if(!list.contains(key)){
                list.add(key);
            }
        }
        return list.toArray(new String[list.size()]);
    }
    private synchronized String solveKeys(String arg){
        if(arg.contains("&")){
            if(PRINT)System.out.println("--------------------------");
            if(PRINT)System.out.println("start SolveKeys = "+arg);
            String keys[] = findKeys(arg);
            for(String key : keys){
                V value = hash.get(key);
                if(value==null){
                    arg = arg.replaceAll(key, "null");
                }else{
                    arg = arg.replaceAll("[&][(]"+key+"[)]", value.toString());
                }
                if(PRINT)System.out.println(key + " = "+value);
            }
            if(PRINT)System.out.println("end SolveKeys = "+arg);
            if(PRINT)System.out.println("--------------------------");
            return arg;
        }else{
            return arg;
        }
    }
    private synchronized String solveExpr(String expr){
        expr = solveKeys(expr);
        Object value;
        try {
            value = engine.eval(expr);   //try to solve the expression 
        } catch (ScriptException ex) {
            value = expr;                //in this case the a numerical expression was not solved
        }
        return value.toString();
    }
    private synchronized String solveAll(String expr) throws Exception{
        expr = solveKeys(expr);
        if(expr.contains("$")){
            if(PRINT)System.out.println("==========================");
            if(PRINT)System.out.println("start SolveExpr = "+expr);
            String sub_expr[] = findExpr(expr);
            for(String sub : sub_expr){
                if(PRINT)System.out.println("sub = "+sub);
                String value = solveExpr(sub);
                if(PRINT)System.out.println(sub + " = "+value);
                //expr = expr.replaceAll("[$][(]"+sub+"[)]", value);
                String temp = expr;
                expr = expr.replace("$("+sub+")", value);
                while(!expr.equals(temp)){
                    temp = expr;
                    expr = expr.replace("$("+sub+")", value);
                }
                if(PRINT)System.out.println("expr = "+expr);
            }
            if(PRINT)System.out.println("end SolveExpr = "+expr);
            if(PRINT)System.out.println("==========================");
            return expr;
        }else{
            return expr;
        }
    }
    private synchronized String solve(String expr){
        try{
            return solveAll(expr);
        }catch(Throwable ex){
            System.err.println("expr = ["+expr+"]");
            ex.printStackTrace(System.err);
            return expr;
        }
    }
    public synchronized LinkedList<BlockedEntry> entrySet() throws InterruptedException{
        waitForUnloked();
        LinkedList<BlockedEntry> list = new LinkedList<>();
        for(Map.Entry<String, V> e : hash.entrySet()){
            list.addLast(new BlockedEntry(e.getKey(), solve(e.getValue().toString())));
        }
        return list;
    }
    public synchronized void lock(String id) throws Exception{
        if(ids.contains(id)){
            throw new Exception("lock(id), id = '"+id+"' alware is in ids = "+ids);
        }
        ids.add(id);
    }
    public synchronized void unlock(String id) throws Exception{
        if(!ids.contains(id)){
            throw new Exception("unlock(id), id = '"+id+"' is not in ids = "+ids);
        }
        ids.remove(id);
        if(ids.isEmpty()){
            notify();
        }
    }
    public synchronized boolean isLocked(){
        return !ids.isEmpty();
    }
}
