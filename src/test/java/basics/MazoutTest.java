package basics;

import java.util.*;

import java.util.Random;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Grade
public class MazoutTest {

    private static final Random random = new Random(134203);

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void toyExample() {
        Mazout.City city = new Mazout.City();
        Mazout.House h1 = city.addHouse(12);
        Mazout.House h2 = city.addHouse(6);
        Mazout.House h3 = city.addHouse(15);
        Mazout.House h4 = city.addHouse(30);
        Mazout.House h5 = city.addHouse(21);
        Mazout.House h6 = city.addHouse(39);
        Mazout.House h7 = city.addHouse(55);
        Mazout.House h8 = city.addHouse(27);
        Mazout.House h9 = city.addHouse(30);

        h1.setNeighborRight(h2);
        h1.setNeighborDown(h3);

        h2.setNeighborLeft(h1);
        h2.setNeighborRight(h3);
        h2.setNeighborDown(h5);

        h3.setNeighborLeft(h2);
        h3.setNeighborDown(h6);

        h4.setNeighborRight(h5);
        h4.setNeighborAbove(h1);
        h4.setNeighborDown(h7);

        h5.setNeighborLeft(h4);
        h5.setNeighborRight(h6);
        h5.setNeighborAbove(h2);
        h5.setNeighborDown(h8);

        h6.setNeighborLeft(h5);
        h6.setNeighborAbove(h3);
        h6.setNeighborDown(h9);

        h7.setNeighborRight(h8);
        h7.setNeighborAbove(h4);

        h8.setNeighborLeft(h9);
        h8.setNeighborRight(h7);
        h8.setNeighborAbove(h5);

        h9.setNeighborLeft(h8);
        h9.setNeighborAbove(h6);

        int demand = Mazout.getTotalDemand(city, h1, new String[]{"right", "down", "left", "down"});
        assertEquals(12+6+21+30+55, demand);

        assertEquals(30, Mazout.getTotalDemand(city, h9, new String[]{}));
    }

    /**
     * Generates a rectangular grid of houses.
     */
    private static class RectangularGrid {
        private final Mazout.City city;
        private final Mazout.House[][] houses;
        private final int rows;
        private final int cols;

        public RectangularGrid(int rows,
                               int cols) {
            city = new Mazout.City();
            houses = new Mazout.House[rows][cols];
            this.rows = rows;
            this.cols = cols;

            for (int row = 0; row < rows; row++) {
                for (int col=0; col < cols; col++) {
                    houses[row][col] = city.addHouse(10 + random.nextInt(90));
                }
            }
            
            for (int row = 0; row < rows; row++) {
                for (int col=0; col < cols; col++) {
                    if (row > 0) {
                        houses[row][col].setNeighborAbove(houses[row-1][col]);
                    }
                    if (row < rows - 1) {
                        houses[row][col].setNeighborDown(houses[row+1][col]);
                    }
                    if (col > 0) {
                        houses[row][col].setNeighborLeft(houses[row][col-1]);
                    }
                    if (col < cols - 1) {
                        houses[row][col].setNeighborRight(houses[row][col+1]);
                    }
                }
            }
        }

        public Mazout.House getHouse(int row, int col) {
            return houses[row][col];
        }

        public Mazout.City getCity() {
            return city;
        }

        public int getRows() {
            return rows;
        }

