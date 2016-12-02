/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MPC;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.awt.geom.Point2D;
import Main.Hyperplane;
import java.util.List;

/**
 *
 * @author marci
 */
public class ModelLP {
    public final IloCplex cplex;
    public final int D;
    public final int N;
    
    protected final IloNumVar u[][];
    protected final IloNumVar x[][];
    protected final IloNumVar p;
    protected IloNumVar z[][];
    
    public void start_conditions(double ...state) throws IloException{
        // Initial conditions: x(0) = start
        for(int d=0; d<state.length; d++){
            x[0][d].setLB(state[d]);
            x[0][d].setUB(state[d]);
        }
    }
    public void end_conditions(double ...state) throws IloException{
        // End conditions: x(N-1) = end
        for(int d=0; d<state.length; d++){
            x[x.length-1][d].setLB(state[d]);
            x[x.length-1][d].setUB(state[d]);
        }
    }

    
    public ModelLP(int D, double time, int N, double Umax, List<Point2D[]> obstacles, Point2D... points) throws IloException{
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
	
	// obstacles constraints
	for(int r=0; r<obstacles.size(); ++r) {
	    Hyperplane obstacle[] = Hyperplane.hyperplansFrom(D>2, obstacles.get(r));
	    z = new IloNumVar[N][obstacle.length];
	    for(int n=0; n<N; n++){
		IloNumExpr z_sum = null;
		for(int i=0; i<obstacle.length; ++i) {
		    //h(i) x(n) >= b - M(1-z(n,r))
		    z[n][i] = cplex.boolVar("z("+n+","+r+","+i+")");
		    if(z_sum == null) {
			z_sum = z[n][i];
		    } else {
			z_sum = cplex.sum(z_sum, z[n][i]);
		    }
		    IloNumExpr expr = obstacle[i].scalProd(cplex, x[n]);
		    IloNumExpr m = cplex.prod(10000, cplex.sum(-1, z[n][i]));
		    cplex.addGe(expr, cplex.sum(obstacle[i].b, m), "obc("+n+","+r+","+i+")");
		    System.out.println(obstacle[i].b);
		}
		cplex.addGe(z_sum, 1, "sum("+n+","+r+")");
	    }
        }
        cplex.exportModel("./model.lp");
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
