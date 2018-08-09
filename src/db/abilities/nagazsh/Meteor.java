package db.abilities.nagazsh;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import db.effects.lisk.BurningGroundEffect;
import enums.AbilityType;
import enums.UnitClass;

public class Meteor extends Ability {
    private int damage = 40;

    public Meteor(Unit owner) throws SlickException {
        super(AbilityType.meteorAbility);

        affectedUnitClasses = new UnitClass[] { UnitClass.enemy, UnitClass.ground };

        addCalledMethod("deltaCurrentHealth", new Class[] { int.class }, new Object[] { -damage });

        effect = new BurningGroundEffect();

        range = 100;

        setIconPosition(2, 1);
    }

}
