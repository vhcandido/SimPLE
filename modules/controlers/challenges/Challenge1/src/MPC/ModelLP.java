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
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.math3.special.Erf.erfInv;
/**
 *
 * @author marci
 */
public class ModelLP {
    public final IloCplex cplex;
    public final int D;
    public final int N;
    private final double Delta;
    public List<double[][]> sigma;
    private final double erfInverse;
    
    protected final IloNumVar u[][];
    protected final IloNumVar x[][];
    protected final IloNumVar pen;
    protected IloNumVar z[][][];
    
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
    
    private void sigmaMatrix(double delta) {
	double[][] sigma0 = new double[][]{
	    {0.0003, 0, 0, 0},
	    {0, 0.0003, 0, 0},
	    {0, 0, 0, 0},
	    {0, 0, 0, 0},
	};
	
	double[][] A = A(this.D, delta);
	
	this.sigma = new ArrayList<>();
	this.sigma.add(sigma0);
	for(int n=1; n<N; ++n) {
	    double[][] sigmaSum = matMultiply(A, this.sigma.get(n-1));
	    double[][] transp = matTranspose(A);
	    sigmaSum = matMultiply(sigmaSum, transp);
	    sigmaSum = matSum(sigmaSum, sigma0);
	    this.sigma.add(sigmaSum);
	}
    }
    
    private double[][] matTranspose(double[][] m) {
	double[][] ret = new double[m[0].length][m.length];
	for(int i=0; i<m.length; ++i) {
	    for(int j=0; j<m[0].length; ++j) {
		ret[j][i] = m[i][j];
	    }
	}
	return ret;
    }
    
    private double[][] matSum(double[][] m1, double[][] m2) {
	double[][] ret = new double[m1.length][m1[0].length];
	for(int i=0; i<m1.length; ++i) {
	    for(int j=0; j<m1[0].length; ++j) {
		ret[j][i] = m1[i][j] + m2[i][j];
	    }
	}
	return ret;
    }
    
    private double[][] matMultiply(double[][] m1, double[][] m2) {
	double[][] ret = new double[m1.length][m2[0].length];
	for(int i=0; i<m1.length; ++i) {
	    for(int j=0; j<m2[0].length; ++j) {
		for(int k=0; k<m1[0].length; ++k) {
		    ret[i][j] += m1[i][k] * m2[k][j];
		}
	    }
	}
	return ret;
    }
    
    private double[] zeroFill(double[] x) {
	double[] ret = new double[2*this.D];
	for(int i=0; i<ret.length; ++i) {
	    ret[i] = i<x.length ? x[i] : 0;
	}
	return ret;
    }
    
    private double c_in(int n, double[] a) {
	for(int i=0; i<a.length; ++i) { a[i] *= 2; }
	double[][] h = new double[][]{a};
	double c;
	double[][] hs = matMultiply(h, sigma.get(n));
	double[][] transp = matTranspose(h);
	c = matMultiply(hs, transp)[0][0];
	c = Math.sqrt(c) * this.erfInverse;
	return c;
    }
    
    public ModelLP(int D, double time, int N, double Delta, double Umax, List<Point2D[]> obstacles, Point2D... points) throws IloException{
        this.D = D;
        this.N = N;
	this.Delta = Delta;
	double delta = this.Delta/(N*obstacles.size());
	this.erfInverse = erfInv(1 - 2*delta);
	sigmaMatrix(delta);

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
        
        pen = cplex.numVar(0, Double.POSITIVE_INFINITY, "penality");
        
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
        obj = cplex.sum(obj, cplex.prod(1000, pen));
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
                cplex.addLe(expr, cplex.sum(h[i].b, pen), "sr("+n+","+i+")");
            }
        }
	z = new IloNumVar[obstacles.size()][N][];
	// obstacles constraints
	for(int r=0; r<obstacles.size(); ++r) {
	    Hyperplane obstacle[] = Hyperplane.hyperplansFrom(D>2, obstacles.get(r));
	    for(int n=0; n<N; n++){
		IloNumExpr z_sum = null;
		z[r][n] = new IloNumVar[obstacle.length];
		for(int i=0; i<obstacle.length; ++i) {
		    //h(i) x(n) >= b - M(1-z(n,r))
		    z[r][n][i] = cplex.boolVar("z("+r+","+n+","+i+")");
		    if(z_sum == null) {
			z_sum = z[r][n][i];
		    } else {
			z_sum = cplex.sum(z_sum, z[r][n][i]);
		    }
		    IloNumExpr expr = obstacle[i].scalProd(cplex, x[n]);
		    IloNumExpr m = cplex.prod(10000, cplex.sum(-1, z[r][n][i]));
		    double c = c_in(n, zeroFill(obstacle[i].a));
		    cplex.addGe(expr, cplex.sum(obstacle[i].b + c, m), "obc("+r+","+n+","+i+")");
		}
		cplex.addGe(z_sum, 1, "sum("+r+","+n+")");
	    }
        }

	// Corner cutting constraints
	for(int r=0; r<z.length; ++r) {
	    for(int n=1; n<z[r].length-1; n++){
		IloNumExpr p_sum = null;
		for(int i=0; i<z[r][n].length; ++i) {
		    IloNumVar p = cplex.numVar(0, Double.POSITIVE_INFINITY, "p("+r+","+n+","+i+")");

		    // p(r,n,i) >= z(r,n,i) + p(r,n-1,i) - 1
		    IloNumExpr expr;
		    expr = cplex.sum(z[r][n][i], z[r][n-1][i]);
		    expr = cplex.sum(expr, -1);
		    cplex.addGe(p, expr, "corner1("+r+","+n+","+i+")");

		    // p(r,n,i) <= z(r,n,i)
		    cplex.addLe(p, z[r][n][i], "corner2("+r+","+n+","+i+")");

		    // p(r,n,i) <= z(r,n-1,i)
		    cplex.addLe(p, z[r][n-1][i], "corner3("+r+","+n+","+i+")");

		    // sum_i p(r,n,i) >= 1 \forall r,n
		    if(p_sum == null) {
			p_sum = p;
		    } else {
			p_sum = cplex.sum(p_sum, p);
		    }
		}
		cplex.addGe(p_sum, 1, "sump("+r+","+n+")");
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
