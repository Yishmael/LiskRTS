package db.effects.lisk;

import org.newdawn.slick.SlickException;

import db.effects.Effect;
import enums.EffectType;
import enums.UnitClass;

public class GroundNourishmentEffect extends Effect {
    private int bonusHealth = 123;

    public GroundNourishmentEffect() throws SlickException {
        super(EffectType.groundNourishmentEffect, new UnitClass[] { UnitClass.friend, UnitClass.structure });

        addApplyingMethod("deltaMaxHealth", new Class[] { int.class }, new Object[] { bonusHealth });

        addClearingMethod("deltaMaxHealth", new Class[] { int.class }, new Object[] { -bonusHealth });
    }

}
