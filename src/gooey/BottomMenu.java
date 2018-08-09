package gooey;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import db.Command;
import db.Unit;
import db.UnitFactory;
import db.Upgrade;
import db.abilities.Ability;
import db.abilities.Ability.AbilityTargetType;
import db.effects.Effect;
import encaps.Message;
import encaps.Message.MessageType;
import encaps.Vector2i;
import enums.AbilityType;
import enums.CommandType;
import enums.EffectType;
import enums.State;
import enums.TeamTag;
import enums.UnitClass;
import enums.UpgradeType;
import other.Consts;
import other.Misc;
import other.World;

public class BottomMenu {
    public enum View {
        defaultView, buildView, genericActionView;
    }

    public static int xOffset = 0, yOffset = Consts.SCREEN_HEIGHT - Consts.bottomMenuHeight + 30,
            padding = 10;
    public static Rectangle mapFrame = new Rectangle(xOffset + padding, yOffset, 160, 160);
    private Rectangle unitFrame = new Rectangle(mapFrame.getX() + mapFrame.getWidth() + padding, yOffset + 0,
            250, 160);
    private Image unitFrameImage;
    private Rectangle cmdFrame = new Rectangle(unitFrame.getX() + unitFrame.getWidth() + padding, yOffset,
            160, 160);
    private Image cmdFrameImage;
    private Rectangle currentScreenOnMinimap = new Rectangle(mapFrame.getX(), mapFrame.getY(),
            800f / 2000 * mapFrame.getWidth(), 400f / 2000 * mapFrame.getHeight());
    private View view = View.defaultView;
    private Button[] buttons;
    private Unit unit;
    private int cmdCount, unitCount, upgradeCount, abilityCount;
    private TeamTag team;
    private Unit unitToBuild = null;
    private Ability abilityToCast = null;
    private Unit abilityTarget = null;
    private Vector2i abilityTargetPoint = null;

    public BottomMenu(TeamTag team) throws SlickException {
        this.team = team;

        buttons = new Button[] {};

        unitFrameImage = new Image("res/images/gooey/unitframe.png");
        cmdFrameImage = new Image("res/images/gooey/cmdframe.png");

    }

    public void setUnit(Unit unit) throws SlickException {
        resetView();
        this.unit = unit;
        if (unit == null || unit.getTeamTag() != team) {
            buttons = new Button[0];
            return;
        }

        cmdCount = unit.getUnitCommands().size();
        unitCount = unit.getCreatableUnits().size() + unit.getBuildableUnits().size();
        upgradeCount = unit.getAvailableUpgrades().size();
        abilityCount = unit.getUnitAbilities().size();

        buttons = new Button[cmdCount + unitCount + upgradeCount + abilityCount + 2];
        for (int i = 0; i < cmdCount; i++) {
            buttons[i] = new Button(unit.getUnitCommands().get(i), View.defaultView);
        }
        for (int i = 0; i < unitCount; i++) {
            if (i < unit.getCreatableUnits().size()) {
                buttons[i + cmdCount] = new Button(unit.getCreatableUnits().get(i), View.defaultView);
            } else {
                buttons[i + cmdCount] = new Button(
                        unit.getBuildableUnits().get(i - unit.getCreatableUnits().size()), View.buildView);
            }
        }
        for (int i = 0; i < upgradeCount; i++) {
            buttons[i + cmdCount + unitCount] = new Button(unit.getAvailableUpgrades().get(i),
                    View.defaultView);
        }
        for (int i = 0; i < abilityCount; i++) {
            buttons[i + cmdCount + unitCount + upgradeCount] = new Button(unit.getUnitAbilities().get(i),
                    View.defaultView);
        }
        // TODO combine these into one button
        buttons[buttons.length - 2] = new Button(new Command(CommandType.cancel), View.genericActionView);
        buttons[buttons.length - 1] = new Button(new Command(CommandType.cancel), View.buildView);
    }

