/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.awt.geom.Point2D;


/**
 *
 * @author marcio
 */
public class Hyperplane {
    public final double a[];
    public final double b;
    public Hyperplane(double b, double ...a) {
        this.a = a;
        this.b = b;
    }
    public double scalarProd(double ...p) {
        double sum = 0;
        int length = Math.min(a.length, p.length);
        for(int n=0; n<length; n++){
            sum += a[n] * p[n];
        }
        return sum;
    }

    public IloNumExpr scalProd(IloCplex cplex, IloNumVar... x) throws IloException {
        IloNumExpr sum = null;
        int length = Math.min(a.length, x.length);
        for(int n=0; n<length; n++){
            if(sum == null){
                sum = cplex.prod(a[n], x[n]);
            }else{
                sum = cplex.sum(sum, cplex.prod(a[n], x[n]));
            }
        }
        return sum;
    }
    
    public static Hyperplane[] hyperplansFrom(boolean dim3D, Point2D points[]){
        Hyperplane[] hyperplans = new Hyperplane[points.length];
        for(int i=0; i<points.length; i++){
            int k = (i+1) % points.length;
            double x1 = points[i].getX();
            double y1 = points[i].getY();
            double x2 = points[k].getX();
            double y2 = points[k].getY();

            //x'
            double ax = -( y2 - y1 );
            //y'
            double ay = +( x2 - x1 );
            
            
            //normalize
            double norm = Math.sqrt(ax*ax+ay*ay);
            ax /= norm;
            ay /= norm;

            double b = ax * x1  + ay * y1;

            if(dim3D){
                hyperplans[i] = new Hyperplane(b, ax, ay, 0.0);
            }else{
                hyperplans[i] = new Hyperplane(b, ax, ay);
            }
            //if(PRINT)System.out.printf(Locale.ENGLISH, "%8.4f * x + %8.4f * y  = %8.4f\n", ax, ay, b);
        }
        return hyperplans;
    }
}
