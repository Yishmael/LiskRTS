package db.abilities.neutral;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import enums.AbilityType;
import enums.UnitClass;

public class Gather extends Ability {

    private int capacity = 55;

    public Gather(Unit owner) throws SlickException {
        super(AbilityType.gatherAbility);

        affectedUnitClasses = new UnitClass[] { UnitClass.resource };

        range = 50;

        addCalledMethod("removeUnitAbility", new Class[] { Ability.class },
                new Object[] { AbilityType.gatherAbility });
        addCalledMethod("addUnitAbility", new Class[] { Ability.class }, new Object[] { new Deliver(owner) });

        setIconPosition(1, 2);
    }

    public int getCapacity() {
        return capacity;
    }
}
