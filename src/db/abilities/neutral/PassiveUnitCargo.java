package db.abilities.neutral;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import enums.AbilityType;
import enums.UnitClass;

public class PassiveUnitCargo extends Ability {

    private Unit unit;

    private Unit owner;

    public PassiveUnitCargo(Unit owner) throws SlickException {
        super(AbilityType.passiveUnitCargoAbility);
        this.owner = owner;

        affectedUnitClasses = new UnitClass[] { UnitClass.organic, UnitClass.friend };

        setIconPosition(2, 1);

    }

    public Unit getUnit() {
        return unit;
    }

    public void loadUnit(Unit unit) throws SlickException {
        this.unit = unit;
        System.out.println("Loaded " + unit.getType().getName());

        addCalledMethod("deltaCurrentHealth", new Class[] { int.class }, new Object[] { -100 });

        addCalledMethod("removeUnitAbility", new Class[] { AbilityType.class },
                new Object[] { AbilityType.passiveUnitCargoAbility });

        UnloadUnit ul = new UnloadUnit(owner);
        ul.setUnit(unit);
        addCalledMethod("addUnitAbility", new Class[] { Ability.class }, new Object[] { ul });
    }

}
