package db.abilities.airon;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import db.effects.lisk.GroundNourishmentEffect;
import enums.AbilityType;
import enums.UnitClass;
import enums.UpgradeType;
import gameplay.Requirement;

public class GroundNourishment extends Ability {

    public GroundNourishment(Unit owner) throws SlickException {
        super(AbilityType.groundNourishmentAbility);

        range = 260;
        affectedUnitClasses = new UnitClass[] {};

        addRequirement(new Requirement(UpgradeType.groundNourishmentUpgrade));

        effect = new GroundNourishmentEffect();

        setIconPosition(2, 2);

    }

}
