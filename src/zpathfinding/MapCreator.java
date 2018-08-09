package zpathfinding;

public class MapCreator {

    public static Tile[][] getMap(int rows, int cols) {
        Tile[][] grid = new Tile[cols][rows];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                grid[i][j] = new Tile(i, j);
                grid[i][j].wall = Math.random() < 0.2f;
            }
        }

        grid[0][0].wall = false;
        grid[cols - 1][rows - 1].wall = false;

        return grid;
    }
}
