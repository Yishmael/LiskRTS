package db.abilities.lisk;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import db.effects.lisk.StingEffect;
import enums.AbilityType;
import enums.UnitClass;
import enums.UpgradeType;
import gameplay.Requirement;

public class Sting extends Ability {

    public Sting(Unit owner) throws SlickException {
        super(AbilityType.stingAbility);

        affectedUnitClasses = new UnitClass[] { UnitClass.organic, UnitClass.self };
        effect = new StingEffect();

        addRequirement(new Requirement(UpgradeType.venomUpgrade));

        addCalledMethod("deltaDamage", new Class[] { int.class }, new Object[] { 3 });

        setIconPosition(1, 1);
    }

}
