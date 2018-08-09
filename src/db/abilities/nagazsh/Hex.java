package db.abilities.nagazsh;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import db.effects.nagazsh.HexEffect;
import enums.AbilityType;
import enums.UnitClass;

public class Hex extends Ability {

    private int bonusMoveSpeed = 15;

    public Hex(Unit owner) throws SlickException {
        super(AbilityType.hexAbility);

        affectedUnitClasses = new UnitClass[] { UnitClass.organic, UnitClass.enemy };

        effect = new HexEffect();

        // addCalledMethod("affectBy", new Class[] { Effect.class }, new Object[] { new HexEffect()
        // });

        range = 120;

        setIconPosition(1, 1);
    }

    public int getBonusMoveSpeed() {
        return bonusMoveSpeed;
    }

}
