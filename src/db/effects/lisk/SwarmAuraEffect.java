package db.effects.lisk;

import org.newdawn.slick.SlickException;

import db.effects.Effect;
import enums.EffectType;
import enums.UnitClass;

public class SwarmAuraEffect extends Effect {
    private int bonusMoveSpeed = 10;

    public SwarmAuraEffect() throws SlickException {
        super(EffectType.swarmEffect,
                new UnitClass[] { UnitClass.organic, UnitClass.ground, UnitClass.friend });

        addApplyingMethod("deltaMoveSpeed", new Class[] { int.class }, new Object[] { bonusMoveSpeed });

        addClearingMethod("deltaMoveSpeed", new Class[] { int.class }, new Object[] { -bonusMoveSpeed });
    }

}
