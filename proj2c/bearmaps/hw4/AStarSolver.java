package bearmaps.hw4;
import bearmaps.proj2ab.ArrayHeapMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex>{
    private SolverOutcome outcome;
    private LinkedList<Vertex> solution;
    private double solutionWeight;
    private double timeSpent;
    private int numexplored;
    private Map<Vertex, Double> distTo;
    private Map<Vertex, Vertex> edgeTo;
    private ArrayHeapMinPQ<Vertex> queue;
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout){
        Stopwatch sw = new Stopwatch();
        queue = new ArrayHeapMinPQ<>();
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        solution = new LinkedList<>();
        distTo.put(start, 0.0);
        solutionWeight = 0;
        queue.add(start, input.estimatedDistanceToGoal(start, end));
        numexplored = -1;
        while(queue.size() > 0){
            Vertex smallest = queue.removeSmallest();
            numexplored+=1;
            if(smallest.equals(end)){
                timeSpent = sw.elapsedTime();
                outcome = SolverOutcome.SOLVED;
                Vertex pos = end;
                while(!pos.equals(start)){
                    solution.addFirst(pos);
                    pos = edgeTo.get(pos);
                }
                solutionWeight = distTo.get(end);
                solution.addFirst(start);
                return;
            }
            if(sw.elapsedTime() > timeout){
                outcome = SolverOutcome.TIMEOUT;
                timeSpent = sw.elapsedTime();
                return;
            }
            for(WeightedEdge<Vertex> x: input.neighbors(smallest)){
                relax(x, input, end, smallest);
            }
        }
        outcome = SolverOutcome.UNSOLVABLE;
        timeSpent = sw.elapsedTime();
    }
    private void relax(WeightedEdge<Vertex> e, AStarGraph<Vertex> yes, Vertex end, Vertex x){
        Vertex to = e.to();
        if(!distTo.containsKey(to) ||distTo.get(x)+ e.weight() <distTo.get(to)){
            distTo.put(to, distTo.get(x) + e.weight());
            edgeTo.put(to, x);
            if (queue.contains(to)) {
                queue.changePriority(to, distTo.get(to) + yes.estimatedDistanceToGoal(to, end));
            }
            else {
                queue.add(to, distTo.get(to) + yes.estimatedDistanceToGoal(to, end));
            }
        }
    }
    public SolverOutcome outcome(){
        return outcome;
    }
    public List<Vertex> solution(){
        return solution;
    }
    public double solutionWeight(){
        return solutionWeight;
    }
    public int numStatesExplored(){
        return numexplored;
    }
    public double explorationTime(){
        return timeSpent;
    }

}
