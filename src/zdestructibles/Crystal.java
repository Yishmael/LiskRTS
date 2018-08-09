package zdestructibles;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import db.Unit;
import enums.UnitType;

public class Crystal extends Unit {

    public Crystal(int x, int y, int width, int height) throws SlickException {
        super(x, y);
        setRectangle(new Rectangle(x, y, width, height));
        setModelImage(UnitType.crystal.getModelImage());
        setUnitType(UnitType.crystal);
        setHealth(1, 1);
    }

    public void draw(Graphics g) throws SlickException {
        g.drawImage(getModelImage(), getRectangle().getX(), getRectangle().getY(),
                getRectangle().getX() + getRectangle().getWidth(),
                getRectangle().getY() + getRectangle().getHeight(), 0, 0, 64, 64);
    }
}
