package db.effects.lisk;

import org.newdawn.slick.SlickException;

import db.effects.Effect;
import enums.EffectType;
import enums.UnitClass;

public class StingEffect extends Effect {

    public StingEffect() throws SlickException {
        super(EffectType.stingEffect, new UnitClass[] { UnitClass.organic, UnitClass.enemy });

        addTickMethods("deltaCurrentHealth", new Class[] { int.class }, new Object[] { -4 });

        hasTicks = true;
        duration = 3000;

    }

}
