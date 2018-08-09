package other;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.Sys;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import db.Unit;
import db.UnitFactory;
import db.abilities.Ability;
import db.abilities.Ability.AbilityTargetType;
import db.effects.Effect;
import encaps.Blueprint;
import encaps.Resources;
import encaps.StartLocation;
import enums.AbilityType;
import enums.EffectType;
import enums.TeamTag;
import enums.UnitClass;
import enums.UnitType;
import enums.UpgradeType;
import gooey.BottomMenu;
import gooey.SelectionBox;
import handlers.EffectHandler;
import handlers.UpgradeHandler;
import zpathfinding.Pathfinder;

public class World {
    private Image bgImage, minimapImage;
    private int cameraShiftX, cameraShiftY;
    private SelectionBox sbox = new SelectionBox();
    private ArrayList<Unit> units = new ArrayList<>();
    private ArrayList<Unit> unitQueue = new ArrayList<>();
    private ArrayList<Blueprint> blueprints = new ArrayList<>();
    private boolean aurasCheckedThisFrame;
    private long lastTimeAurasChecked, lastTimePathChecked;
    private Pathfinder pf;

    public static TeamTag team;
    public static int mapX, mapY;
    public static UnitFactory uf;
    public static Resources[] playersResources = new Resources[] { new Resources(), new Resources() };

    public World(Pathfinder pf) throws SlickException {
        this.pf = pf;

        minimapImage = new Image("res/images/none.png");
        bgImage = new Image("res/images/other/bg.png");

        uf = new UnitFactory();

        units.addAll(new StartLocation(TeamTag.team1, 1, 1).getUnits());
        units.addAll(new StartLocation(TeamTag.team2, 7, 1).getUnits());
        // units.addAll(new CollectionSpawner(CollectionSpawner.trees, 700, 100).getUnits());
        // units.addAll(new CollectionSpawner(CollectionSpawner.camp1, 700, 100).getUnits());

        Unit unit = uf.createUnitAt(UnitType.dirtMound, 7 * Consts.TILE_SIZE, 10 * Consts.TILE_SIZE);
        unit.setTeamTag(TeamTag.friendly);
        uf.addCreatableUnitsToUnit(unit);
        units.add(unit);
    }

    public void addUnit(Unit unit) throws SlickException {
        // care old instance

        for (Ability ability: unit.getUnitAbilities()) {
            if (ability.isEnabled() && ability.getType().getAbilityTargetType() == AbilityTargetType.none) {
                ability.callAllMethodsOnUnit(unit);
            }
        }

        for (UpgradeType upgradeType: uf.getCompletedUpgrades()) {
            UpgradeHandler.applyUpgradeToUnit(unit, upgradeType);
            if (unit.hasAvailableUpgrade(upgradeType)) {
                unit.removeAvailableUpgrade(upgradeType);
            }
        }

        applyCompletedUpgradesToAll(unit.getType());
        unitQueue.add(unit);
    }

    private void applyCompletedUpgradesToAll(Object upgrade) throws SlickException {
        if (upgrade.getClass() == UnitType.class) {
            uf.addCreatedUnit((UnitType) upgrade);
            for (Unit unit: units) {
                UpgradeHandler.applyUpgradeToUnit(unit, (UnitType) upgrade);
            }
        } else if (upgrade.getClass() == UpgradeType.class) {
            uf.addCompletedUpgrade((UpgradeType) upgrade);
            for (Unit unit: units) {
                UpgradeHandler.applyUpgradeToUnit(unit, (UpgradeType) upgrade);
            }
        }
    }

    public void applyEffectsToUnitFromUnit(Unit attackingUnit, Unit targetUnit) throws SlickException {
        for (Ability ability: attackingUnit.getUnitAbilities()) {
            // TODO make better checks
            if (ability.isEnabled() && ability.getType().getAbilityTargetType() == AbilityTargetType.none) {
                EffectHandler.applyEffectToUnitByUnit(attackingUnit, targetUnit, ability.getEffect());
            }
        }
    }

    public void applyUnitSelection() throws SlickException {
        if (sbox.isActive()) {
            sbox.applyUnitSelectionAsTeam(units, team);
        }
    }

    public void deleteSelecteedUnit() {
        for (Iterator<Unit> iter = units.iterator(); iter.hasNext();) {
            Unit unit = (Unit) iter.next();
            if (unit.isSelected()) {
                unit.setCurrentHealth(0);
                iter.remove();
                break;
            }
        }
    }

