package gooey;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import db.Unit;
import enums.TeamTag;
import enums.UnitClass;
import other.Consts;

public class SelectionBox {

    private boolean active;
    private Unit selectedUnit;
    private Rectangle rect;
    private boolean unitSelected;

    public void openAt(float x, float y) {
        if (y > Consts.SCREEN_HEIGHT - Consts.bottomMenuHeight) {
            return;
        }
        if (!active) {
            rect = new Rectangle(x, y, 0, 0);
            active = true;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void drawAt(Graphics g, int x, int y) {
        if (active) {
            rect.setWidth(x - rect.getX());
            rect.setHeight(y - rect.getY());

            if (Math.abs(rect.getX() - x) > 20 || Math.abs(rect.getY() - y) > 20) {
                g.draw(rect);
            }
        }
    }

    public void applyUnitSelectionAsTeam(ArrayList<Unit> units, TeamTag team) throws SlickException {
        if (!active) {
            return;
        }
        transformRectangle();
        selectedUnit = null;
        boolean ownUnitSelected = false;
        int remainingCapacity = Consts.selectionCapacity;

        for (Unit unit: units) {
            if (unit.getRectangle().intersects(rect)) {
                ownUnitSelected = unit.getTeamTag() == team;
                if (ownUnitSelected) {
                    break;
                }
            }
        }

        // select all in the selection box
        for (Unit unit: units) {
            unit.setSelected(false);
            if (unit.getRectangle().intersects(rect)) {
                if (ownUnitSelected) {
                    if (unit.getTeamTag() == team) {
                        unit.setSelected(true);
                        remainingCapacity--;
                    } else {
                        continue;
                    }
                } else {
                    unit.setSelected(true);
                    remainingCapacity--;
                }
                if (!unit.getType().isUnitClass(UnitClass.structure)) {
                    unitSelected = true;
                }
                if (selectedUnit == null) {
                    selectedUnit = unit;
                }
            }
        }

        // if units are selected, deselect everything else
        if (unitSelected) {
            for (Unit unit: units) {
                if (unit.isSelected()) {
                    if (unit.getType().isUnitClass(UnitClass.structure)) {
                        unit.setSelected(false);
                        remainingCapacity++;
                    } else {
                        selectedUnit = unit;
                    }
                }
            }
        } else { // if only buildings are selected, deselect all but one
            boolean skipped = false;
            for (Unit unit: units) {
                if (unit.isSelected()) {
                    if (!skipped) {
                        selectedUnit = unit;
                        skipped = true;
                        continue;
                    }
                    unit.setSelected(false);
                    remainingCapacity++;
                }
            }

        }
        // deselect all units above selection capacity
        for (Unit unit: units) {
            if (unit.isSelected()) {
                if (remainingCapacity < 0) {
                    unit.setSelected(false);
                    remainingCapacity++;
                } else {
                    selectedUnit = unit;
                    break;
                }
            }
        }

        // if only units of other team(s) are selected, deselect all but one
        if (!ownUnitSelected) {
            for (Unit unit: units) {
                if (unit.isSelected()) {
                    if (Consts.selectionCapacity - remainingCapacity > 1) {
                        unit.setSelected(false);
                        remainingCapacity++;
                    } else {
                        selectedUnit = unit;
                    }
                }
            }
        }

        if (selectedUnit != null) {
            selectedUnit.setSelected(true);
        }
        active = false;

    }

    private void transformRectangle() {
        if (rect.getWidth() < 0) {
            rect.setWidth(Math.abs(rect.getWidth()));
            rect.setX(rect.getX() - rect.getWidth());

        }
        if (rect.getHeight() < 0) {
            rect.setHeight(Math.abs(rect.getHeight()));
            rect.setY(rect.getY() - rect.getHeight());
        }

    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

}
