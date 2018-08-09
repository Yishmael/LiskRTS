package db.effects.lisk;

import org.newdawn.slick.SlickException;

import db.effects.Effect;
import enums.EffectType;
import enums.UnitClass;

public class BurningGroundEffect extends Effect {

    private int dps = 15;

    public BurningGroundEffect() throws SlickException {
        super(EffectType.burningGroundEffect,
                new UnitClass[] { UnitClass.organic, UnitClass.ground, UnitClass.enemy });

        addTickMethods("deltaCurrentHealth", new Class[] { int.class }, new Object[] { -dps });

        hasTicks = true;
        duration = 3000;

    }

}
