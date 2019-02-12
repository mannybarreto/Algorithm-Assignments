package pathfinder.informed;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

/**
 * Unit tests for Maze Pathfinder. Tests include completeness and
 * optimality.
 */
public class PathfinderTests {

    @Test
    public void testPathfinder_t0() {
        String[] maze = {
            "XXXXXXX",
            "XI...KX",
            "X.....X",
            "X.X.XGX",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);

        // result will be a 2-tuple (isSolution, cost) where
        // - isSolution = 0 if it is not, 1 if it is
        // - cost = numerical cost of proposed solution
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]); // Test that result is a solution
        assertEquals(6, result[1]); // Ensure that the solution is optimal
    }

    @Test
    public void testPathfinder_t1() {
        String[] maze = {
            "XXXXXXX",
            "XI....X",
            "X.MMM.X",
            "X.XKXGX",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);

        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(14, result[1]); // Ensure that the solution is optimal
    }

    @Test
    public void testPathfinder_t2() {
        String[] maze = {
            "XXXXXXX",
            "XI.G..X",
            "X.MMMGX",
            "X.XKX.X",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);

        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(10, result[1]); // Ensure that the solution is optimal
    }

    @Test
    public void testPathfinder_t3() {
        String[] maze = {
            "XXXXXXX",
            "XI.G..X",
            "X.MXMGX",
            "X.XKX.X",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);

        assertNull(solution); // Ensure that Pathfinder knows when there's no solution
    }
    
    @Test
    public void testPathfinder_t4() {

        String[] maze = {
            "XXXXXXX",
            "XIMMMKX",
            "XMMMMMX",
            "XMMMMGX",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        // result will be a 2-tuple (isSolution, cost) where
        // - isSolution = 0 if it is not, 1 if it is
        // - cost = numerical cost of proposed solution
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]); // Test that result is a solution
        assertEquals(14, result[1]); // Ensure that the solution is optimal

    }
    
    @Test
    public void testPathfinder_t5() {

        String[] maze = {
            "XXXXXXX",
            "XI...GX",
            "X....MX",
            "X....MX",
            "X....MX",
            "XG...KX",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]); 
        assertEquals(12, result[1]); 

    }
    
    @Test
    public void testPathfinder_t6() {

        String[] maze = {
            "XXXXXXX",
            "XIKGGGX",
            "XGGGGGX",
            "XGGGGGX",
            "XGGGGGX",
            "XGGGGGX",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]); 
        assertEquals(2, result[1]); 

    }
    
    @Test
    public void testPathfinder_t7() {

        String[] maze = {
            "XXXXXXX",
            "XIG...X",
            "X.....X",
            "X.....X",
            "X.....X",
            "X....KX",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]); 
        assertEquals(15, result[1]); 

    }

} 
