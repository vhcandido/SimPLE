package tsp;
import java.util.Random;

public class Main {
    public static void main(String[] args){
        Random random = new Random();
        TSP.solve(3, random);
    }
}