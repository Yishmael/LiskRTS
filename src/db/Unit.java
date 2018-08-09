package db;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.Sys;
import org.lwjgl.util.Point;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Rectangle;

import db.abilities.Ability;
import db.abilities.neutral.GatherPoint;
import db.abilities.neutral.PassiveUnitCargo;
import db.effects.Effect;
import encaps.Blueprint;
import encaps.Message;
import encaps.Message.MessageType;
import encaps.Projectile;
import encaps.QueuedUnit;
import encaps.QueuedUpgrade;
import encaps.Vector2i;
import enums.AbilityType;
import enums.CommandType;
import enums.EffectType;
import enums.State;
import enums.TeamTag;
import enums.UnitClass;
import enums.UnitType;
import enums.UpgradeType;
import gameplay.Requirement;
import handlers.EffectHandler;
import other.Consts;
import other.Misc;
import other.World;
import zpathfinding.Pathfinder;
import zpathfinding.Tile;

public class Unit extends Interactive {
    private ArrayList<Command> unitCommands = new ArrayList<>();
    private ArrayList<Unit> creatableUnits = new ArrayList<>();
    private ArrayList<Unit> buildableUnits = new ArrayList<>();
    private ArrayList<Upgrade> availableUpgrades = new ArrayList<>();
    private ArrayList<Ability> unitAbilities = new ArrayList<>();
    private ArrayList<Requirement> applicableUpgrades = new ArrayList<>();
    private ArrayList<Effect> unitEffects = new ArrayList<>();

    private ArrayList<QueuedUnit> unitCreationQueue = new ArrayList<>();
    private ArrayList<QueuedUpgrade> upgradesQueue = new ArrayList<>();

    // unit state info
    private State state = null;

    // basic info
    private float x, y;
    private Rectangle rectangle;
    private int collisionSize = 4;
    private float scale = 1;
    private UnitType unitType;
    private Image modelImage, avatarImage, iconImage;
    private int imageWidth, imageHeight;
    private Color color = Color.white;
    private int currentHealth, maxHealth;
    private int armor;

    // creation info
    private int populationUsage = 1;
    private int creationTime = 400;
    private int creationCost = 100;

    // commands
    private Unit targetUnit;
    private AbilityType activeAbility;
    private Vector2i targetPoint;
    private boolean following, goingToBuild, building;
    private int moveSpeed;
    private boolean forcedMove;
    private Pathfinder pf;
    private ArrayList<Tile> path = new ArrayList<>();

    // miscellaneous
    private boolean selected, highlighted, fullyUpgraded;

    // attack
    private boolean aggressive = true;
    private boolean forcedAttack;
    private Projectile[] projectiles;
    private int projectileSpeed;
    private long lastAttackTime, lastAttackInitTime;
    private boolean attackBegan, attackReady, chasing;
    private int attackCooldown = 100, attackPoint = 100, attackBackswing = 100,
            attackRange = Consts.meleeRange;
    private int damage;
    private long lastTargetRequestTime;
    private int targetAcquisitionRange = 140;

    private TeamTag team = TeamTag.team1;

    public Unit(float x, float y) throws SlickException {
        modelImage = new Image("res/images/none.png");
        imageWidth = modelImage.getWidth();
        imageHeight = modelImage.getHeight();
        avatarImage = new Image("res/images/none.png");
        iconImage = new Image("res/images/none.png");

        this.x = x;
        this.y = y;
        this.rectangle = new Rectangle(x, y, imageWidth * scale, imageHeight * scale);
    }

    // RMB target is either ground or unit
    // if it's ground, move there no matter what
    // if it's enemy unit, attack it no matter what
    // if it's friendly unit, move to it (or interact with it)
    // if doing attack move (attack on ground): order move to location (x, y) and set
    // "aggressive" to true which will, every 500ms, set a message requesting a new target.
    // if target is found, order attack on it, keeping old (x,y) location remembered
    // if target is NOT found, do nothing; unit will just keep moving to (x,y)

    private boolean isAttackReady() {
        if (hasCommand(CommandType.attack)) {
            if (Sys.getTime() - lastAttackTime >= Math.max(attackBackswing, attackCooldown)) {
                if (!attackBegan) {
                    lastAttackInitTime = Sys.getTime();
                    attackBegan = true;
                }
                if (Sys.getTime() - lastAttackInitTime >= attackPoint) { // launching attack
                    return true;
                }
            }
        }
        return false;
    }

