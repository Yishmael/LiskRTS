package encaps;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import db.Unit;

public class Projectile {
    private float x, y, speed;
    private float angle;
    private Rectangle rectangle;
    private Image image;
    private Unit targetUnit;
    private Vector2i targetPoint;

    public Projectile(int x, int y, float angle, int speed) throws SlickException {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.speed = speed;

        rectangle = new Rectangle(x, y, 10, 10);

        image = new Image("res/images/other/projectile.png");
    }

    public void setTarget(Unit target) {
        this.targetUnit = target;
        targetPoint = null;
    }

    public void setTarget(int x, int y) {
        targetPoint = new Vector2i(x, y);
        targetUnit = null;
    }

    public void update(int dt) {
        float goalX = 0, goalY = 0;

        if (targetPoint != null) {
            goalX = targetPoint.getX();
            goalY = targetPoint.getY();
        } else if (targetUnit != null) {
            goalX = targetUnit.getCenterX();
            goalY = targetUnit.getCenterY();
        }

        angle = (float) Math.atan2(goalY - y, goalX - x);
        x += dt / 1000f * speed * Math.cos(angle);
        y += dt / 1000f * speed * Math.sin(angle);

        rectangle.setX(x);
        rectangle.setY(y);

    }

    public Unit getTargetUnit() {
        return targetUnit;
    }

    public boolean reachedTarget() {
        if (targetUnit != null) {
            return rectangle.intersects(targetUnit.getRectangle());
        } else if (targetPoint != null) {
            return rectangle.contains(targetPoint.getX(), targetPoint.getY());
        }

        return true;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, x + 10, y + 10, 0, 0, 32, 32);
    }

    public void deltaX(float dx) {
        x += dx;
    }

    public void deltaY(float dy) {
        y += dy;
    }
}
