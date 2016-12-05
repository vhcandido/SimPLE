package tsp;

public class Main {
    public static void main(String[] args){
	    double goals[][] = new double[][]{
	    {0, 0, 0, 0},
	    {6, 0, 0, 1},
	    {6, 4, -1, 0},
	    {3, 2, 0, 0},
	    {0, 4, 0, 1},
	};
        TSP.solve(goals);
    }
}