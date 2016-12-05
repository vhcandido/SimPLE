package tsp;

import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.util.Arrays;

class TSP {

    static void solve(double[][] goals){
	int n = goals.length;
	double[][] c = new double[n][n];
	for (int i = 0; i < n-1; i++){
	    c[i][i] = 0;
            for(int j = i+1; j < n; j++){
                c[i][j] = distance(goals[i][0], goals[i][1], goals[j][0], goals[j][1]);
		c[j][i] = c[i][j];
            }
        }
        // model
        try {
            IloCplex cplex = new IloCplex();

            // variables
            IloNumVar[][] x = new IloNumVar[n][];
            for (int i = 0; i < n; i++){
                x[i] = cplex.boolVarArray(n);
            }
            IloNumVar[] u = cplex.numVarArray(n, 0, Double.MAX_VALUE);

            // objective
            IloLinearNumExpr obj = cplex.linearNumExpr();
            for(int i = 0; i < n; i++){
                for (int j = 0; j < n; j++) {
                    if(i != j){
                        obj.addTerm(c[i][j], x[i][j]);
                    }
                }
            }
            cplex.addMinimize(obj);

            // constraints
            for (int j = 0; j < n; j++) {
                IloLinearNumExpr expr = cplex.linearNumExpr();
                for (int i = 0; i < n; i++) {
                    if(i != j){
                        expr.addTerm(1.0, x[i][j]);
                    }
                }
                cplex.addEq(expr, 1.0);
            }
            for (int i = 0; i < n; i++) {
                IloLinearNumExpr expr = cplex.linearNumExpr();
                for (int j = 0; j < n; j++) {
                    if(i != j){
                        expr.addTerm(1.0, x[i][j]);
                    }
                }
                cplex.addEq(expr, 1.0);
            }

            for (int i = 1; i < n; i++) {
                for (int j = 1; j < n; j++) {
                    if(i != j){
                        IloLinearNumExpr expr = cplex.linearNumExpr();
                        expr.addTerm(1.0, u[i]);
                        expr.addTerm(-1.0, u[j]);
                        expr.addTerm(n - 1, x[i][j]);
                        cplex.addLe(expr, n - 2);
                    }
                }
            }

            // solve model
            cplex.solve();


            double[][] xResult = new double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if(i != j)
                        xResult[i][j] = cplex.getValue(x[i][j]);
                    else
                        xResult[i][j] = 0.0;
                }
            }
            for (int i = 0; i < n; i++) {
                System.out.println(Arrays.toString(xResult[i]));
            }

            String result = cplex.getStatus().toString();
            System.out.println("result: " + result);
            // end
            cplex.end();

	    double[][] ord_goals = new double[n][4];
	    ord_goals[0] = goals[0];
	    int idx = 0;
	    for(int i=0; i < goals.length-1; ++i) {
		for(int j=1; j < goals.length; ++j) {
		    if(xResult[idx][j] == 1) {
			idx = j;
			ord_goals[j] = goals[idx];
			break;
		    }
		}
	    }
	    for(double[] p: ord_goals) {
		System.out.println(Arrays.toString(p));
	    }

        } catch (IloException e) {
            e.printStackTrace();
        }
    }

    private static double distance(double xi, double yi, double xj, double yj){
	return Math.sqrt(Math.pow(xi - xj, 2) + Math.pow(yi - yj, 2));
    }
}