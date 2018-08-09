package gameplay;

import java.util.ArrayList;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import db.Unit;
import other.Consts;
import other.World;

public class FogOfWar {

    private class Blip {
        private Rectangle rectangle = new Rectangle(0, 0, 0, 0);
        private Color color = new Color(0, 0, 0, 0.8f);

        public Blip(Rectangle rectangle) {
            this.rectangle = rectangle;
        }

        public float getDarkness() {
            return color.a;
        }

        public void setDarkness(float amount) {
            color.a = amount;
        }

        @SuppressWarnings("unused")
        public void draw(Graphics g) {
            g.setColor(color);
            g.fill(rectangle);
        }

        public void drawAt(Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, 40, 40);
        }

    }

    public static Blip[][] fog = new Blip[2000 / 40][2000 / 40];
    public static int shiftX, shiftY;
    private long lastTimeUpdated;
    private boolean enabled;

    public FogOfWar() {
        init();
    }

    private void init() {
        for (int x = 0; x < fog.length; x++) {
            for (int y = 0; y < fog[0].length; y++) {
                fog[x][y] = new Blip(new Rectangle(x * 40, y * 40, 40, 40));
            }
        }
    }

    public void draw(Graphics g, ArrayList<Unit> units) {
        if (!enabled) {
            return;
        }
        if (Sys.getTime() - lastTimeUpdated >= 200) {
            lastTimeUpdated = Sys.getTime();

            // TODO make this go through only visible blips
            for (int x = 0; x < fog.length; x++) {
                for (int y = 0; y < fog[0].length; y++) {
                    if (fog[x][y].getDarkness() == 0.2f) {
                        fog[x][y].setDarkness(0.5f);
                    }
                }
            }
            for (Unit unit: units) {
                if (unit.getTeamTag().isEnemyOf(World.team)) {
                    continue;
                }
                if (unit.getCenterY() > Consts.SCREEN_HEIGHT - Consts.bottomMenuHeight
                        || unit.getCenterY() < 0 || unit.getCenterX() > Consts.SCREEN_WIDTH
                        || unit.getCenterX() < 0) {
                    continue;
                }
                int vision = 1;
                for (int i = -vision; i <= vision; i++) {
                    for (int j = -vision; j <= vision; j++) {
                        fog[(-shiftX + unit.getCenterX()) / 40 + i][(-shiftY + unit.getCenterY()) / 40 + j]
                                .setDarkness(0.2f);
                    }
                }
            }
        }

        for (int x = -shiftX / 40; x < -shiftX / 40 + 21; x++) {
            for (int y = -shiftY / 40; y < -shiftY / 40 + 11; y++) {
                if (x < 0 || y < 0) {
                    continue;
                }
                fog[x][y].drawAt(g, shiftX + x * 40, shiftY + y * 40);
            }
        }
    }

    public void deltaX(int dx) {
        shiftX += dx;
    }

    public void deltaY(int dy) {
        shiftY += dy;
    }

    public void toggle() {
        enabled = !enabled;
    }
}
