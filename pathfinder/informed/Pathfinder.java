package pathfinder.informed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Maze Pathfinding algorithm that implements a basic, uninformed, breadth-first tree search.
 */
public class Pathfinder {
    //Expand each tree node
	//Add cost of future and past, including future spots that include "mud"
	//Compare the costs of multiple ways of getting to the goal
	//???
	//Profit
	
	
	//A*
	//would have to take into consideration the costs of its next possible moves and take the best one without accidentally missing goal state
	
	//ways to complete:
	//when a goal state is found, add its path and cost of that past to a ??
	//once all possible paths to goal are found, compare costs
	//return path with smallest cost
	//if no paths or cost have been added to ??, return
	
	//thoughts
	//must take key in to consideration. Can we change the goal state? Start with key as goal state, once key is found, set door as goal state, and key as initial state? 2 seperate searches? one...
	//with key as initial and door as goal, and one with initial as initial and key as goal? Best cost would be the lowest of the first search added to the lowest of the second. (not sure if this ...
	//is A* however)
	
	
	//things that need to be added:
	//an if statement to take into consideration mud tiles (maybe)
	//a way for the solve method to find multiple goal states (right now it stops when it finds the first possibility)
	//a cost tracker for every path that would add cost on, and a mud tiles extra cost could be added on to as well
	

	
	//Questions for manny:
	// does getCost take mud tiles into consideration? yes
	// thoughts on how to get to the key before the "door"?
	// how will the solve method know when it has found all possible ways to the goal state?
    
    /**
     * Given a MazeProblem, which specifies the actions and transitions available in the
     * search, returns a solution to the problem as a sequence of actions that leads from
     * the initial to a goal state.
     * 
     * @param problem A MazeProblem that specifies the maze, actions, transitions.
     * @return An ArrayList of Strings representing actions that lead from the initial to
     * the goal state, of the format: ["R", "R", "L", ...]
     */
    public static ArrayList<String> solve (MazeProblem problem) {
        boolean keyObtained = false;
        
        // Implementing BFS, so frontier is a Queue (which, in JCF, is an interface that
        // can be used atop a LinkedList implementation)
        PriorityQueue<SearchTreeNode> frontier = new PriorityQueue<SearchTreeNode>();
        
        // Add initial state to frontier
        frontier.add(new SearchTreeNode(problem.INITIAL_STATE, null, null, 0));
        
        // Continue expanding nodes as long as the frontier is not empty
        // (not strictly necessary for this assignment because a solution was
        // always assumed to be
        while (!frontier.isEmpty()) {
            // Grab the front node of the queue - this is the node we're expanding
            SearchTreeNode expanding = frontier.poll();
            
            // If it's a goal state, we're done!
            if (problem.isGoal(expanding.state) && keyObtained) {
                return getPath(expanding);
            }
            
            // Otherwise, must generate children
            Map<String, MazeState> transitions = problem.getTransitions(expanding.state);
            // For each action:MazeState pair in the transitions...
            for (Map.Entry<String, MazeState> transition : transitions.entrySet()) {
                // ...create a new STN and add that to the frontier
                frontier.add(new SearchTreeNode(transition.getValue(), transition.getKey(), expanding, heuristicFunction(transition.getValue())));
            }
        }
        
        return null;
    }
    
    /**
     * Given a leaf node in the search tree (a goal), returns a solution by traversing
     * up the search tree, collecting actions along the way, until reaching the root
     * 
     * @param last SearchTreeNode to start the upward traversal at (a goal node)
     * @return ArrayList sequence of actions; solution of format ["U", "R", "U", ...]
     */
    private static ArrayList<String> getPath (SearchTreeNode last) {
        ArrayList<String> result = new ArrayList<>();
        for (SearchTreeNode current = last; current.parent != null; current = current.parent) {
            result.add(current.action);
        }
        Collections.reverse(result);
        return result;
    }
    
    
    private static double heuristicFunction (MazeState s) {
        throw new UnsupportedOperationException();
    }
}

/**
 * SearchTreeNode that is used in the Search algorithm to construct the Search
 * tree.
 */
class SearchTreeNode {
    
    MazeState state;
    String action;
    SearchTreeNode parent;
    double evaluation;
    
    /**
     * Constructs a new SearchTreeNode to be used in the Search Tree.
     * 
     * @param state The MazeState (row, col) that this node represents.
     * @param action The action that *led to* this state / node.
     * @param parent Reference to parent SearchTreeNode in the Search Tree.
     */
    SearchTreeNode (MazeState state, String action, SearchTreeNode parent, double evaluation) {
        this.state = state;
        this.action = action;
        this.parent = parent;
        this.evaluation = evaluation;
    }
    
    public int compareTo(SearchTreeNode n) {
        if (this.evaluation < n.evaluation){
            return -1;
        } else if (this.evaluation > n.evaluation){
            return 1;
        } else
            return 0;
    }
    
}
