package zdestructibles;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import db.Unit;
import enums.UnitType;

public class Tree extends Unit {

    public Tree(int id, int x, int y) throws SlickException {
        super(x, y);
        setModelImage(UnitType.valueOf("tree" + id).getModelImage());
        setRectangle(new Rectangle(x, y, getModelImage().getWidth(), getModelImage().getHeight()));
        setUnitType(UnitType.valueOf("tree" + id));
        setScale(1.5f);
        setHealth(1, 1);
    }

    public void draw(Graphics g) throws SlickException {
        g.drawImage(getModelImage(), getRectangle().getX(), getRectangle().getY(),
                getRectangle().getX() + getRectangle().getWidth(),
                getRectangle().getY() + getRectangle().getHeight(), 0, 0, 64, 64);
    }
}
