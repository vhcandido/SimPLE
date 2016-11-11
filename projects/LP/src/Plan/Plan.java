/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Plan;

import Linear.Model;
import SimPLE.Module;
import SimPLE.Scene;
import ilog.concert.IloException;
import java.awt.Color;
import java.awt.geom.Point2D;

/**
 *
 * @author marci
 */
public class Plan extends Module{
    private final double Mission_Time = 10.0;   //6.0
    private final int Mission_Steps = 10;
    
    
    
    private final Point2D[] points = new Point2D[]{
        new Point2D.Double(-2, -2),
        new Point2D.Double(-2, 6),
        new Point2D.Double(8, 6),
        new Point2D.Double(8, -2),
    };
    
    private final Scene scene;
    //private final boolean recalc;
    public Plan(String ID) throws IloException {
        super(ID);
        this.scene = new Scene(ID);
        //this.recalc = recalc;
    }
    
    private void delaySolve() throws InterruptedException{
        //sleep_ms(1000);
    }

    @Override
    public void start() throws Throwable {
        writeDbl("ax", 0.0);
        writeDbl("ay", 0.0);
        
        scene.beginScene(500);
            scene.setColor("safe", Color.GREEN);
            scene.drawPolygon("safe", points);
            
            for(int n=0; n<goals.length; n++){
                scene.setColor("goals", Color.RED);
                scene.drawLine("goals", goals[n][0], goals[n][1], goals[n][0]+goals[n][2], goals[n][1]+goals[n][3]);
                scene.paintOval("goals", goals[n][0], goals[n][1], 0.05, 0.05, Color.ORANGE, Color.BLACK);
            }
        scene.endScene();
    }
    
    private final double goals[][] = new double[][]{
        {0, 0, 0, 0},
        {6, 0, 0, 1},
        {6, 4, -1, 0},
        {3, 2, 0, 0},
        {0, 4, 0, 1},
    };
    
    private int solved = 0;
    private int infeasibles = 0;
    
