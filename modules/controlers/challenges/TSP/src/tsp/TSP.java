package tsp;

import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.util.Arrays;
import java.util.Random;

class TSP {

    static void solve(int n, Random random){
        double[] Xpos = new double[n];
        double[] Ypos = new double[n];

        for(int i = 0; i < n; i++){
            Xpos[i] = random.nextDouble() * 100;
            Ypos[i] = random.nextDouble() * 100;
	    System.out.println(Xpos[i] + " - " + Ypos[i]);
        }
        double[][] c = new double[n][n];
        for (int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                c[i][j] = distance(Xpos[i], Ypos[i], Xpos[j], Ypos[j]);
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

        } catch (IloException e) {
            e.printStackTrace();
        }
    }

    private static double distance(double xi, double yi, double xj, double yj){
        return Math.sqrt(Math.pow(xi - xj, 2) + Math.pow(yi - yj, 2));
    }
}