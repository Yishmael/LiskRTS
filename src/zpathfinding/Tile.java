package zpathfinding;

import java.util.ArrayList;

public class Tile {
    public int i, j, f = 0, g = 0, h = 0;
    public Tile previous = null;
    public boolean wall;
    public ArrayList<Tile> neighbors = new ArrayList<>();

    public Tile(int i, int j) {
        this.i = i;
        this.j = j;
        this.wall = Math.random() < 0.0f;
    }

    public void addNeighbors(Tile[][] grid) {
        if (i < grid.length - 1) {
            neighbors.add(grid[i + 1][j]);
        }
        if (i > 0) {
            neighbors.add(grid[i - 1][j]);
        }
        if (j < grid[0].length - 1) {
            neighbors.add(grid[i][j + 1]);
        }
        if (j > 0) {
            neighbors.add(grid[i][j - 1]);
        }

        // diagonal movement
        if (i > 0 && j > 0) {
            neighbors.add(grid[i - 1][j - 1]);
        }
        if (i > 0 && j < grid[0].length - 1) {
            neighbors.add(grid[i - 1][j + 1]);
        }
        if (i < grid.length - 1 && j > 0) {
            neighbors.add(grid[i + 1][j - 1]);
        }
        if (i < grid.length - 1 && j < grid[0].length - 1) {
            neighbors.add(grid[i + 1][j + 1]);
        }
    }
}