    public void deltaMapX(int dx) {
        mapX += dx;
        cameraShiftX -= dx;
    }

    public void deltaMapY(int dy) {
        mapY += dy;
        cameraShiftY -= dy;
    }

    public void deltaUnitsX(int dx) {
        for (Unit unit: units) {
            unit.deltaX(dx);
        }
    }

    public void deltaUnitsY(int dy) {
        for (Unit unit: units) {
            unit.deltaY(dy);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(bgImage, mapX, mapY);
    }

    public void drawSelectionBoxAt(Graphics g, int x, int y) {
        sbox.drawAt(g, x, y);
    }

    public void drawUnits(Graphics g) throws SlickException {
        for (Unit unit: units) {
            if (unit.isAffectedBy(EffectType.invisibilityEffect)) {
                if (!unit.getTeamTag().isEnemyOf(team)) {
                    unit.draw(g);
                }
            } else {
                unit.draw(g);
            }
        }

        for (Blueprint bp: blueprints) {
            bp.draw(g);
        }
    }

    public void addBlueprint(Blueprint bp) {
        blueprints.add(bp);
    }

    public void removeBlueprintAt(int x, int y) throws SlickException {
        for (Iterator<Blueprint> iter = blueprints.iterator(); iter.hasNext();) {
            Blueprint bp = (Blueprint) iter.next();
            if (bp.getX() == x && bp.getY() == y) {
                iter.remove();
            }
        }
    }

    public void drawMinimap(Graphics g) {
        g.drawImage(minimapImage, 0 + 10, Consts.SCREEN_HEIGHT - Consts.bottomMenuHeight + 30, 0 + 10 + 160,
                Consts.SCREEN_HEIGHT - Consts.bottomMenuHeight + 160, 0, 0, 32, 32);
        for (Unit unit: units) {
            // TODO update only every 0.5s?
            g.setColor(unit.getTeamTag().getColor());
            g.fillOval(BottomMenu.mapFrame.getX() + (unit.getX() + cameraShiftX) / 2000f * 160f,
                    BottomMenu.mapFrame.getY() + (unit.getY() + cameraShiftY) / 2000f * 160f, 5, 5);
        }
    }

    public void findTargetForUnit(Unit requestingUnit) throws SlickException {
        boolean targetFound = false;
        Unit targetUnit = null;
        int maxTargetPriority = 0;
        for (Unit target: units) {
            if (target.hashCode() == requestingUnit.hashCode()) {
                continue;
            }
            if (target.getTeamTag().isEnemyOf(requestingUnit.getTeamTag())) {
                if (Misc.getDistance(target.getCenterX(), target.getCenterY(), requestingUnit.getCenterX(),
                        requestingUnit.getCenterY()) <= requestingUnit.getTargetAcquisitionRange()) {
                    if (target.getUnitTargetPriority() > maxTargetPriority) {
                        maxTargetPriority = target.getUnitTargetPriority();
                        targetUnit = target;
                        targetFound = true;
                    }
                }
            }
        }
        if (targetFound) {
            requestingUnit.attackTarget(targetUnit);
        } else {
            // requestingUnit.attackMoveTo(requestingUnit.getTargetX(),
            // requestingUnit.getTargetY());
        }
    }

    private ArrayList<Unit> getAllUnitsWithinDistanceOfPoint(int x, int y, int distance) {
        ArrayList<Unit> result = new ArrayList<>();

        for (Unit unit: units) {
            if (Misc.getDistance(x, y, unit.getCenterX(), unit.getCenterY()) <= distance) {
                result.add(unit);
            }
        }
        return result;
    }

    public Unit getSelectedUnit() {
        for (Unit unit: units) {
            if (unit.isSelected()) {
                return unit;
            }
        }
        return null;
    }

    public Unit getUnitAt(int x, int y) {
        for (Unit unit: units) {
            if (unit.getRectangle().contains(x, y)) {
                return unit;
            }
        }
        return null;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void highlightUnitAt(int x, int y) {
        Unit unit = getUnitAt(x, y);
        if (unit != null) {
            unit.setHighlighted(true);
        }
    }

    public void highlightUnitClassAt(UnitClass[] classes, int x, int y) {
        Unit unit = getUnitAt(x, y);
        if (unit != null) {
            if (UnitClass.canSourceAffectTarget(team, classes, unit.getTeamTag(),
                    unit.getType().getUnitClasses())) {
                unit.setHighlighted(true);
                return;
            }
        }
    }

    public void orderSelectedUnitsTo(int x, int y) throws SlickException {
        Unit target = getUnitAt(x, y);
        for (Unit unit: units) {
            if (unit.isSelected() && unit.getTeamTag() == team) {
                // if unit is at the location, interact with it
                if (target != null) {
                    unit.interactWithUnit(target);
                    unit.setGatherPoint(target);
                    // else order move
                } else {
                    unit.setGatherPoint(x, y);
                    unit.walkTowardsPoint(x, y);
                }
            }
        }

    }

    public void openSelectionBoxAt(int x, int y) {
        sbox.openAt(x, y);
    }

    public void researchComplete(UpgradeType upgradeType) throws SlickException {
        for (Unit unit: units) {
            if (unit.hasAvailableUpgrade(upgradeType)) {
                unit.removeAvailableUpgrade(upgradeType);
            }
        }
        applyCompletedUpgradesToAll(upgradeType);
    }

    public TeamTag getTeam() {
        return team;
    }

    public void stopSelectedUnits() {
        for (Unit unit: units) {
            if (unit.isSelected() && unit.getTeamTag() == team) {
                unit.stopAllActions();
            }
        }
    }

    public void updateUnits(int dt) throws SlickException {

        for (Unit unit: units) {
            unit.update(dt);
            // temp
            if (unit.isSelected()) {
                if (Sys.getTime() - lastTimePathChecked > 500) {
                    lastTimePathChecked = Sys.getTime();
                }
            }

            if (Sys.getTime() - lastTimeAurasChecked >= Consts.auraLingerTime - 15) {
                aurasCheckedThisFrame = true;
                for (Ability ability: unit.getUnitAbilities()) {
                    if (!ability.isEnabled() || ability.getEffect() == null) {
                        continue;
                    }
                    int sourceX = unit.getCenterX();
                    int sourceY = unit.getCenterY();
                    if (ability.getType().getAbilityTargetType() == Ability.AbilityTargetType.none) {
                        for (Unit affUnit: getAllUnitsWithinDistanceOfPoint(sourceX, sourceY,
                                ability.getRange())) {
                            EffectHandler.applyEffectToUnitByUnit(unit, affUnit, ability.getEffect());
                        }
                    }
                }
                for (Effect effect: new ArrayList<>(unit.getUnitEffects())) {
                    if (Sys.getTime() - effect.getLastApplyTime() > effect.getDuration()) {
                        unit.clearEffect(effect.getType());
                    }
                }

            }
            for (Effect effect: new ArrayList<Effect>(unit.getUnitEffects())) {
                effect.applyTicksOnUnit(unit);
            }
        }

        if (aurasCheckedThisFrame) {
            lastTimeAurasChecked = Sys.getTime();
            aurasCheckedThisFrame = false;
        }
        // remove dead units
        for (Iterator<Unit> iter = units.iterator(); iter.hasNext();) {
            if (((Unit) iter.next()).isDead()) {
                iter.remove();
            }
        }
        // move enqueued units to main unit list
        for (Iterator<Unit> iter = unitQueue.iterator(); iter.hasNext();) {
            Unit unit = (Unit) iter.next();
            unit.setGatherPoint((int) unit.getX(), (int) unit.getY());
            // applyCompletedUpgradesToAll(unit.getType());
            // for (Ability ability: unit.getUnitAbilities()) {
            // if (ability.isEnabled()
            // && ability.getType().getAbilityTargetType() == AbilityTargetType.none) {
            // ability.callAllMethodsOnUnit(unit);
            // System.out.println(unit.getType() + " " + ability.getType());
            // }
            // }
            unit.setPathfinder(pf);
            uf.addCreatableUnitsToUnit(unit);
            applyCompletedUpgradesToAll(unit.getType());
            units.add(unit);
            iter.remove();
        }
    }

    public boolean isSelectionBoxActive() {
        return sbox.isActive();
    }

    public void useAbilityOnSelectedUnit(AbilityType abilityType) throws SlickException {
        for (Unit unit: units) {
            if (unit.isSelected()) {
                unit.getUnitAbility(abilityType).callAllMethodsOnUnit(unit);
                break;
            }
        }
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

}
