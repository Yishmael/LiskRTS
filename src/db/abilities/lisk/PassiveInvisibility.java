package db.abilities.lisk;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import db.effects.lisk.InvisibilityEffect;
import enums.AbilityType;
import enums.UnitClass;

public class PassiveInvisibility extends Ability {

    public PassiveInvisibility(Unit owner) throws SlickException {
        super(AbilityType.passiveInvisibility);

        // TODO fix stack overflow problem
        affectedUnitClasses = new UnitClass[] { UnitClass.self };

        effect = new InvisibilityEffect();

        setIconPosition(1, 2);
    }
}