    public void draw(Graphics g) throws SlickException {

        g.setColor(new Color(0x6495ed));
        g.fillRect(0, Consts.SCREEN_HEIGHT - Consts.bottomMenuHeight, Consts.SCREEN_WIDTH,
                Consts.SCREEN_HEIGHT);

        g.setColor(Color.white);
        g.draw(mapFrame);
        g.drawImage(unitFrameImage, unitFrame.getX(), unitFrame.getY());
        g.draw(unitFrame);
        if (unit != null) {
            g.drawImage(unit.getAvatarImage(), unitFrame.getX() + 20, unitFrame.getY() + 20,
                    unitFrame.getX() + 70, unitFrame.getY() + 80, 0, 0, 32, 32);
        }
        g.drawImage(cmdFrameImage, cmdFrame.getX(), cmdFrame.getY());
        g.draw(cmdFrame);
        if (unit != null && unit.getTeamTag() == team) {
            for (Button button: buttons) {
                if (button.isVisibleInView(view)) {
                    g.setColor(Color.yellow);
                    int x = 450 + button.getIcon().getColumn() * (30 + padding) + xOffset;
                    int y = button.getIcon().getRow() * (30 + padding) + padding + yOffset;
                    button.drawAt(g, x, y);
                }
            }
        }

        g.setColor(Color.yellow);
        if (unit != null) {
            g.drawString("" + unit.getCurrentHealth() + "/" + unit.getMaxHealth(), unitFrame.getX() + 10,
                    unitFrame.getY() + 100);

            g.drawString("" + unit.getType().getName(), unitFrame.getX() + 90, unitFrame.getY() + 10);
            if (unit.getDamage() > 0) {
                g.drawString("DMG: " + unit.getDamage(), unitFrame.getX() + 90, unitFrame.getY() + 60);
            }
            g.drawString("ARMR: " + unit.getArmor(), unitFrame.getX() + 90, unitFrame.getY() + 75);
            if (unit.getMoveSpeed() > 0) {
                g.drawString("MS: " + unit.getMoveSpeed(), unitFrame.getX() + 90, unitFrame.getY() + 90);
            }
            for (int i = 0; i < unit.getUnitEffects().size(); i++) {
                Effect effect = unit.getUnitEffects().get(i);
                g.drawImage(effect.getIconImage(), unitFrame.getX() + 90 + 20 * i, unitFrame.getY() + 130,
                        unitFrame.getX() + 90 + 20 * (i + 1), unitFrame.getY() + 130 + 20, 0, 0, 32, 32);
            }
        }

        // TODO separate resources for each player
        g.drawString("" + World.playersResources[team.ordinal()].getDirt(), xOffset + 650 + 60 * 0,
                yOffset - 20);
        g.drawString("" + World.playersResources[team.ordinal()].getTimber(), xOffset + 650 + 60 * 1,
                yOffset - 20);
    }

    public void drawHoverTextFor(Graphics g, int x, int y) {

        if (unit == null) {
            return;
        }
        if (unit.getTeamTag() == team) {
            for (Button button: buttons) {
                if (button.isVisibleInView(view) && button.getRectangle().contains(x, y)) {
                    g.drawString(button.getIcon().getHoverText(), button.getRectangle().getX(),
                            button.getRectangle().getY() - 40);
                    return;
                }
            }
        }

        for (int i = 0; i < unit.getUnitEffects().size(); i++) {
            EffectType effectType = unit.getUnitEffects().get(i).getType();
            if (new Rectangle(unitFrame.getX() + 90 + 20 * i, unitFrame.getY() + 130, 20, 20).contains(x,
                    y)) {
                g.drawString(effectType.getHoverInfoText(), unitFrame.getX() + 90 + 20 * i,
                        unitFrame.getY() + 130 - 20);
                return;
            }
        }
    }

    // if target is valid, lock it
    public Message setAbilityTarget(Unit target) throws SlickException {
        if (unit.getState() == State.attack || unit.getState() == State.move) {
            abilityTarget = target;
            abilityTargetPoint = null;
            return handleLeftClickAtScreen(200, 200);
        }
        if (abilityToCast.getType().getAbilityTargetType() == AbilityTargetType.unitTarget) {
            if (UnitClass.canSourceAffectTarget(team,
                    unit.getUnitAbility(unit.getActiveAbility()).getAffectedUnitClasses(),
                    target.getTeamTag(), target.getType().getUnitClasses())) {
                abilityTarget = target;
                abilityTargetPoint = null;
                return handleLeftClickAtScreen(200, 200);
            }
        }
        return null;
    }

