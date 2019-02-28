package nim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Artificial Intelligence responsible for playing the game of Nim! Implements
 * the alpha-beta-pruning mini-max search algorithm
 */
public class NimPlayer {

    private final int MAX_REMOVAL;

    NimPlayer(int MAX_REMOVAL) {
        this.MAX_REMOVAL = MAX_REMOVAL;
    }

    /**
     * 
     * @param remaining Integer representing the amount of stones left in the pile
     * @return An int action representing the number of stones to remove in the
     *         range of [1, MAX_REMOVAL]
     */
    public int choose(int remaining) {
        GameTreeNode root = new GameTreeNode(remaining, 0, true);
        Map<GameTreeNode, Integer> visited = new HashMap<GameTreeNode, Integer>();
        root.score = alphaBetaMinimax(root, Integer.MIN_VALUE, Integer.MAX_VALUE, true, visited);
        for(GameTreeNode kid : root.children) {
        	if (kid.score == root.score) {
        		return kid.action;
        	}
        }
        return 1;
    }

    /**
     * Constructs the minimax game tree by the tenets of alpha-beta pruning with
     * memoization for repeated states.
     * 
     * @param node    The root of the current game sub-tree
     * @param alpha   Smallest minimax score possible
     * @param beta    Largest minimax score possible
     * @param isMax   Boolean representing whether the given node is a max (true) or
     *                min (false) node
     * @param visited Map of GameTreeNodes to their minimax scores to avoid
     *                repeating large subtrees
     * @return Minimax score of the given node + [Side effect] constructs the game
     *         tree originating from the given node how do you store the tree? is it
     *         only generated for the subtree?
     */
    private int alphaBetaMinimax(GameTreeNode node, int alpha, int beta, boolean isMax,
            Map<GameTreeNode, Integer> visited) {
        // Create children of current node.
        for (int i = 3; i > 0; i--) {
            if (node.remaining - i >= 0) {
                node.children.add(new GameTreeNode(node.remaining - i, i, !isMax));
            }
        }

        int v;
        
        // Check if terminal node or if node has been memoized/visited before.
        // TO BREAK THE RECURSION, YOU WANT AN IF STATEMENT CHECKING IF THE REMAINING FOR THE CURRENT NODE
        // THAT YOU ARE PASSING IN IS 0; IF IT'S A MAX, THEN YOU WOULD RETURN 0 OTHERWISE RETURN 1
        if (node.children.size() == 0) {
            visited.put(node, node.score);
            return 0;
        }
        if (visited.get(node) != null) {
            return visited.get(node);
        }
        
        if (isMax) {
            v = Integer.MIN_VALUE;
            // SINCE WE ARE NOT SURE WHETHER OR NOT THE CHILD NODE IN QUESTION IS ALREADY IN VISITED, 
            // AND WE ALSO DON'T KNOW IF ALPHA BETA MINIMAX WILL MEAN YOU HAVE TO PRUNE ANY OF THE CHILDREN
            // IT'S NOT SMART TO CREATE THE KIDS ON SIGHT
            // INSTEAD YOUR FOR LOOP SHOULD START AT 1 FOR THE ACTION, STOP AT THE MINIMUM OF THE MAX_REMOVAL AND THE REMAINING 
            // 1) MAKE KID 
            // 2) CHECK IF IT IS ALREADY IN THE VISITED
            		// IF ALREADY IN VISITED THEN YOU JUST SET V TO THE MAX/MIN OF V AND THE SCORE OF THE NODE THAT ALREADY EXISTS IN VISITED TO HANDLE MEMOIZATION
            		// OTHERWISE, SET THE V TO THE RECURSIVE CALL TO THE MAX/MIN OF THE V AND THE RECURSIVE CALL 
            //ALPHA/BETA IS INE AS IS JUST RETURN V AFTER EVERY CONDITION 
            for (GameTreeNode child : node.children) {
                v = Math.max(v, alphaBetaMinimax(child, alpha, beta, false, visited));
                alpha = Math.max(alpha, v);
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            v = Integer.MAX_VALUE;
            for (GameTreeNode child : node.children) {
                v = Math.min(v, alphaBetaMinimax(child, alpha, beta, true, visited));
                beta = Math.min(beta, v);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        visited.put(node, v);
        return v;
    }
}

/**
 * GameTreeNode to manage the Nim game tree.
 */
class GameTreeNode {

    int remaining, action, score;
    boolean isMax;
    ArrayList<GameTreeNode> children;

    /**
     * Constructs a new GameTreeNode with the given number of stones remaining in
     * the pile, and the action that led to it. We also initialize an empty
     * ArrayList of children that can be added-to during search, and a placeholder
     * score of -1 to be updated during search.
     * 
     * @param remaining The Nim game state represented by this node: the # of stones
     *                  remaining in the pile
     * @param action    The action (# of stones removed) that led to this node
     * @param isMax     Boolean as to whether or not this is a maxnode
     */
    GameTreeNode(int remaining, int action, boolean isMax) {
        this.remaining = remaining;
        this.action = action;
        this.isMax = isMax;
        children = new ArrayList<>();
        score = -1;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GameTreeNode
                ? remaining == ((GameTreeNode) other).remaining && isMax == ((GameTreeNode) other).isMax
                        && action == ((GameTreeNode) other).action
                : false;
    }

    @Override
    public int hashCode() {
        return remaining + ((isMax) ? 1 : 0);
    }

}
