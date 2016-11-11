/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RHC;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.awt.geom.Point2D;
import lp.Hyperplane;

/**
 *
 * @author marci
 */
public class Model {
    public final IloCplex cplex;
    public final int D;
    public final int N;
    
    protected final IloNumVar u[][];
    protected final IloNumVar x[][];
    protected final IloNumVar p;
    
    public void start_conditions(double ...state) throws IloException{
        // Initial conditions: x(0) = start
        for(int d=0; d<state.length; d++){
            x[0][d].setLB(state[d]);
            x[0][d].setUB(state[d]);
        }
    }

    public Model(int D, double time, int N, double Umax, double end[], Point2D... points) throws IloException{
        this.D = D;
        this.N = N;
    // Create the enviroment
        cplex = new IloCplex();

        // Define the control variables
        u = new IloNumVar[N-1][D];
        for(int n=0; n<N-1; n++){
            for(int d=0; d<D; d++){
                u[n][d] = cplex.numVar(-Umax, +Umax, "u("+n+","+d+")");
            }
        }
        
        // Define the state variables
        x = new IloNumVar[N][2*D];
        for(int n=0; n<N; n++){
            for(int d=0; d<2*D; d++){
                x[n][d] = cplex.numVar(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, "x("+n+","+d+")");
            }
        }
        
        p = cplex.numVar(0, Double.POSITIVE_INFINITY, "penality");
        
        IloNumExpr obj = null;
        for(int n=0; n<N-1; n++){
            for(int d=0; d<D; d++){
                if(obj==null){
                    obj = cplex.prod(u[n][d], u[n][d]);
                }else{
                    obj = cplex.sum(obj, cplex.prod(u[n][d], u[n][d]));
                }
            }
        }
        obj = cplex.sum(obj, cplex.prod(1000, p));
        
        
        IloNumVar errorD = cplex.numVar(0, Double.POSITIVE_INFINITY, "distance");
        for(int i=0; i<32; i++){
            IloNumExpr dx[] = new IloNumExpr[2];
            for(int j=0; j<2; j++){
                dx[j] = cplex.sum(x[x.length-1][j], -end[j]);
            }
            double ax = Math.cos(2*Math.PI*i/32.0);
            double ay = Math.sin(2*Math.PI*i/32.0);
            
            cplex.addGe(errorD, cplex.sum(cplex.prod(ax,dx[0]), cplex.prod(ay, dx[1])) );
        }
        
        obj = cplex.sum(obj, cplex.prod(5, errorD));
        
//        if(end.length==x[0].length){
//            IloNumVar errorV = cplex.numVar(0, Double.POSITIVE_INFINITY, "velocity");
//            for(int i=0; i<32; i++){
//                IloNumExpr dv[] = new IloNumExpr[2];
//                for(int j=0; j<2; j++){
//                    dv[j] = cplex.sum(x[x.length-1][D+j], -end[D+j]);
//                }
//                double ax = Math.cos(2*Math.PI*i/32.0);
//                double ay = Math.sin(2*Math.PI*i/32.0);
//
//                cplex.addGe(errorV, cplex.sum(cplex.prod(ax,dv[0]), cplex.prod(ay, dv[1])) );
//            }
//            obj = cplex.sum(obj, cplex.prod(10, errorV));
//        }
        
        cplex.addMinimize(obj);
        
        
        // Dynamic equations: x(n+1) = A x(n) + B u(n)
        final double dt = time/N;
        final double A[][] = A(D, dt);
        final double B[][] = B(D, dt);
        for(int n=0; n<N-1; n++){
            for(int d=0; d<2*D; d++){
                IloNumExpr expr = null;
                //A x(n)
                for(int j=0; j<2*D; j++){
                    if(expr==null){
                        expr = cplex.prod(A[d][j], x[n][j]);
                    }else{
                        expr = cplex.sum(expr, cplex.prod(A[d][j], x[n][j]));
                    }
                }
                //B u(n)
                for(int j=0; j<D; j++){
                    expr = cplex.sum(expr, cplex.prod(B[d][j], u[n][j]));
                }
                cplex.addEq(x[n+1][d], expr, "dy("+n+","+d+")");
            }
        }
        //spatial restrictions: H x(n) <= b
        Hyperplane h[] = Hyperplane.hyperplansFrom(D>2, points);
        for(int n=0; n<N; n++){
            for(int i=0; i<h.length; i++){
                //h(i) x(n) <= b
                IloNumExpr expr = h[i].scalProd(cplex, x[n]);
                cplex.addLe(expr, cplex.sum(h[i].b, p), "sr("+n+","+i+")");
            }
        }
        
        //cplex.exportModel("./model.lp");
    }
 
    public double [][] states() throws IloException{
        final double X[][] = new double[x.length][];
        for(int n=0; n<x.length; n++){
            X[n] = cplex.getValues(x[n]);
        }
        return X;
    }
    public double [][] controls() throws IloException{
        final double U[][] = new double[u.length][];
        for(int n=0; n<u.length; n++){
            U[n] = cplex.getValues(u[n]);
        }
        return U;
    }
    
    
    /**
     * Similar to:
     * {1      ,0      ,dt     ,0},
     * {0      ,1      ,0      ,dt},
     * {0      ,0      ,1      ,0},
     * {0      ,0      ,0      ,1},
     * @param D
     * @param dt
     * @return 
     */
    private final double [][] A(int D, double dt){
        double A[][] = new double[2*D][2*D];
        for(int i=0; i<2*D; i++){
            A[i][i] = 1;
        }
        for(int i=0; i<D; i++){
            A[i][i+D] = dt;
        }
        return A;
    }
    /**
     * Similar to:
     * {dt*dt/2    ,0},
     * {0          ,dt*dt/2},
     * {dt         ,0},
     * {0          ,dt},
     * @param D
     * @param dt
     * @return 
     */
    private final double [][] B(int D, double dt){
        double B[][] = new double[2*D][2*D];
        for(int i=0; i<D; i++){
            B[i][i] = dt*dt/2;
        }
        for(int i=0; i<D; i++){
            B[i+D][i] = dt;
        }
        return B;
    }
    
}
