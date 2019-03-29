package lcs;

import java.util.HashSet;
import java.util.Set;

public class LCS {

    /**
     * memoCheck is used to verify the state of your tabulation after performing
     * bottom-up and top-down DP. Make sure to set it after calling either one of
     * topDownLCS or bottomUpLCS to pass the tests!
     */
    public static int[][] memoCheck;

    // -----------------------------------------------
    // Shared Helper Methods
    // -----------------------------------------------

    // [!] TODO: Add your shared helper methods here!
    public static Set<String> collectSolution(String rStr, int row, String cStr, int col, Set<String> result) {
        if (row == 0 || col == 0) {
            return result;
        }

        char rLetter = rStr.charAt(row - 1), cLetter = cStr.charAt(col - 1);

        if (rLetter == cLetter) {
            result = addLetter(Character.toString(cLetter), collectSolution(rStr, row - 1, cStr, col - 1, result));
            return result;
        } else {
            Set<String> leftResult = new HashSet<String>();
            Set<String> upResult = new HashSet<String>();
            if (memoCheck[row][col] >= memoCheck[row - 1][col]) {
                leftResult.addAll(collectSolution(rStr, row - 1, cStr, col, result));
            }

            if (memoCheck[row][col] >= memoCheck[row][col - 1]) {
                upResult.addAll(collectSolution(rStr, row, cStr, col - 1, result));
            }
            leftResult.addAll(upResult);
            return leftResult;
        }
    }
    
    public static Set<String> addLetter(String letter, Set<String> collected) {
        HashSet<String> result = new HashSet<String>();
        for (String s : collected) {
            result.add(s + letter);
        }
        
        return result;
    }

    public static void createMemo(String rStr, String cStr) {
        memoCheck = new int[rStr.length() + 1][cStr.length() + 1];

        for (int i = 0; i < rStr.length() + 1; i++) {
            memoCheck[i][0] = 0;
        }

        for (int i = 0; i < cStr.length() + 1; i++) {
            memoCheck[0][i] = 0;
        }
    }

    // -----------------------------------------------
    // Bottom-Up LCS
    // -----------------------------------------------

    /**
     * Bottom-up dynamic programming approach to the LCS problem, which solves
     * larger and larger subproblems iterative using a tabular memoization
     * structure.
     * 
     * @param rStr The String found along the table's rows
     * @param cStr The String found along the table's cols
     * @return The longest common subsequence between rStr and cStr + [Side Effect]
     *         sets memoCheck to refer to table
     */
    public static Set<String> bottomUpLCS(String rStr, String cStr) {
        Set<String> result = new HashSet<String>();
        result.add("");
        if (rStr.length() == 0 || cStr.length() == 0) {
            return result;
        }

        createMemo(rStr, cStr);
        evaluateCellsBottomUp(rStr, cStr);
        result = collectSolution(rStr, rStr.length(), cStr, cStr.length(), result);


        return result;
    }

    // [!] TODO: Add any bottom-up specific helpers here!
    public static void evaluateCellsBottomUp(String rStr, String cStr) {
        for (int col = 1; col < rStr.length() + 1; col++) {
            for (int row = 1; row < cStr.length() + 1; row++) {
                char rLetter = rStr.charAt(row - 1), cLetter = cStr.charAt(col - 1);
                if (rLetter != cLetter) {
                    memoCheck[row][col] = Math.max(memoCheck[row - 1][col], memoCheck[row][col - 1]);
                } else {
                    memoCheck[row][col] = memoCheck[row - 1][col - 1] + 1;
                }
            }
        }
    }

    // -----------------------------------------------
    // Top-Down LCS
    // -----------------------------------------------

    /**
     * Top-down dynamic programming approach to the LCS problem, which solves
     * smaller and smaller subproblems recursively using a tabular memoization
     * structure.
     * 
     * @param rStr The String found along the table's rows
     * @param cStr The String found along the table's cols
     * @return The longest common subsequence between rStr and cStr + [Side Effect]
     *         sets memoCheck to refer to table
     */
    public static Set<String> topDownLCS(String rStr, String cStr) {

        throw new UnsupportedOperationException();
        // Set<String> result;
        // memoCheck = new int[rStr.length()][cStr.length()];
        // return collectSolution(rStr, 0, cStr, 0);
    }

    // [!] TODO: Add any top-down specific helpers here!

}
