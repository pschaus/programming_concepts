package basics;


//
// BEGIN STRIP
import java.util.Set;
import java.util.HashSet;
// END STRIP


/**
 * Only video instructions were given for the quiz https://www.youtube.com/watch?v=3qD_6wz7hOo
 *
 * The specification are as follows:
 *  - The ball enters from map[0][0] and goes east
 *  - The ball always falls down if exiting the map
 *  - The only elements in the maps are empty space ('.') and deviations ('/' and '\\')
 *  - Should return the number of cells visited by the ball, not counting multiple visits to the same cell
 *  - The students should note that one of the deviation symbol is an escape char
 */
public class Flipper {

    // BEGIN STRIP

    enum Direction {
        North,
        South,
        East,
        West,
    }

    /**
     * Updates the x coordinate depending on the Direction
     *
     * @param x the current x coordinate
     * @param dir the current Direction
     * @return the new x coordinate
     */
    private static int updateX(int x, Direction dir) {
        switch (dir) {
            case North:
                return x - 1;
            case South:
                return x + 1;
            default:
                return x;
        }
    }

    /**
     * Updates the y coordinate depending on the Direction
     *
     * @param y the current y coordinate
     * @param dir the current Direction
     * @return the new y coordinate
     */
    private static int updateY(int y, Direction dir) {
        switch (dir) {
            case East:
                return y + 1;
            case West:
                return y - 1;
            default:
                return y;
        }
    }

    /**
     * Updates the direction depending on the mirror orientation
     *
     * @param dir the current direction
     * @param mirror the mirror
     * @return the new direction
     */
    private static Direction reflect(Direction dir, char mirror) {
        switch (dir) {
            case East:
                return mirror == '/' ? Direction.North : Direction.South;
            case West:
                return mirror == '/' ? Direction.South : Direction.North;
            case North:
                return mirror == '/' ? Direction.East : Direction.West;
            default:
                return mirror == '/' ? Direction.West : Direction.East;
        }
    }

    // END STRIP


    /**
     * Counts the number of visited cells.
     * The light beam enters at position (0, 0) and head east.
     *
     * @param map the representation of the map
     * @return the number of visited cells, not counting multiple visits to the same cell
     */
    public static int run(char [][] map) {
        // TODO
        // STUDENT return -1;
        // BEGIN STRIP

        // The set used to store the cells that are enlighted
        Set<Integer> enlighted = new HashSet<>();
        // Current position and direction of the beam in the map, it starts in the corner at map[0][0]
        // and is going right (east).
        int x = 0;
        int y = 0;
        Direction dir = Direction.East;
        // While the beam is inside the map, it goes to the next cell.
        while (x >= 0 && x < map.length && y >= 0 && y < map[0].length) {
            // The id of the cell is computed using a 1 dimension representation of the 2 dimensions map.
            // As an example, for a map of size 3x3 the indexing works as illustrated below (the numbers
            // represent the index of each cell, the index [0][0] is on the top left).
            // [0, 1, 2]
            // [3, 4, 5]
            // [6, 7, 8]
            int cellId = x * map[0].length + y;
            // Adding the current cell to the set of visited cells. Since this is a set, adding a cell already in the set
            // does not modify it. Hence, we correctly do not count multiple times the same cell.
            enlighted.add(cellId);
            // If the current cell is a mirror, the direction of the beam is changed
            switch (map[x][y]) {
                case '/':
                    dir = reflect(dir, '/');
                    break;
                case '\\':
                    dir = reflect(dir, '\\');
                    break;
            }
            // Change the coordinates of the beam depending on its direction
            x = updateX(x, dir);
            y = updateY(y, dir);
        }
        return enlighted.size();
        // END STRIP
    }

}
