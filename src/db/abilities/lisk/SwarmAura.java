package db.abilities.lisk;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import db.effects.lisk.SwarmAuraEffect;
import enums.AbilityType;
import enums.UnitClass;
import enums.UpgradeType;
import gameplay.Requirement;

public class SwarmAura extends Ability {

    private int bonusMoveSpeed = 15;

    public SwarmAura(Unit owner) throws SlickException {
        super(AbilityType.swarmAuraAbility);

        affectedUnitClasses = new UnitClass[] { UnitClass.self };

        effect = new SwarmAuraEffect();

        addCalledMethod("deltaMoveSpeed", new Class[] { int.class }, new Object[] { bonusMoveSpeed });

        addRequirement(new Requirement(UpgradeType.insectLegionUpgrade));

        range = 120;

        setIconPosition(1, 0);
    }

}
