package db.abilities.neutral;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import encaps.Vector2i;
import enums.AbilityType;
import enums.UnitClass;

public class GatherPoint extends Ability {

    private Image gatherPointImage;
    private Unit gatherPointUnit;
    private Vector2i gatherPoint;

    public GatherPoint(Unit owner) throws SlickException {
        super(AbilityType.gatherPointAbility);

        affectedUnitClasses = new UnitClass[] {};

        setIconPosition(1, 3);

        addCalledMethod("setGatherPoint", new Class[] { int.class, int.class }, new Object[] { 200, 300 });

        gatherPointImage = new Image("res/images/other/gatherpoint.png");
    }

    public Vector2i getGatherPoint() {
        return gatherPoint;
    }

    public Image getGatherPointImage() {
        return gatherPointImage;
    }

    public boolean hasUnitTarget() {
        return gatherPointUnit != null;
    }

    public Unit getUnitTarget() {
        return gatherPointUnit;
    }

    public void draw(Graphics g) {
        if (gatherPoint != null) {
            g.drawImage(gatherPointImage, gatherPoint.getX() - 32 / 2, gatherPoint.getY() - 32);
        } else if (gatherPointUnit != null) {
            g.drawImage(gatherPointImage, gatherPointUnit.getCenterX() - 32 / 2,
                    gatherPointUnit.getCenterY() - 32);
        }
    }

    public Unit getGatherPointUnit() {
        return gatherPointUnit;
    }

    public void setTargetUnit(Unit targetUnit) {
        this.gatherPointUnit = targetUnit;
    }

    public void setGatherPoint(Vector2i gatherPoint) {
        this.gatherPoint = gatherPoint;
    }

}