    public void attackTarget(Unit target) throws SlickException {
        if (target == this || target.getCurrentHealth() == 0) {
            return;
        }

        forcedAttack = true;
        forcedMove = false;
        targetUnit = target;
        targetPoint = null;

        // point -> ATTACK -> max(backswing, cooldown) -> point -> ATTACK

        // create projectiles
        if (attackRange > Consts.meleeRange) {
            if (Misc.getDistance(getCenterX(), getCenterY(), target.getCenterX(),
                    target.getCenterY()) <= attackRange) {
                if (attackReady) {
                    attackBegan = false;
                    lastAttackTime = Sys.getTime();
                    // create projectiles only if attack is ranged
                    for (int i = 0; i < projectiles.length; i++) {
                        if (projectiles[i] != null) {
                            continue;
                        }
                        // TODO make some projs not follow target
                        projectiles[i] = new Projectile(getCenterX(), getCenterY(),
                                (float) Math.atan2(target.getCenterY() - getCenterY(),
                                        target.getCenterX() - getCenterX()),
                                projectileSpeed);
                        projectiles[i].setTarget(target);
                        break;
                    }
                }
                // if out of range, but can't move, forget it
            } else if (moveSpeed == 0) {
                targetUnit = null;
            }
            // if melee, attack if within collision range
        } else {
            if (rectangle.intersects(target.getRectangle())) {
                if (attackReady) {
                    attackBegan = false;
                    lastAttackTime = Sys.getTime();
                    targetUnit.deltaCurrentHealth((int) -damage);
                    queuedMessage = new Message(MessageType.applyEffectsToAttackTarget, this);
                }
                // if out of range, but can't move, forget it
            } else if (moveSpeed == 0) {
                targetUnit = null;
            }
        }

    }

    public void walkTowardsUnit(Unit unit) {
        stopAllActions();
        forcedMove = true;
        forcedAttack = false;
        targetUnit = unit;
        targetPoint = null;
        aggressive = false;
    }

    public void walkTowardsPoint(int x, int y) {
        stopAllActions();
        forcedMove = false;
        forcedAttack = false;
        targetUnit = null;
        aggressive = false;

        if (targetPoint == null) {
            path.clear();
        }

        if (path.isEmpty()) {
            path = pf.getPath(this.x + Pathfinder.shiftX, this.y + Pathfinder.shiftY, x + Pathfinder.shiftX,
                    y + Pathfinder.shiftY);
            if (path.isEmpty()) {
                stopAllActions();
            } else {
                getNextTargetPoint();
            }
            // for (Tile p: path) {
            // System.out.print(p.i + ":" + p.j + ", ");
            // }
            // System.out.println();
        }
    }

    public void getNextTargetPoint() {
        if (path.size() > 0) {
            targetPoint = new Vector2i(
                    path.get(0).i * Consts.TILE_SIZE - Pathfinder.shiftX + Consts.TILE_SIZE / 2,
                    path.get(0).j * Consts.TILE_SIZE - Pathfinder.shiftY + Consts.TILE_SIZE / 2);
            path.remove(0);
        }
    }

    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

    private void attackMoveTo(int x, int y) {
        stopAllActions();
        forcedAttack = false;
        forcedMove = false;
        aggressive = true;
        targetPoint = new Vector2i(x, y);
    }

