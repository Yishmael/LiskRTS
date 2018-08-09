package db.abilities.lisk;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import enums.AbilityType;
import enums.UnitClass;

public class Rush extends Ability {
    private int bonusMoveSpeed;

    public Rush(Unit owner) throws SlickException {
        super(AbilityType.rushAbility);

        bonusMoveSpeed = 15;

        // effect = new Effect(EffectType.noneEffect, new UnitClass[] {});

        // addRequirement(new Requirement(UpgradeType.enhancedGenetics));

        addCalledMethod("deltaMoveSpeed", new Class[] { int.class }, new Object[] { bonusMoveSpeed });

        affectedUnitClasses = new UnitClass[] { UnitClass.self, UnitClass.organic };

        setIconPosition(2, 0);
    }

}
