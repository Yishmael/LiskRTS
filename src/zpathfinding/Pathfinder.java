package zpathfinding;

import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import db.Unit;
import other.Consts;

public class Pathfinder {
    private int cols, rows;
    private ArrayList<Tile> path = new ArrayList<>();
    private ArrayList<Tile> openSet = new ArrayList<>();
    private ArrayList<Tile> closedSet = new ArrayList<>();
    private char[][] world;
    private Tile start, goal;
    private ArrayList<Unit> units;
    private long lastTimeCollisionChecked;
    public static Tile[][] grid = null;
    public static int shiftX, shiftY;

    public float heuristic(Tile a, Tile b) {
        return Math.abs(a.i - b.i) + Math.abs(a.j - b.j);
    }

    public Pathfinder() {

        cols = 2000 / Consts.TILE_SIZE;
        rows = (2000 - Consts.bottomMenuHeight) / Consts.TILE_SIZE + 1;
        grid = new Tile[cols][rows];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                grid[i][j] = new Tile(i, j);

            }
        }
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                grid[i][j].addNeighbors(grid);
            }
        }

        start = grid[0][0];
        start.wall = false;
        goal = grid[cols - 1][rows - 1];
        goal.wall = false;

        resetWorld();
    }

    private void findPath() {
        path.clear();
        openSet.clear();
        closedSet.clear();

        openSet.add(start);
        while (openSet.size() > 0) {
            int best = 0;
            for (int i = 0; i < openSet.size(); i++) {
                if (openSet.get(i).f < openSet.get(best).f) {
                    best = i;
                }
            }

            Tile current = openSet.get(best);

            if (current == goal) {
                Tile temp = current;
                path.add(temp);
                while (temp.previous != null) {
                    path.add(temp.previous);
                    temp = temp.previous;
                }
            }
            openSet.remove(current);
            closedSet.add(current);

            ArrayList<Tile> neighbors = new ArrayList<>(current.neighbors);
            for (Tile neighbor: neighbors) {
                if (!SpotsContainSpot(closedSet, neighbor) && !neighbor.wall) {
                    int temp_g = current.g + 1;

                    boolean new_path = false;
                    if (SpotsContainSpot(openSet, neighbor)) {
                        if (temp_g < neighbor.g) {
                            neighbor.g = temp_g;
                            new_path = true;
                        }
                    } else {
                        neighbor.g = temp_g;
                        new_path = true;
                        openSet.add(neighbor);
                    }
                    if (new_path) {
                        neighbor.h = (int) heuristic(neighbor, goal);
                        neighbor.f = neighbor.g + neighbor.h;
                        neighbor.previous = current;
                    }
                }
            }
        }
        if (path.isEmpty()) {
            System.out.println("No solution");
        } else {
            resetWorld();
        }
    }

    private boolean SpotsContainSpot(ArrayList<Tile> spots, Tile spot) {
        for (Tile s: spots) {
            if (s == spot) {
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                switch (world[i][j]) {
                case 'A':
                    g.setColor(Color.green);
                    break;
                case 'Z':
                    g.setColor(Color.orange);
                    break;
                case 'o':
                    g.setColor(Color.white);
                    continue;
                case 'x':
                    g.setColor(Color.red);
                    break;
                case '@':
                    g.setColor(Color.blue);
                    break;
                }
                g.setColor(g.getColor().multiply(new Color(1, 1, 1, 0.15f)));
                g.fillRect(i * (Consts.TILE_SIZE + 1) - shiftX, j * (Consts.TILE_SIZE + 1) - shiftY,
                        Consts.TILE_SIZE, Consts.TILE_SIZE);
            }
        }
    }

    private void resetWorld() {

        world = new char[cols][rows];

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                world[i][j] = 'o';
            }
        }

        for (Tile step: path) {
            world[step.i][step.j] = '@';
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j].wall) {
                    world[i][j] = 'x';
                }
            }
        }

        world[start.i][start.j] = 'A';
        world[goal.i][goal.j] = 'Z';

    }

    @SuppressWarnings("unused")
    private void printWorld() {
        for (int j = 0; j < grid[0].length; j++) {
            for (int i = 0; i < grid.length; i++) {
                System.out.print(world[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public void update(int dt) {
        if (Sys.getTime() - lastTimeCollisionChecked < 100) {
            return;
        }
        lastTimeCollisionChecked = Sys.getTime();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                grid[i][j].wall = false;
                for (Unit unit: units) {
                    if (unit.getCenterX() / Consts.TILE_SIZE == i
                            && unit.getCenterY() / Consts.TILE_SIZE == j) {
                        // System.out.println(unit.getType() + " @ " + j + ":" + i);
                        grid[i][j].wall = true;
                    }
                }
            }
        }
        // findPath();
    }

    public boolean canWalkOn(float x, float y) {
        return !grid[(int) (x / Consts.TILE_SIZE)][(int) (y / Consts.TILE_SIZE)].wall;
    }

    public ArrayList<Tile> getPath(float x1, float y1, float x2, float y2) {
        start = grid[(int) (x1 / Consts.TILE_SIZE)][(int) (y1 / Consts.TILE_SIZE)];
        start.previous = null;

        goal = grid[(int) (x2 / Consts.TILE_SIZE)][(int) (y2 / Consts.TILE_SIZE)];
        goal.previous = null;

        // System.out.println(start.i + ":" + start.j + "->" + goal.i + ":" + goal.j);

        findPath();

        if (path.size() > 0) {
            Collections.reverse(path);
            // path.remove(0);
        }
        return path;
    }
}
