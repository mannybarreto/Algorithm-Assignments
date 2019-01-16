package pathfinder.uninformed;

/**
 * Maze Pathfinding representation of a given state, i.e., an occupiable position
 * in the given maze.
 */
public class MazeState {
    
    public int col, row;
    
    /**
     * Constructs a new MazeState, which tracks the given row and column that it
     * represents in the Maze.<br>
     * <b>NOTE: Row 0, Column 0 is located at the upper-left-hand corner of the maze!</b>
     * @param col Integer column number of this state (X coord in a Cartesian plane)
     * @param row Integer row number of this state (Y coord in a Cartesian plane)
     */
    MazeState (int col, int row) {
        this.col = col;
        this.row = row;
    }
    
    /**
     * [Mutator] Adds the coordinates of the given other MazeState to this one's; useful
     * for computing offsets given in MazeProblem transitions.
     * @param other The other MazeState to add to this one.
     */
    public void add (MazeState other) {
        this.col += other.col;
        this.row += other.row;
    }
    
    @Override
    public boolean equals (Object other) {
        return other instanceof MazeState 
            ? this.row == ((MazeState) other).row && this.col == ((MazeState) other).col
            : false;
    }
    
    @Override
    public int hashCode () {
        return row * col;
    }
    
    public String toString () {
        return "(" + col + ", " + row + ")";
    }
    
}