    public void update(int dt) throws SlickException {

        if (aggressive) {
            if (Sys.getTime() - lastTargetRequestTime >= Consts.updateTargetTime) {
                lastTargetRequestTime = Sys.getTime();
                queuedMessage = new Message(Message.MessageType.requestTarget, this);
            }
        }
        if (targetUnit != null && targetUnit.isDead()) {
            targetUnit = null;
            // queuedMessage = new Message(Message.MessageType.requestTarget, this);
        }

        if (attackRange > Consts.meleeRange) {
            updateProjectiles(dt);
        }

        attackReady = isAttackReady();
        if (targetUnit != null) {
            if (targetUnit.getTeamTag().isEnemyOf(team) || forcedAttack) {
                if (attackReady) {
                    attackTarget(targetUnit);
                }
            }
        }
        // stop if : reached ally unit | reached ground | within attack range of enemy unit
        // including some forced actions
        if (targetUnit != null) {
            if (forcedAttack || targetUnit.getTeamTag().isEnemyOf(team)) {
                if (targetUnit.getRectangle().intersects(rectangle) || Misc.getDistance(getCenterX(),
                        getCenterY(), targetUnit.getCenterX(), targetUnit.getCenterY()) <= attackRange) {
                    chasing = false;
                } else {
                    chasing = true;
                }
            } else if (forcedMove || !targetUnit.getTeamTag().isEnemyOf(team)) {
                if (rectangle.intersects(targetUnit.getRectangle())) {
                    following = false;
                    // if it got to the building site, start building
                    if (goingToBuild) {
                        startBuilding();
                    }
                } else {
                    following = true;
                }
            }
        } else if (targetPoint != null) {
            if (Misc.getDistance(targetPoint.getX(), targetPoint.getY(), x, y) <= 10 + Math.random() * 15) {
                if (path.isEmpty()) {
                    targetPoint = null;
                } else {
                    getNextTargetPoint();
                }
            }
        }

        if (targetUnit != null || targetPoint != null) {
            if (chasing || following || targetPoint != null) {
                float endPointX = 0;
                float endPointY = 0;
                if (targetUnit != null) {
                    endPointX = targetUnit.getCenterX();
                    endPointY = targetUnit.getCenterY();
                } else if (targetPoint != null) {
                    endPointX = targetPoint.getX();
                    endPointY = targetPoint.getY();
                }
                float angle = (float) Math.atan2(endPointY - y, endPointX - x);

                x += dt / 1000f * moveSpeed * Math.cos(angle);
                y += dt / 1000f * moveSpeed * Math.sin(angle);
                rectangle.setX(x);
                rectangle.setY(y);

            }
        }

        for (Iterator<QueuedUnit> iter = unitCreationQueue.iterator(); iter.hasNext();) {
            QueuedUnit queuedUnit = (QueuedUnit) iter.next();
            if (Sys.getTime() - queuedUnit.getInitTime() >= queuedUnit.getUnit().getCreationTime()) {
                if (hasUnitAbility(AbilityType.gatherPointAbility)) {
                    GatherPoint gp = (GatherPoint) getUnitAbility(AbilityType.gatherPointAbility);
                    if (gp.hasUnitTarget()) {
                        queuedUnit.getUnit().attackMoveTo(gp.getUnitTarget().getCenterX(),
                                gp.getUnitTarget().getCenterY());
                    } else {
                        queuedUnit.getUnit().attackMoveTo(gp.getGatherPoint().getX(),
                                gp.getGatherPoint().getY());
                    }
                }
                queuedMessage = new Message(Message.MessageType.createUnit, queuedUnit.getUnit());
                iter.remove();
                for (QueuedUnit queuedUnit1: unitCreationQueue) {
                    queuedUnit1.setInitTime(Sys.getTime());
                }
                break;
            }
        }
        for (Iterator<QueuedUpgrade> iter = upgradesQueue.iterator(); iter.hasNext();) {
            QueuedUpgrade queuedUpgrade = (QueuedUpgrade) iter.next();
            if (Sys.getTime() - queuedUpgrade.getInitTime() >= queuedUpgrade.getUpgrade().getCreationTime()) {
                queuedMessage = new Message(MessageType.research,
                        new Object[] { team, (UpgradeType) queuedUpgrade.getUpgrade().getUpgrade() });

                iter.remove();
                // removeAvailableUpgrade((UpgradeType) queuedUpgrade.getUpgrade().getUpgrade());
                for (QueuedUpgrade queuedUpgrade1: upgradesQueue) {
                    queuedUpgrade1.setInitTime(Sys.getTime());
                }
                break;
            }
        }

    }

    private void updateProjectiles(int dt) {
        for (int i = 0; i < projectiles.length; i++) {
            if (projectiles[i] == null) {
                continue;
            }
            projectiles[i].update(dt);
            if (projectiles[i].reachedTarget()) {
                if (projectiles[i].getTargetUnit() != null) {
                    projectiles[i].getTargetUnit().deltaCurrentHealth((int) -damage);
                }
                queuedMessage = new Message(MessageType.applyEffectsToAttackTarget, this);
                projectiles[i] = null;
                continue;
            }
        }
    }

