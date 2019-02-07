package pathfinder.informed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;

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
        // [!] Note: Hyper-Commenting below for illustrative purposes; you should not have
        // had / needed nearly as much as demonstrated below
        
        // Implementing BFS, so frontier is a Queue (which, in JCF, is an interface that
        // can be used atop a LinkedList implementation)
        Queue<SearchTreeNode> frontier = new LinkedList<>();
        
        // Add initial state to frontier
        frontier.add(new SearchTreeNode(problem.INITIAL_STATE, null, null));
        
        // Continue expanding nodes as long as the frontier is not empty
        // (not strictly necessary for this assignment because a solution was
        // always assumed to be
        while (!frontier.isEmpty()) {
            // Grab the front node of the queue - this is the node we're expanding
            SearchTreeNode expanding = frontier.poll();
            
            // If it's a goal state, we're done!
            if (problem.isGoal(expanding.state)) {
                return getPath(expanding);
            }
            
            // Otherwise, must generate children
            Map<String, MazeState> transitions = problem.getTransitions(expanding.state);
            // For each action:MazeState pair in the transitions...
            for (Map.Entry<String, MazeState> transition : transitions.entrySet()) {
                // ...create a new STN and add that to the frontier
                frontier.add(new SearchTreeNode(transition.getValue(), transition.getKey(), expanding));
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
    
}

/**
 * SearchTreeNode that is used in the Search algorithm to construct the Search
 * tree.
 */
class SearchTreeNode {
    
    MazeState state;
    String action;
    SearchTreeNode parent;
    
    /**
     * Constructs a new SearchTreeNode to be used in the Search Tree.
     * 
     * @param state The MazeState (row, col) that this node represents.
     * @param action The action that *led to* this state / node.
     * @param parent Reference to parent SearchTreeNode in the Search Tree.
     */
    SearchTreeNode (MazeState state, String action, SearchTreeNode parent) {
        this.state = state;
        this.action = action;
        this.parent = parent;
    }
    
}