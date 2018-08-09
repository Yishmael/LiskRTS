package encaps;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import db.Unit;

public class Blueprint {
    private int x, y;
    private float scale;
    private Image image;
    private Color color = new Color(1, 1, 1, 0.3f);

    public Blueprint(Unit unit) {
        this.x = (int) unit.getX();
        this.y = (int) unit.getY();
        this.scale = unit.getScale();
        this.image = unit.getModelImage();
    }

    public void draw(Graphics g) {
        image.draw(x, y, scale, color);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
