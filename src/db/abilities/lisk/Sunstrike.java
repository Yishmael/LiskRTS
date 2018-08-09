package db.abilities.lisk;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import db.effects.lisk.BurningGroundEffect;
import encaps.Message;
import encaps.Message.MessageType;
import enums.AbilityType;
import enums.UnitClass;

public class Sunstrike extends Ability {
    private int damage = 5;

    public Sunstrike(Unit owner) throws SlickException {
        super(AbilityType.sunstrikeAbility);

        affectedUnitClasses = new UnitClass[] { UnitClass.organic, UnitClass.enemy };

        addCalledMethod("deltaCurrentHealth", new Class[] { int.class }, new Object[] { -damage });

        effect = new BurningGroundEffect();

        addCalledMethod("setQueuedMessage", new Class[] { Message.class },
                new Object[] { new Message(MessageType.summonUnit, owner) });

        range = 700;

        setIconPosition(2, 1);
    }

}