    public void draw(Graphics g) throws SlickException {
        g.setColor(team.getColor());
        if (attackRange > Consts.meleeRange) {
            g.draw(new Ellipse(getCenterX(), getCenterY(), attackRange, attackRange));
        }
        g.drawImage(modelImage, x, y, x + imageWidth * scale, y + imageHeight * scale, 0, 0, imageWidth,
                imageHeight, highlighted ? color.addToCopy(new Color(-0.3f, 0.3f, -0.3f)) : color);

        g.fillRect(x, y, 10, 10);

        if (attackRange > Consts.meleeRange) {
            drawProjectiles(g);
        }

        if (selected) {
            g.setColor(Color.white);
            g.drawRoundRect(x, y, imageWidth * scale, imageHeight * scale, 10);
        }

        // TODO leave the boolean there
        if (selected && hasUnitAbility(AbilityType.gatherPointAbility)) {
            GatherPoint gp = (GatherPoint) getUnitAbility(AbilityType.gatherPointAbility);
            gp.draw(g);
        }

        g.setColor(Color.black);
        g.fill(new Rectangle(x, y - 20, rectangle.getWidth(), 5));
        g.setColor(new Color(1f - 1f * currentHealth / maxHealth, 1f * currentHealth / maxHealth, 0));
        g.fill(new Rectangle(x, y - 20, rectangle.getWidth() * currentHealth / maxHealth, 5));

        if (unitCreationQueue.size() > 0) {
            g.setColor(Color.orange);
            g.draw(new Rectangle(x, y - 30, 100, 15));
            g.fillRect(x, y - 30, 100f * (Sys.getTime() - unitCreationQueue.get(0).getInitTime())
                    / unitCreationQueue.get(0).getUnit().getCreationTime(), 15);
        }
        for (int i = 0; i < unitCreationQueue.size(); i++) {
            g.drawImage(unitCreationQueue.get(i).getUnit().getIconImage(), x + 35 * (i % 3),
                    y + 80 + 35 * (i / 3));
        }

        if (upgradesQueue.size() > 0) {
            g.setColor(Color.blue);
            g.draw(new Rectangle(x, y - 50, 100, 15));
            g.fillRect(x, y - 50, 100f * (Sys.getTime() - upgradesQueue.get(0).getInitTime())
                    / upgradesQueue.get(0).getUpgrade().getCreationTime(), 15);
        }

        for (int i = 0; i < upgradesQueue.size(); i++) {
            g.drawImage(((Upgrade) upgradesQueue.get(i).getUpgrade()).getIconImage(), x + 35 * (i % 3),
                    y + 80 + 35 * (i / 3));
        }

        highlighted = false;

        if (selected && moveSpeed > 0) {
            if (targetPoint != null) {
                g.fill(new Rectangle(targetPoint.getX() - 5, targetPoint.getY() - 5, 10, 10));
            } else if (targetUnit != null) {
                g.draw(targetUnit.getRectangle());
            }
        }

        for (Effect effect: unitEffects) {
            g.drawString(effect.getType() + "", x, y);
            g.drawImage(effect.getEffectImage(), x + rectangle.getWidth() / 2 - 16,
                    y + rectangle.getHeight() / 2 - 16);
        }

        if (selected) {
            pf.draw(g);
        }

    }

    public ArrayList<Requirement> getApplicableUpgrades() {
        return applicableUpgrades;
    }

    public void addApplicableUpgrade(Requirement upg) {
        if (!hasApplicableUpgrade(upg)) {
            applicableUpgrades.add(upg);
        }
    }

    public boolean hasApplicableUpgrade(Requirement upg) {
        for (Requirement r: applicableUpgrades) {
            if (r.getRequirement() == upg.getRequirement()) {
                return true;
            }
        }
        return false;
    }

    public void interactWithUnit(Unit target) throws SlickException {
        stopAllActions();
        if (team.isEnemyOf(target.getTeamTag())) {
            attackTarget(target);
        } else {
            if (target.hasUnitAbility(AbilityType.passiveUnitCargoAbility)) {
                PassiveUnitCargo puc = (PassiveUnitCargo) target
                        .getUnitAbility(AbilityType.passiveUnitCargoAbility);
                if (rectangle.intersects(target.getRectangle())) {
                    puc.loadUnit(this);
                    puc.callAllMethodsOnUnit(target);
                    queuedMessage = new Message(MessageType.hideUnit, this);
                }
            }
            walkTowardsUnit(target);
        }
    }

    public void stopAllActions() {
        if (goingToBuild && targetUnit != null) {
            queuedMessage = new Message(MessageType.clearBlueprint,
                    new int[] { (int) targetUnit.getX(), (int) targetUnit.getY() });
        }
        forcedAttack = false;
        forcedMove = false;
        goingToBuild = false;
        targetUnit = null;
        targetPoint = null;
    }

