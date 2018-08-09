package other;

import zpathfinding.Pathfinder;

public class Misc {

    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static int getSnapped(int a, int worldA) {
        return (int) (a - 25f * 3 / 2 - (a + worldA) % 25);
    }

    public static boolean canBuildingBeBuiltAt(int collisionSize, int x, int y) {
        for (int i = 0; i < collisionSize * collisionSize; i++) {
            int j = Misc.getSnapped(x, World.mapX) + 25 * (i % collisionSize);
            int k = Misc.getSnapped(y, World.mapY) + 25 * (i / collisionSize);
            if (Pathfinder.grid[j / 60][k / 60].wall) {
                return false;
            }
        }
        return true;
    }
}