    // if target is valid, lock it
    public Message setAbilityTargetPoint(Vector2i point) throws SlickException {
        if (unit.getState() == State.attack || unit.getState() == State.move) {
            abilityTargetPoint = new Vector2i(point.x, point.y);
            abilityTarget = null;
            return handleLeftClickAtScreen(200, 200);
        }
        if (abilityToCast.getType().getAbilityTargetType() == AbilityTargetType.groundTarget) {
            abilityTargetPoint = new Vector2i(point.x, point.y);
            abilityTarget = null;
            return handleLeftClickAtScreen(200, 200);
        }
        return null;
    }

    public Message handleLeftClickAtScreen(int x, int y) throws SlickException {
        if (y >= Consts.SCREEN_HEIGHT - Consts.bottomMenuHeight || view == View.defaultView) {
            return null;
        }
        if (unit == null || unit.getTeamTag() != team) {
            return null;
        }

        if (view == View.buildView && unitToBuild != null) {
            if (Misc.canBuildingBeBuiltAt(unitToBuild.getCollisionSize(), x, y)) {
                int snappedX = Misc.getSnapped(x, World.mapX);
                int snappedY = Misc.getSnapped(y, World.mapY);
                unitToBuild.setX(snappedX);
                unitToBuild.setY(snappedY);
                unit.setTargetUnit(unitToBuild);
                unit.setState(State.build);
                unit.executeCurrentActionAt(snappedX, snappedY);
                unitToBuild = null;
                return new Message(MessageType.clearCursor, null);
            }
        } else if (view == View.genericActionView) {
            if (abilityToCast != null) {
                if (abilityTarget == null && abilityTargetPoint == null) {
                    return new Message(MessageType.requestingAbilityTargetAtPoint, new int[] { x, y });
                } else {
                    if (abilityTarget != null) {
                        unit.executeCurrentActionAt(abilityTarget);
                        view = View.defaultView;
                        abilityTarget = null;
                        abilityToCast = null;
                        return new Message(MessageType.clearCursor, null);
                    } else if (abilityTargetPoint != null) {
                        unit.executeCurrentActionAt(abilityTargetPoint.x, abilityTargetPoint.y);
                        view = View.defaultView;
                        abilityTargetPoint = null;
                        abilityToCast = null;
                        return new Message(MessageType.clearCursor, null);
                    }
                }
            } else {
                if (abilityTarget == null && abilityTargetPoint == null) {
                    return new Message(MessageType.requestingAbilityTargetAtPoint, new int[] { x, y });
                } else {
                    if (abilityTarget != null) {
                        unit.executeCurrentActionAt(abilityTarget);
                        view = View.defaultView;
                        abilityTarget = null;
                        abilityToCast = null;
                        return new Message(MessageType.clearCursor, null);
                    } else if (abilityTargetPoint != null) {
                        unit.executeCurrentActionAt(abilityTargetPoint.x, abilityTargetPoint.y);
                        view = View.defaultView;
                        abilityTargetPoint = null;
                        abilityToCast = null;
                        return new Message(MessageType.clearCursor, null);
                    }
                }

            }
        }
        return null;
    }

