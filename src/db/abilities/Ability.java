package db.abilities;

import org.lwjgl.Sys;
import org.newdawn.slick.SlickException;

import db.Interactive;
import db.Unit;
import db.effects.Effect;
import enums.AbilityType;
import enums.UnitClass;

public abstract class Ability extends Interactive {
    public enum AbilityTargetType {
        unitTarget, groundTarget, noTarget, none;
    }

    private AbilityType abilityType;
    protected Effect effect;
    protected Unit spawnedUnit;
    private long lastCastTime;
    protected int duration, cooldown, range;
    protected UnitClass[] affectedUnitClasses = new UnitClass[] {};

    public Ability(AbilityType abilityType) throws SlickException {
        this.abilityType = abilityType;

        setHoverText(abilityType.getHoverInfoText());
    }

    public AbilityType getType() {
        return abilityType;
    }

    public boolean isReady() {
        return Sys.getTime() - lastCastTime >= getCooldown();
    }

    public void setLastCastTime(long newTime) {
        lastCastTime = newTime;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getDuration() {
        return duration;
    }

    public int getRange() {
        return range;
    }

    public boolean canAffectUnitClass(UnitClass unitClass) {
        for (UnitClass uc: affectedUnitClasses) {
            if (uc == unitClass) {
                return true;
            }
        }
        return false;
    }

    public UnitClass[] getAffectedUnitClasses() {
        return affectedUnitClasses;
    }

    public Effect getEffect() {
        return effect;
    }

    public Unit getSpawnedUnit() {
        return spawnedUnit;
    }

}
