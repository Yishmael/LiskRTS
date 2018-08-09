package db.effects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.lwjgl.Sys;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import db.Unit;
import encaps.MethodAndArgs;
import enums.EffectType;
import enums.UnitClass;

public abstract class Effect {
    private EffectType effectType;
    private ArrayList<MethodAndArgs> applyingMethods = new ArrayList<>();
    private ArrayList<MethodAndArgs> clearingMethods = new ArrayList<>();
    private ArrayList<MethodAndArgs> tickMethods = new ArrayList<>();
    protected int duration = 500;
    protected boolean hasTicks;
    private long lastApplyTime, lastTickTime, firstApplyTime;
    private Image effectImage, iconImage;
    private UnitClass[] affectedUnitClasses = new UnitClass[] {};

    public Effect(EffectType effectType, UnitClass[] affectedUnitClasses) throws SlickException {
        this.effectType = effectType;
        this.affectedUnitClasses = affectedUnitClasses;
        effectImage = new Image(effectType.getEffectImagePath());
        iconImage = new Image(effectType.getIconImagePath());
    }

    public EffectType getType() {
        return effectType;
    }

    public long getLastApplyTime() {
        return lastApplyTime;
    }

    public void refresh() {
        lastApplyTime = Sys.getTime();
        if (firstApplyTime == 0) {
            firstApplyTime = Sys.getTime();
        }
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Image getEffectImage() {
        return effectImage;
    }

    public Image getIconImage() {
        return iconImage;
    }

    public void setLastTickTime(long lastTickTime) {
        this.lastTickTime = lastTickTime;
    }

    public UnitClass[] getAffectedUnitClasses() {
        return affectedUnitClasses;
    }

    public void applyTicksOnUnit(Unit unit) {
        if (hasTicks) {
            if (Sys.getTime() - lastTickTime >= 1000 && Sys.getTime() - firstApplyTime >= 1000) {
                lastTickTime = Sys.getTime();
                for (MethodAndArgs mnr: tickMethods) {
                    try {
                        mnr.getMethod().invoke(unit, mnr.getArgs());
                    } catch (IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void addTickMethods(String methodName, @SuppressWarnings("rawtypes") Class[] parameterTypes,
            Object[] args) {
        try {
            Method m = Unit.class.getMethod(methodName, parameterTypes);
            tickMethods.add(new MethodAndArgs(m, args));
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

    }

    public void applyAllOnUnit(Unit unit) {
        for (MethodAndArgs mnr: applyingMethods) {
            try {
                mnr.getMethod().invoke(unit, mnr.getArgs());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void addApplyingMethod(String methodName, @SuppressWarnings("rawtypes") Class[] parameterTypes,
            Object[] args) {
        try {
            Method m = Unit.class.getMethod(methodName, parameterTypes);
            applyingMethods.add(new MethodAndArgs(m, args));
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

    }

    public void clearAllFromUnit(Unit unit) {
        for (MethodAndArgs mnr: clearingMethods) {
            try {
                mnr.getMethod().invoke(unit, mnr.getArgs());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void addClearingMethod(String methodName, @SuppressWarnings("rawtypes") Class[] parameterTypes,
            Object[] args) {
        try {

            Method m = Unit.class.getMethod(methodName, parameterTypes);
            clearingMethods.add(new MethodAndArgs(m, args));
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

    }

    public boolean hasTicks() {
        return hasTicks;
    }

}
