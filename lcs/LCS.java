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
    public static Set<String> collectSolution (String rStr, int row, String cStr, int col, Set<String> result){        
        char rLetter = rStr.charAt(row - 2),
             cLetter = cStr.charAt(col - 2);

        if (row == 0 || col == 0) {
            result.add("");
            return result;
        }

        if (rLetter != cLetter) {
            if (memoCheck[row][col] > memoCheck[row - 1][col] && memoCheck[row][col] > memoCheck[row][col - 1]) {
                result.add(rStr.substring(row - 1));
                collectSolution(rStr, row - 1, cStr, col - 1, result);
            }
        } else {
            if (memoCheck[row][col] == memoCheck[row - 1][col]) {
                collectSolution(rStr, row - 1, cStr, col, result);
            }

            else if (memoCheck[row][col] == memoCheck[row][col - 1]) {
                collectSolution(rStr, row, cStr, col - 1, result);
            }
        }

        return result;
    }
    
    public static void createMemo (String rStr, String cStr) {
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
        
        if (rStr.length() == 0 || cStr.length() == 0) {
            result.add("");
            return result;
        }
        
        createMemo(rStr, cStr);
        evaluateCellsBottomUp(rStr, cStr);
        return collectSolution(rStr, rStr.length() + 1, cStr, cStr.length() + 1, result);
    }

    // [!] TODO: Add any bottom-up specific helpers here!
    public static void evaluateCellsBottomUp(String rStr, String cStr) {        
        for (int col = 1; col < rStr.length() + 1; col ++) {
            for (int row = 1; row < cStr.length() + 1; row++) {
                char rLetter = rStr.charAt(row - 1),
                     cLetter = cStr.charAt(col - 1);
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
