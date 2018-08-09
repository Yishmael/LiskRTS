package zdestructibles;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import db.Unit;
import enums.UnitType;

public class Tent extends Unit {

    public Tent(int x, int y, int width, int height) throws SlickException {
        super(x, y);
        setRectangle(new Rectangle(x, y, width, height));
        setModelImage(UnitType.tent.getModelImage());
        setUnitType(UnitType.tent);
        setHealth(1, 1);
    }

    public void draw(Graphics g) throws SlickException {
        g.drawImage(getModelImage(), getRectangle().getX(), getRectangle().getY(),
                getRectangle().getX() + getRectangle().getWidth(),
                getRectangle().getY() + getRectangle().getHeight(), 0, 0, 64, 64);
    }
}
