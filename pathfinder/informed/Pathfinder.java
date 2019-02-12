package pathfinder.informed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Maze Pathfinding algorithm that implements an informed A* search.
 * 
 * @author Manny Barreto
 * @author Bennett Shingledecker
 */
public class Pathfinder {
    /**
     * Given a MazeProblem, which specifies the actions and transitions available in
     * the search, returns a solution to the problem as a sequence of actions that
     * leads from the initial to a goal state.
     * 
     * @param problem A MazeProblem that specifies the maze, actions, transitions.
     * @return An ArrayList of Strings representing actions that lead from the
     *         initial to the goal state, of the format: ["R", "R", "L", ...]
     */
    public static ArrayList<String> solve(MazeProblem problem) {
        SearchTreeNode key = findGoal(problem, new SearchTreeNode(problem.INITIAL_STATE, null, null, 0),
                problem.KEY_STATE);

        if (key == null) {
            return null;
        } else {
            return getPath(findGoal(problem, key, problem.GOAL_STATES));
        }
    }

    /**
     * Given a leaf node in the search tree (a goal), returns a solution by
     * traversing up the search tree, collecting actions along the way, until
     * reaching the root
     * 
     * @param last SearchTreeNode to start the upward traversal at (a goal node)
     * @return ArrayList sequence of actions; solution of format ["U", "R", "U",
     *         ...]
     */
    private static ArrayList<String> getPath(SearchTreeNode last) {
        ArrayList<String> result = new ArrayList<>();

        for (SearchTreeNode current = last; current.parent != null; current = current.parent) {
            result.add(current.action);
        }

        Collections.reverse(result);

        return result;
    }

    /**
     * Given a problem, starting node, and destinations HashSet, finds the path to the nearest
     * destination state.
     * 
     * @param problem MazeState problem pertaining to the maze being solved.
     * @param initial SearchTreeNode is where the search begins, keeping track of previous searches.
     * @param dests HashSet<MazeState> containing all destinations to search to, moving towards the nearest one.
     * @return SearchTreeNode containing optimal path to nearest State in dests.
     */
    private static SearchTreeNode findGoal(MazeProblem problem, SearchTreeNode initial, HashSet<MazeState> dests) {
        PriorityQueue<SearchTreeNode> frontier = new PriorityQueue<SearchTreeNode>(new Comparator<SearchTreeNode>() {
            public int compare(SearchTreeNode i, SearchTreeNode j) {
                if (i.evaluation < j.evaluation) {
                    return -1;
                } else if (i.evaluation > j.evaluation) {
                    return 1;
                } else
                    return 0;
            }
        });

        HashSet<MazeState> graveyard = new HashSet<MazeState>();

        frontier.add(initial);

        while (!frontier.isEmpty()) {
            SearchTreeNode expanding = frontier.poll();
            graveyard.add(expanding.state);

            if (dests.contains(expanding.state)) {
                return expanding;
            }

            Map<String, MazeState> transitions = problem.getTransitions(expanding.state);

            for (Map.Entry<String, MazeState> transition : transitions.entrySet()) {
                if (!graveyard.contains(transition.getValue())) {
                    frontier.add(new SearchTreeNode(transition.getValue(), transition.getKey(), expanding,
                            heuristicFunction(transition.getValue(), problem, dests)));
                }
            }
        }

        return null;
    }

    /**
     * Uses Manhattan Distance and cost to score different nodes relative to given dests for findGoals frontiers.
     * 
     * @param s State to be analyzed heuristically.
     * @param problem MazeProblem providing the maze to be used to score nodes.
     * @param dests Destinations used to find manhattan distance.
     * @return int corresponding to heuristic score.
     */
    private static int heuristicFunction(MazeState s, MazeProblem problem, HashSet<MazeState> dests) {
        int cost = problem.getCost(s);
        int distance = 90000;

        for (MazeState goal : dests) {
            int x = Math.abs(s.row - goal.row);
            int y = Math.abs(s.col - goal.col);
            if (x + y < distance) {
                distance = x + y;
            }
        }

        return cost + distance;
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
     * @param state  The MazeState (row, col) that this node represents.
     * @param action The action that *led to* this state / node.
     * @param parent Reference to parent SearchTreeNode in the Search Tree.
     */

    SearchTreeNode(MazeState state, String action, SearchTreeNode parent, double evaluation) {
        this.state = state;
        this.action = action;
        this.parent = parent;
        this.evaluation = evaluation;
    }

}
