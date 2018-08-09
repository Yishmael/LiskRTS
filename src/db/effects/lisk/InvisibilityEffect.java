package db.effects.lisk;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

import db.effects.Effect;
import enums.EffectType;
import enums.UnitClass;

public class InvisibilityEffect extends Effect {

    public InvisibilityEffect() throws SlickException {

        super(EffectType.invisibilityEffect, new UnitClass[] { UnitClass.self });

        addApplyingMethod("setColor", new Class[] { Color.class }, new Object[] { new Color(1, 1, 1, 0.4f) });
        addClearingMethod("setColor", new Class[] { Color.class }, new Object[] { new Color(1, 1, 1, 1f) });
    }

}
