package pathfinder.informed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Maze Pathfinding algorithm that implements a basic, uninformed, breadth-first tree search.
 */
public class Pathfinder {
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
        // Implementing A*, so frontier is a PriorityQueue. Changed comparator to compare heuristic values.
        PriorityQueue<SearchTreeNode> frontier = new PriorityQueue<SearchTreeNode>(new Comparator<SearchTreeNode>(){
                         //override compare method
         public int compare(SearchTreeNode i, SearchTreeNode j){
             if (i.evaluation < j.evaluation){
                 return -1;
             } else if (i.evaluation > j.evaluation){
                 return 1;
             } else
                 return 0;
             }
         });
        
        // Add initial state to frontier
        frontier.add(new SearchTreeNode(problem.INITIAL_STATE, null, null, 0));
        
        // Continue expanding nodes as long as the frontier is not empty
        // (not strictly necessary for this assignment because a solution was
        // always assumed to be
        while (!frontier.isEmpty()) {
            // Grab the front node of the queue - this is the node we're expanding
            SearchTreeNode expanding = frontier.poll();
            
            // If it's a goal state and we obtained the key, we're done!
            if (problem.isGoal(expanding.state) && problem.keyObtained) {
                return getPath(expanding);
            }
            
            if (problem.isKey(expanding.state)) {
                problem.keyObtained = true;
                frontier.clear();
            }
            
            // Otherwise, must generate children
            Map<String, MazeState> transitions = problem.getTransitions(expanding.state);
            // For each action:MazeState pair in the transitions...
            for (Map.Entry<String, MazeState> transition : transitions.entrySet()) {
                // ...create a new STN and add that to the frontier
                frontier.add(new SearchTreeNode(transition.getValue(), transition.getKey(), expanding, heuristicFunction(transition.getValue(), problem)));
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
    
    
    private static int heuristicFunction (MazeState s, MazeProblem problem) {
	int cost = problem.getCost(s);
	int distance = 90000;
	
	if (problem.keyObtained) {
    	for (int i = 0; i < problem.GOAL_STATES.size(); i++) { 
    	    int x = Math.abs(s.row - problem.GOAL_STATES.get(i).row); 
    	    int y = Math.abs(s.col - problem.GOAL_STATES.get(i).col);
    	    if (x + y < distance) {
    	        distance = x + y;
    	    }
    	}
	} else {
        int x = Math.abs(s.row - problem.KEY_STATE.row); 
        int y = Math.abs(s.col - problem.KEY_STATE.col);
	    distance =  x + y;
	}
    
   	return cost * distance;
	    //http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html#S7
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
}
