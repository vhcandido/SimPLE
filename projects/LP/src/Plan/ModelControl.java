/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Plan;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/**
 *
 * @author marci
 */
public class ModelControl {
    public final IloCplex cplex;
    public final int D;
    public final int N;
    
    protected final IloNumVar u[][];
    protected final IloNumVar x[][];
    
    public void start_conditions(double ...state) throws IloException{
        // Initial conditions: x(0) = start
        for(int d=0; d<state.length; d++){
            x[0][d].setLB(state[d]);
            x[0][d].setUB(state[d]);
        }
    }

    
    private double[] interpolation(double target[][], double target_dt, double time_elapsed) throws IloException{
        //time_elapsed += dt*n;
        
        int target_n = (int)(time_elapsed/target_dt);
        double lambda = time_elapsed/target_dt - target_n;
        if(target_n+1>=target.length){
            return null;
        }else{
            double state[] = new double[2*D];
            for(int d=0; d<2*D; d++){
                state[d] = target[target_n][d]*(1-lambda) + target[target_n+1][d]*lambda;
            }
            return state;
        }
    }
    
    public ModelControl(int D, double time, int N, double Umax, double target[][], double target_dt, double time_elapsed) throws IloException{
        this.D = D;
        this.N = N;
        final double dt = time/(N-1);
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
        
        IloNumExpr obj1 = null;
        for(int n=0; n<N; n++){
            double state[] = interpolation(target, target_dt, time_elapsed + n*dt);
            if(state!=null){
                IloNumExpr dx[] = new IloNumExpr[2*D];
                for(int d=0; d<2*D; d++){
                    dx[d] = cplex.sum(x[n][d], -state[d]);
                    if(obj1==null){
                        obj1 = cplex.prod(dx[d], dx[d]);
                    }else{
                        obj1 = cplex.sum(obj1, cplex.prod(dx[d], dx[d]));
                    }
                }
            }
        }
        IloNumExpr obj2 = null;
        for(int n=0; n<N-1; n++){
            for(int d=0; d<D; d++){
                if(obj2==null){
                    obj2 = cplex.prod(u[n][d], u[n][d]);
                }else{
                    obj2 = cplex.sum(obj2, cplex.prod(u[n][d], u[n][d]));
                }
            }
        }
        cplex.addMinimize(cplex.sum(cplex.prod(10, obj1), cplex.prod(1, obj2)));
        
        // Dynamic equations: x(n+1) = A x(n) + B u(n)
        
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
