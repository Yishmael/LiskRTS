package db.abilities.neutral;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import encaps.Message;
import encaps.Message.MessageType;
import enums.AbilityType;
import enums.UnitClass;
import other.World;

public class UnloadUnit extends Ability {

    private Unit owner;

    public UnloadUnit(Unit owner) throws SlickException {
        super(AbilityType.unloadUnitAbility);
        this.owner = owner;
        setIconPosition(2, 2);

        affectedUnitClasses = new UnitClass[] { UnitClass.self, UnitClass.organic };

        // addCalledMethod("setMessage", new Class[] { Message.class },
        // new Object[] { new Message(MessageType.createUnit, owner) });

    }

    public void setUnit(Unit unit) throws SlickException {
        Unit newUnit = World.uf.createUnitAt(unit.getType(), owner.getX(), owner.getY());

        addCalledMethod("setMessage", new Class[] { Message.class },
                new Object[] { new Message(MessageType.createUnit, newUnit) });

        addCalledMethod("removeUnitAbility", new Class[] { AbilityType.class },
                new Object[] { AbilityType.unloadUnitAbility });
        addCalledMethod("addUnitAbility", new Class[] { Ability.class },
                new Object[] { new PassiveUnitCargo(owner) });

        unit = null;
    }

}