    @Override
    public void loop() throws Throwable {
        for(int n=0; n<goals.length; n++){
            loopLP_recalc((n+1)%goals.length);
//            if(recalc){
//                loopLP_recalc((n+1)%goals.length);
//            }else{
//                loopLP((n+1)%goals.length);
//            }
            

            //loopLP_recalc((n+1)%goals.length);
        }
    }
    private void loopLP_recalc(int n) throws IloException, InterruptedException{
        //int k = (n+1)%goals.length; //next point
        
        double px = readDbl("robot.px");
        double py = readDbl("robot.py");
//        double px = readDbl("gps.px");
//        double py = readDbl("gps.py");
        double vx = readDbl("robot.vx");
        double vy = readDbl("robot.vy");
        
        Model model = new Model(2, Mission_Time, Mission_Steps, 1.0, points);
        model.start_conditions(px, py, vx, vy);
        model.end_conditions(goals[n]);
        
        double target[][] = null;
        if(model.cplex.solve()){
            delaySolve();
            target = model.states();
            scene.beginScene(700*(n+1));
                scene.setColor("plan", Color.BLACK);
                for(int t=0; t<target.length-1; t++){
                    scene.drawLine("plan", target[t][0], target[t][1], target[t+1][0], target[t+1][1]);
                }
                for(int t=0; t<target.length; t++){
                    scene.fillOval("plan", target[t][0]-0.02, target[t][1]-0.02, 0.04, 0.04);
                }
            scene.endScene();
        }
        model.cplex.end();
        
        double t0 = time_sec();
        double remain_time = Mission_Time;
        while(remain_time>Mission_Time/Mission_Steps){
            px = readDbl("robot.px");
            py = readDbl("robot.py");
            vx = readDbl("robot.vx");
            vy = readDbl("robot.vy");
//
//            px = readDbl("gps.px");
//            py = readDbl("gps.py");
//            vx = readDbl("robot.vx");
//            vy = readDbl("robot.vy");
            
            //final double control_time = Mission_Time/Mission_Steps;
            final double control_time = 2.5;
            
            ModelControl model2 = new ModelControl(2, control_time, Mission_Steps, 1.0, target, Mission_Time/Mission_Steps, Mission_Time-remain_time);
            model2.start_conditions(px, py, vx, vy);
            
            if(model2.cplex.solve()){
                delaySolve();
                double x[][] = model2.states();
                double u[][] = model2.controls();
                
                
                solved++;
                writeInt("solved", solved);
                writeDbl("ax", u[0][0]);
                writeDbl("ay", u[0][1]);
                
                double cx = readDbl("robot.px");
                double cy = readDbl("robot.py");

                scene.beginScene(600);
                    scene.paintRect("cur", cx, cy, 0.04, 0.04, Color.ORANGE, Color.BLACK);
                    for(int t=0; t<x.length; t++){
                        scene.paintOval("states", x[t][0], x[t][1], 0.03, 0.03, Color.BLUE, Color.BLACK);
                        //scene.drawString("steps", "["+t+"]", x[t][0]-0.1, x[t][1]-0.25);
                    }
                scene.endScene();
            }else{
                infeasibles++;
                writeInt("infeasibles", infeasibles);
                writeDbl("ax", 0.0);
                writeDbl("ay", 0.0);
            }
            model2.cplex.end();
            sleep_ms(100);
            remain_time = Mission_Time - (time_sec()-t0);
        }
    }
    
//    private void loopLP(int n) throws IloException, InterruptedException{
//        //int k = (n+1)%goals.length; //next point
//        double px = readDbl("robot.px");
//        double py = readDbl("robot.py");
//        double vx = readDbl("robot.vx");
//        double vy = readDbl("robot.vy");
//
//        ModelControl model = new ModelControl(2, Mission_Time, Mission_Steps, 1.0, points);
//        model.start_conditions(px, py, vx, vy);
//        model.end_conditions(goals[n]);
//
//        if(model.cplex.solve()){
//            delaySolve();
//            solved++;
//            writeInt("solved", solved);
//
//            double cx = readDbl("robot.px");
//            double cy = readDbl("robot.py");
//
//            double x[][] = model.states();
//            double u[][] = model.controls();
//
//            scene.beginScene(700*(n+1));
//                scene.paintRect("cur", cx, cy, 0.04, 0.04, Color.ORANGE, Color.BLACK);
//                scene.setColor("plan", Color.BLACK);
//                for(int t=0; t<x.length-1; t++){
//                    scene.drawLine("plan", x[t][0], x[t][1], x[t+1][0], x[t+1][1]);
//                }
//                for(int t=0; t<x.length; t++){
//                    scene.fillOval("plan", x[t][0]-0.02, x[t][1]-0.02, 0.04, 0.04);
//                }
//            scene.endScene();
//
//            
//            double t0 = time_sec();
//            while(time_sec()-t0<Mission_Time){
//                int t = (int)(((time_sec()-t0)*u.length)/Mission_Time);
//                scene.beginScene(600);
//                    scene.setColor("control", Color.RED);
//                    scene.setStroke("control", 3.0);
//                    scene.drawLine("control", x[t][0], x[t][1], x[t+1][0], x[t+1][1]);
//                    scene.setStroke("control", 1.0);
//                    scene.paintRect("control", x[t][0], x[t][1], 0.05, 0.05, Color.RED, Color.BLACK);
//                    scene.drawString("control", "["+t+"]", x[t][0]-0.1, x[t][1]-0.25);
//                scene.endScene();
//                //t = Math.min(t, Mission_Steps-1);
//                writeDbl("ax", u[t][0]);
//                writeDbl("ay", u[t][1]);
//                sleep_ms(100);
//            }
//        }else{
//            infeasibles++;
//            writeInt("infeasibles", infeasibles);
//            writeDbl("ax", 0.0);
//            writeDbl("ay", 0.0);
//            sleep_ms(100);
//        }
//        model.cplex.end();
//    }
}