    public Message handleLeftClickAtGui(int x, int y) throws SlickException {
        if (y < Consts.SCREEN_HEIGHT - Consts.bottomMenuHeight) {
            return null;
        }
        if (unit == null || unit.getTeamTag() != team) {
            return null;
        }
        // handle clicks on buttons
        Message msg = null;
        for (Button button: buttons) {
            if (button.isVisibleInView(view) && button.getRectangle().contains(x, y)) {
                if (Unit.class.isInstance(button.getIcon())) {
                    if (!((Unit) button.getIcon()).isEnabled()) {
                        break;
                    }
                    // if unit is created over time, enqueue it
                    if (unit.canCreateUnit(((Unit) button.getIcon()).getType())) {
                        unit.enqueueUnit(((Unit) button.getIcon()).getType());
                        // if unit is built like a building, set it on cursor
                    } else if (unit.canBuildUnit(((Unit) button.getIcon()).getType())) {
                        Unit unit = new UnitFactory().createUnit(((Unit) button.getIcon()).getType());
                        unit.setTeamTag(this.unit.getTeamTag());
                        unitToBuild = unit;
                        msg = new Message(MessageType.setBuildingUnitOnCursor, unit);
                    }
                } else if (Command.class.isInstance(button.getIcon())) {
                    if (!unit.hasCommand(((Command) button.getIcon()).getType())) {
                        break;
                    }
                    switch (((Command) button.getIcon()).getType()) {
                    case attack:
                        unit.setState(State.attack);
                        view = View.genericActionView;
                        break;
                    case build:
                        view = View.buildView;
                        break;
                    case cancel:
                        unit.setState(null);
                        view = View.defaultView;
                        msg = new Message(MessageType.clearCursor, null);
                        break;
                    case move:
                        unit.setState(State.move);
                        view = View.genericActionView;
                        break;
                    case stop:
                        msg = new Message(MessageType.stop, null);
                        break;
                    }
                } else if (Upgrade.class.isInstance(button.getIcon())) {
                    if (!((Upgrade) button.getIcon()).isEnabled()) {
                        break;
                    }
                    if (UpgradeType.class.isInstance(((Upgrade) button.getIcon()).getUpgrade())) {
                        UpgradeType upgradeType = (UpgradeType) ((Upgrade) button.getIcon()).getUpgrade();
                        if (unit.hasAvailableUpgrade(upgradeType)) {
                            unit.enqueueUpgrade(upgradeType);
                        }
                    }
                } else if (Ability.class.isInstance(button.getIcon())) {
                    if (!button.getIcon().isEnabled()) {
                        break;
                    }
                    AbilityType abilityType = ((Ability) button.getIcon()).getType();
                    if (!unit.hasUnitAbility(abilityType)) {
                        break;
                    }
                    switch (abilityType.getAbilityTargetType()) {
                    case none:
                        break;
                    case noTarget:
                        ((Ability) button.getIcon()).callAllMethodsOnUnit(unit);
                        break;
                    case groundTarget:
                    case unitTarget:
                        view = View.genericActionView;
                        unit.setState(State.ability);
                        unit.setActiveAbility(abilityType);
                        abilityToCast = unit.getUnitAbility(unit.getActiveAbility());
                        msg = new Message(MessageType.setCursorForGroundTargetAbility,
                                ((Ability) button.getIcon()));
                        break;
                    }
                }

                break;
            }
        }
        return msg;

    }

    public void updateCurrentScreenOnMinimap(int x, int y) {
        currentScreenOnMinimap.setX(mapFrame.getX() - x * mapFrame.getWidth() / 2000);
        currentScreenOnMinimap.setY(mapFrame.getY() - y * mapFrame.getHeight() / 2000);
    }

    public void setTeam(TeamTag team) throws SlickException {
        this.team = team;
        setUnit(unit);
    }

    public void setView(View view) {
        this.view = view;
    }

    public Rectangle getMinimapRectangle() {
        return mapFrame;
    }

    public int getCurrentScreenX() {
        return (int) currentScreenOnMinimap.getX();
    }

    public int getCurrentScreenY() {
        return (int) currentScreenOnMinimap.getY();
    }

    public void setCurrentScreenOnMinimapCenter(int x, int y) {
        currentScreenOnMinimap.setX(x - currentScreenOnMinimap.getWidth() / 2);
        currentScreenOnMinimap.setY(y - currentScreenOnMinimap.getHeight() / 2);
    }

    public void clear() throws SlickException {
        setUnit(null);
    }

    public Message handleHotkeyKey(int keyCode) {
        if (unit == null || unit.getTeamTag() != team) {
            return null;
        }
        Message msg = null;

        if (keyCode == Input.KEY_A) {
            unit.setState(State.attack);
            view = View.genericActionView;
        } else if (keyCode == Input.KEY_O) {
            if (unit.hasCommand(CommandType.stop)) {
                msg = new Message(MessageType.stop, null);
            }
        } else if (keyCode == Input.KEY_M) {
            unit.setState(State.move);
            view = View.genericActionView;
        }
        return msg;
    }

    public View getView() {
        return view;
    }

    public void drawCurrentScreenOnMinimap(Graphics g) {
        g.setColor(Color.white);
        g.draw(currentScreenOnMinimap);
    }

    public Unit getUnit() {
        return unit;
    }

    public void refreshSelection() throws SlickException {
        setUnit(unit);
    }

    public void resetView() {
        view = View.defaultView;
        if (unit != null) {
            unit.setState(null);
        }
    }

}
