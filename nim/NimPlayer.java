package nim;

import java.util.ArrayList;
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
        throw new UnsupportedOperationException();
        // constructs a tree of options based off remaining
        // via alpha beta, locates the best action
        // returns that action
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
        int v;
        if (node.children.size() == 0) {
            visited.put(node, node.score);
            return node.score;
        }
        if (isMax) {
            v = Integer.MIN_VALUE;
            for (int i = 0; i <= node.children.size(); i++) {
                v = Math.max(v, alphaBetaMinimax(node.children.get(i), alpha, beta, false, visited));
                alpha = Math.max(alpha, v);
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            v = Integer.MAX_VALUE;
            for (int q = 0; q <= node.children.size(); q++) {
                v = Math.min(v, alphaBetaMinimax(node.children.get(q), alpha, beta, true, visited));
                beta = Math.min(beta, v);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        visited.put(node, v);
        return v;
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