        public int getCols() {
            return cols;
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testVisitMultipleTimes2() {
        RectangularGrid grid = new RectangularGrid(3, 3);

        int demand = Mazout.getTotalDemand(grid.getCity(), grid.getHouse(1, 1), new String[]{});
        assertEquals(grid.getHouse(1,1).getCapacity(), demand);

        demand = Mazout.getTotalDemand(grid.getCity(), grid.getHouse(1, 1), new String[]{"right"});
        assertEquals(grid.getHouse(1,1).getCapacity() + grid.getHouse(1,2).getCapacity(), demand);

        demand = Mazout.getTotalDemand(grid.getCity(), grid.getHouse(1, 1),
                                       new String[]{"right", "left", "right", "left", "right", "left"});
        assertEquals(grid.getHouse(1,1).getCapacity() + grid.getHouse(1,2).getCapacity(), demand);
    }

    // BEGIN STRIP

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testVisitMultipleTimes() {
        for (int i = 0; i < 10; i++) {
            int rows = 10 + random.nextInt(40);
            int cols = 10 + random.nextInt(40);
            RectangularGrid grid = new RectangularGrid(rows, cols);
            int startRow = random.nextInt(rows);
            int startCol = random.nextInt(cols);
            int pathLength = rows*cols+1;
            String [] path = createRandomPath(grid, startRow, startCol, pathLength);
            int demand = getPathDemand(grid, path, startRow, startCol);
            assertEquals(demand, Mazout.getTotalDemand(grid.getCity(), grid.getHouse(startRow, startCol), path));
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testNoVisitMultipleTime() {
        for (int i = 0; i < 10; i++) {
            int rows = 10 + random.nextInt(40);
            int cols = 10 + random.nextInt(40);
            RectangularGrid grid = new RectangularGrid(rows, cols);
            int startRow = random.nextInt(rows);
            int startCol = random.nextInt(cols);
            int pathLength = random.nextInt(rows*cols/2);
            String [] path = createRandomPath(grid, startRow, startCol, pathLength);
            path = avoidMultipleVisit(path, startRow, rows, startCol, cols);
            int demand = getPathDemand(grid, path, startRow, startCol);
            assertEquals(demand, Mazout.getTotalDemand(grid.getCity(), grid.getHouse(startRow, startCol), path));
        }
    }


    /**
     * Changes `path` so that it does not visit multiple time the same house
     */
    private String [] avoidMultipleVisit(String [] path, int firstRow, int rows, int firstCol, int cols) {
        boolean [][] visited = new boolean[rows][cols];
        int limit = path.length;
        int row = firstRow;
        int col = firstCol;
        visited[row][col] = true;
        int i = 0;
        while (i < limit) {
            int newRow = row;
            int newCol = col;
            if (path[i].equals("left")) {
                newCol -= 1;
            } else if (path[i].equals("right")) {
                newCol += 1;
            } else if (path[i].equals("above")) {
                newRow -= 1;
            } else {
                newRow += 1;
            }
            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols || visited[newRow][newCol]) {
                limit -= 1;
                String tmp = path[limit];
                path[limit] = path[i];
                path[i] = tmp;
            } else {
                row = newRow;
                col = newCol;
                visited[row][col] = true;
                i += 1;
            }
        }
        String [] newPath = new String[limit];
        System.arraycopy(path,0,newPath,0, limit);
        return newPath;
    }

    /**
     * Gets for a given cell the possible directions in which it might go
     */
    private List<String> possibleDirection(int row, int rows, int col, int cols) {
        ArrayList<String> possible = new ArrayList<>();
        if (row > 0) {
            possible.add("above");
        }
        if (row < rows - 1) {
            possible.add("down");
        }
        if (col > 0) {
            possible.add("left");
        }
        if (col < cols - 1) {
            possible.add("right");
        }
        return possible;
    }

    /**
     * Create a valid path of size `pathLength` in the grid starting from the house at position
     * (firstRow, firstCol)
     */
    private String[] createRandomPath(RectangularGrid grid, int firstRow, int firstCol, int pathLength) {
        String [] path = new String[pathLength];
        int curRow = firstRow;
        int curCol = firstCol;
        for (int i = 0; i < pathLength; i++) {
            List<String> possibleDirections = possibleDirection(curRow, grid.getRows(), curCol, grid.getCols());
            String direction = possibleDirections.get(random.nextInt(possibleDirections.size()));
            path[i] = direction;
            if (direction.equals("left")) {
                curCol -= 1;
            } else if (direction.equals("right")) {
                curCol += 1;
            } else if (direction.equals("above")) {
                curRow -= 1;
            } else {
                curRow += 1;
            }
        }
        return path;
    }

    /**
     * Get the demand of fuel for `path` in `grid`
     */
    private int getPathDemand(RectangularGrid grid, String [] path, int firstRow, int firstCol) {
        boolean [][] visited = new boolean[grid.getRows()][grid.getCols()];

        int row = firstRow;
        int col = firstCol;
        int demand = grid.getHouse(row, col).getCapacity();
        visited[row][col] = true;
        for (String d : path) {
            if (d.equals("left")) {
                col -= 1;
            } else if (d.equals("right")) {
                col += 1;
            } else if (d.equals("above")) {
                row -= 1;
            } else {
                row += 1;
            }
            if (!visited[row][col]) {
                visited[row][col] = true;
                demand += grid.getHouse(row, col).getCapacity();
            }
        }
        return demand;
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testInvalidCommand() {
        RectangularGrid grid = new RectangularGrid(3, 3);

        try {
            Mazout.getTotalDemand(grid.getCity(), grid.getHouse(1, 1), new String[]{"right","nope"});
        } catch (IllegalArgumentException e) {
            // This is expected
        }
    }    

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testInvalidPath() {
        RectangularGrid grid = new RectangularGrid(3, 3);

        try {
            Mazout.getTotalDemand(grid.getCity(), grid.getHouse(1, 1), new String[]{"right","right"});
        } catch (IllegalArgumentException e) {
            // This is expected
        }
        
        try {
            Mazout.getTotalDemand(grid.getCity(), grid.getHouse(1, 1), new String[]{"left","left"});
        } catch (IllegalArgumentException e) {
            // This is expected
        }
        
        try {
            Mazout.getTotalDemand(grid.getCity(), grid.getHouse(1, 1), new String[]{"down","down"});
        } catch (IllegalArgumentException e) {
            // This is expected
        }
        
        try {
            Mazout.getTotalDemand(grid.getCity(), grid.getHouse(1, 1), new String[]{"above","above"});
        } catch (IllegalArgumentException e) {
            // This is expected
        }
        
        try {
            Mazout.getTotalDemand(grid.getCity(), null, new String[]{});
        } catch (IllegalArgumentException e) {
            // This is expected
        }
    }
    // END STRIP
}