    private void drawProjectiles(Graphics g) {
        for (Projectile projectile: projectiles) {
            if (projectile == null) {
                continue;
            }
            projectile.draw(g);
        }

    }

    public void deltaX(float dx) {
        x += dx;
        if (targetPoint != null) {
            targetPoint.x += dx;
        }
        // gatherPointX += dx;
        rectangle.setX(x);

        if (attackRange > Consts.meleeRange) {
            deltaProjectilesX(dx);
        }
    }

    public void setMessage(Message message) {
        queuedMessage = message;
    }

    public void deltaY(float dy) {
        y += dy;
        if (targetPoint != null) {
            targetPoint.y += dy;
        }
        // gatherPointY += dy;
        rectangle.setY(y);
        if (attackRange > Consts.meleeRange) {
            deltaProjectilesY(dy);
        }
    }

    private void deltaProjectilesX(float dx) {
        for (Projectile projectile: projectiles) {
            if (projectile == null) {
                continue;
            }
            projectile.deltaX(dx);
        }
    }

    private void deltaProjectilesY(float dy) {
        for (Projectile projectile: projectiles) {
            if (projectile == null) {
                continue;
            }
            projectile.deltaY(dy);
        }
    }

    public ArrayList<Unit> getCreatableUnits() {
        return creatableUnits;
    }

    public ArrayList<Unit> getBuildableUnits() {
        return buildableUnits;
    }

    public ArrayList<Upgrade> getAvailableUpgrades() {
        return availableUpgrades;
    }

    public Upgrade getAvailableUpgrade(UpgradeType upgradeType) throws SlickException {
        for (Upgrade upg: availableUpgrades) {
            if (upg.getUpgrade() == upgradeType) {
                return upg;
            }
        }
        return null;
    }

    public Image getAvatarImage() {
        return avatarImage;
    }

    public Image getIconImage() {
        return iconImage;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public Image getModelImage() {
        return modelImage;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rect) {
        this.rectangle = rect;
    }

    public float getScale() {
        return scale;
    }

    public ArrayList<Effect> getUnitEffects() {
        return unitEffects;
    }

    public void clearEffect(EffectType effectType) {
        for (Iterator<Effect> iter = unitEffects.iterator(); iter.hasNext();) {
            EffectType type = (EffectType) iter.next().getType();
            if (type == effectType) {
                getUnitEffect(effectType).clearAllFromUnit(this);
                iter.remove();
                break;
            }
        }
    }

    public void setProjectileSpeed(int speed) {
        projectileSpeed = speed;
    }

    public void enqueueUnit(UnitType unitType) throws SlickException {
        Unit unit = null;
        for (Unit u: creatableUnits) {
            if (u.getType() == unitType) {
                if (unitCreationQueue.size() >= Consts.unitQueueCapacity) {
                    return;
                }
                unit = new UnitFactory().createUnit(unitType);
                unit.setTeamTag(team);
                unit.setX(getCenterX());
                unit.setY(getCenterY());
                unitCreationQueue.add(new QueuedUnit(unit, Sys.getTime()));
                return;
            }
        }
    }

    public void enqueueUpgrade(UpgradeType upgradeType) throws SlickException {
        Upgrade upgrade = null;
        for (Upgrade u: availableUpgrades) {
            if (u.getUpgrade().getClass() == UpgradeType.class) {
                if ((UpgradeType) u.getUpgrade() == upgradeType) {
                    if (unitCreationQueue.size() >= Consts.unitQueueCapacity) {
                        return;
                    }
                    upgrade = new Upgrade(u.getUpgrade());
                    upgradesQueue.add(new QueuedUpgrade(upgrade, Sys.getTime()));
                    removeAvailableUpgrade(upgradeType);
                    queuedMessage = new Message(MessageType.refreshSelection, null);
                    return;
                }
            }
        }
    }

    public int getArmor() {
        return armor;
    }

    public float getPhysicalDamageMultiplier() {
        return (float) (1 - 0.1f * armor / (1 + 0.1f * Math.abs(armor)));

    }

    public void affectBy(Effect effect) throws SlickException {
        if (!isAffectedBy(effect.getType())) {
            Effect newEffect;
            try {
                newEffect = effect.getClass().getConstructor((Class<?>[]) null).newInstance((Object[]) null);
                newEffect.refresh();
                newEffect.applyAllOnUnit(this);
                unitEffects.add(newEffect);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            getUnitEffect(effect.getType()).refresh();
        }
    }

    public boolean isAffectedBy(EffectType effectType) {
        for (Effect effect: unitEffects) {
            if (effect.getType() == effectType) {
                return true;
            }
        }
        return false;
    }

    public Effect getUnitEffect(EffectType effectType) {
        for (Effect effect: unitEffects) {
            if (effect.getType() == effectType) {
                return effect;
            }
        }
        return null;
    }

    public boolean hasUnitAbility(AbilityType abilityType) {
        for (Ability ability: unitAbilities) {
            if (ability.getType() == abilityType) {
                return true;
            }
        }
        return false;
    }

    public Ability getUnitAbility(AbilityType abilityType) {
        for (Ability ability: unitAbilities) {
            if (abilityType == ability.getType()) {
                return ability;
            }
        }
        return null;
    }

    public ArrayList<Command> getUnitCommands() {
        return unitCommands;
    }

    public UnitType getType() {
        return unitType;
    }

    public int getCenterX() {
        return (int) (x + rectangle.getWidth() / 2);
    }

    public int getCenterY() {
        return (int) (y + rectangle.getHeight() / 2);
    }

    public boolean hasCommand(CommandType commandType) {
        for (Command cmd: unitCommands) {
            if (cmd.getType() == commandType) {
                return true;
            }
        }
        return false;
    }

    public void deltaMaxHealth(int delta) {
        float healthPercent = (float) currentHealth / maxHealth;
        if (delta < 0) {
            maxHealth = Math.max(maxHealth + delta, 0);
        } else if (delta > 0) {
            maxHealth = Math.min(5000, currentHealth + delta);
        }
        currentHealth = (int) (maxHealth * healthPercent);
        if (isDead()) {
            clearAllEffects();
            stopAllActions();
        }
    }

    public void deltaCurrentHealth(int delta) {
        if (delta < 0) {
            currentHealth = Math.max(currentHealth + delta, 0);
        } else if (delta > 0) {
            currentHealth = Math.min(maxHealth, currentHealth + delta);
        }
        if (isDead()) {
            clearAllEffects();
            stopAllActions();
        }
    }

    public boolean isDead() {
        return currentHealth == 0;
    }

    public void clearAllEffects() {
        unitEffects.clear();
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setScale(float scale) {
        rectangle.setWidth(rectangle.getWidth() / this.scale * scale);
        rectangle.setHeight(rectangle.getHeight() / this.scale * scale);
        this.scale = scale;
    }

    public void setCenterX(int centerX) {
        x = centerX - rectangle.getWidth() / 2;
    }

    public void setCenterY(int centerY) {
        y = centerY - rectangle.getHeight() / 2;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void removeUnitCommand(CommandType commandType) {
        for (Command cmd: unitCommands) {
            if (cmd.getType() == commandType) {
                unitCommands.remove(cmd);
            }
        }
    }

    public void removeCreatableUnit(UnitType unitType) {
        for (Unit unit: creatableUnits) {
            if (unit.getType() == unitType) {
                creatableUnits.remove(unit);
            }
        }
    }

    public void removeBuildableUnit(UnitType unitType) {
        for (Unit unit: buildableUnits) {
            if (unit.getType() == unitType) {
                buildableUnits.remove(unit);
            }
        }
    }

    public boolean hasAvailableUpgrade(UpgradeType upgradeType) {
        for (Upgrade upg: availableUpgrades) {
            if (upg.getUpgrade() == upgradeType) {
                return true;
            }
        }
        return false;
    }

    public void removeAvailableUpgrade(UpgradeType upgradeType) {
        for (Upgrade upg: availableUpgrades) {
            if (upg.getUpgrade() == upgradeType) {
                availableUpgrades.remove(upg);
                break;
            }
        }
    }

    public ArrayList<Ability> getUnitAbilities() {
        return unitAbilities;
    }

    public void addAvailableUpgrade(Upgrade upgrade) {
        if (!availableUpgrades.contains(upgrade.getUpgrade())) {
            availableUpgrades.add(upgrade);
        }
    }

    public TeamTag getTeamTag() {
        return team;
    }

    public int getPopulationUsage() {
        return populationUsage;
    }

    public void addCreatableUnit(UnitType creatableUnit, boolean direct) throws SlickException {
        if (unitType.isUnitClass(UnitClass.structure)) {
            addUnitAbility(new GatherPoint(this));
            ((GatherPoint) getUnitAbility(AbilityType.gatherPointAbility)).setGatherPoint(new Vector2i(
                    (int) (x + rectangle.getWidth() / 2), (int) (y + rectangle.getHeight() + 20)));
        }
        if (!direct) {
            addUnitCommand(new Command(CommandType.build));
        }
        // TODO
        creatableUnits.add(World.uf.createUnit(creatableUnit));
    }

    public void addBuildableUnit(UnitType buildableUnit, boolean direct) throws SlickException {
        if (unitType.isUnitClass(UnitClass.structure)) {
            addUnitAbility(new GatherPoint(this));
            ((GatherPoint) getUnitAbility(AbilityType.gatherPointAbility))
                    .setGatherPoint(new Vector2i((int) (x + rectangle.getWidth() / 2), (int) (y + 20)));

        }
        if (!direct) {
            addUnitCommand(new Command(CommandType.build));
        }
        // TODO
        buildableUnits.add(World.uf.createUnit(buildableUnit));
    }

    public void removeUnitAbility(AbilityType abilityType) {
        for (Iterator<Ability> iter = unitAbilities.iterator(); iter.hasNext();) {
            if (iter.next().getType() == abilityType) {
                iter.remove();
            }
        }
    }

    public void addUnitAbility(Ability ability) throws SlickException {
        if (!hasUnitAbility(ability.getType())) {
            unitAbilities.add(ability);
            // Ability newAbility;
            // // TODO create a parameterType getter for constructor parameter types
            // try {
            // newAbility = ability.getClass().getConstructor(new Class[] { Unit.class })
            // .newInstance(new Object[] { this });
            // unitAbilities.add(newAbility);
            // } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
            // | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            // e.printStackTrace();
            // } finally {
            // // pass
            // }
        }
    }

    public int getCreationTime() {
        return creationTime;
    }

    public void addUnitCommand(Command command) throws SlickException {
        if (!hasCommand(command.getType())) {
            unitCommands.add(new Command(command.getType()));
            if (command.getType() == CommandType.move || command.getType() == CommandType.attack) {
                addUnitCommand(new Command(CommandType.stop));
            }
        }
    }

    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    public void setAttackRange(int attackRange) throws SlickException {
        this.attackRange = attackRange;

        if (attackRange > Consts.meleeRange) {
            projectiles = new Projectile[Consts.maxProjectiles];
        } else {
            projectiles = null;
        }
    }

    public int getDamage() {
        return damage;
    }

    public void deltaDamage(int delta) {
        if (delta < 0) {
            damage = Math.max(damage + delta, 0);
        } else if (delta > 0) {
            damage = Math.min(1000, damage + delta);
        }
        if (damage == 0) {
            // hide damage GUI
        }
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public void deltaMoveSpeed(int delta) {
        if (delta < 0) {
            moveSpeed = Math.max(moveSpeed + delta, 100);
        } else if (delta > 0) {
            moveSpeed = Math.min(2000, moveSpeed + delta);
        }
        if (moveSpeed == 0) {

            // don't show number
            // clearAllEffects();
            // stopAllActions();
        }
    }

    public void setTeamTag(TeamTag team) {
        this.team = team;
    }

    public void setX(float x) {
        this.x = x;
        rectangle.setX(x);
    }

    public void setY(float y) {
        this.y = y;
        rectangle.setY(y);
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getTargetAcquisitionRange() {
        return targetAcquisitionRange;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public void setModelImage(Image modelImage) {
        this.modelImage = modelImage;
        imageWidth = modelImage.getWidth();
        imageHeight = modelImage.getHeight();

        rectangle.setWidth(imageWidth);
        rectangle.setHeight(imageHeight);
    }

    public void resetModelImage() throws SlickException {
        setModelImage(unitType.getModelImage());
    }

    public void resetAvatarImage() throws SlickException {
        setAvatarImage(unitType.getAvatarImage());
    }

    public void setAvatarImage(Image avatarImage) {
        this.avatarImage = avatarImage;
    }

    public ArrayList<QueuedUnit> getUnitCreationQueue() {
        return unitCreationQueue;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void setHealth(int currentHealth, int maxHealth) {
        this.currentHealth = Math.max(currentHealth, maxHealth);
        this.maxHealth = maxHealth;
    }

    public Unit getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(Unit targetUnit) {
        this.targetUnit = targetUnit;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getUnitTargetPriority() {
        return (int) (10000 * damage / (attackCooldown / 1000f * (armor + 1) * currentHealth));
    }

    public void setMoveSpeed(int moveSpeed) throws SlickException {
        this.moveSpeed = moveSpeed;
        if (moveSpeed > 0) {
            addUnitCommand(new Command(CommandType.move));
        } else {
            removeUnitCommand(CommandType.move);
        }
    }

    public void setDamage(int damage) throws SlickException {
        this.damage = damage;
        if (damage > 0) {
            addUnitCommand(new Command(CommandType.attack));
        } else {
            removeUnitCommand(CommandType.attack);
        }
    }

    public void setIconImage(Image iconImage) {
        this.iconImage = iconImage;
    }

    public int getCreationCost() {
        return creationCost;
    }

    public void setAttackPoint(int attackPoint) {
        this.attackPoint = attackPoint;
    }

    public void setAttackBackswing(int attackBackswing) {
        this.attackBackswing = attackBackswing;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isFullyUpgraded() {
        return fullyUpgraded;
    }

    public void deltaArmor(int delta) {
        armor += delta;
    }

    public int getCollisionSize() {
        return collisionSize;
    }

    public void setFullyUpgraded(boolean fullyUpgraded) {
        this.fullyUpgraded = fullyUpgraded;
    }

    public void setPopulationUsage(int populationUsage) {
        this.populationUsage = populationUsage;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void executeCurrentActionAt(int x, int y) {
        if (state == null) {
            return;
        }

        switch (state) {
        case ability:
            Ability abi = getUnitAbility(activeAbility);
            queuedMessage = new Message(MessageType.applyEffectToUnitsInRadius,
                    new Object[] { this, abi, abi.getRange(), new Point(x, y) });
            break;
        case attack:
            attackMoveTo(x, y);
            break;
        case move:
            walkTowardsPoint(x, y);
            break;
        case build:
            walkTowardsUnit(targetUnit);
            queuedMessage = new Message(MessageType.showBlueprint, new Blueprint(targetUnit));
            goingToBuild = true;
            break;
        }

        state = null;
    }

    public void setActiveAbility(AbilityType abilityType) {
        activeAbility = abilityType;
    }

    public boolean canBuildUnit(UnitType type) {
        for (Unit unit: buildableUnits) {
            if (unit.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public boolean canCreateUnit(UnitType type) {
        for (Unit unit: creatableUnits) {
            if (unit.getType() == type) {
                return true;
            }
        }
        return false;
    }

    private void startBuilding() {
        // TODO create different building styles
        building = true;
        queuedMessage = new Message(MessageType.createBuilding, targetUnit);
        goingToBuild = false;
    }

    public AbilityType getActiveAbility() {
        return activeAbility;
    }

    public void executeCurrentActionAt(Unit target) throws SlickException {
        if (state == null) {
            return;
        }

        switch (state) {
        case ability:
            Ability abi = getUnitAbility(activeAbility);
            if (UnitClass.canSourceAffectTarget(team, abi.getAffectedUnitClasses(), target.getTeamTag(),
                    target.getType().getUnitClasses())) {
                abi.callAllMethodsOnUnit(target);
            }
            Effect eff = abi.getEffect();
            EffectHandler.applyEffectToUnitByUnit(this, target, eff);
            break;
        case attack:
            attackTarget(target);
            break;
        case move:
            walkTowardsUnit(target);
            break;
        case build:
            // walkTowardsUnit(targetUnit);
            // queuedMessage = new Message(MessageType.showBlueprint, new Blueprint(targetUnit));
            // goingToBuild = true;
            break;
        }

        state = null;
    }

    public State getState() {
        return state;
    }

    public void setGatherPoint(int x, int y) {
        if (hasUnitAbility(AbilityType.gatherPointAbility)) {
            ((GatherPoint) getUnitAbility(AbilityType.gatherPointAbility)).setGatherPoint(new Vector2i(x, y));
        }
    }

    public void setGatherPoint(Unit gatherUnit) {
        if (hasUnitAbility(AbilityType.gatherPointAbility)) {
            ((GatherPoint) getUnitAbility(AbilityType.gatherPointAbility)).setTargetUnit(gatherUnit);
        }
    }

    public void setPathfinder(Pathfinder pf) {
        this.pf = pf;
    }
}
