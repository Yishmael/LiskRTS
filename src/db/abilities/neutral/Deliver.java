package db.abilities.neutral;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import enums.AbilityType;

public class Deliver extends Ability {

    private int amount;

    public Deliver(Unit owner) throws SlickException {
        super(AbilityType.deliverDirtAbility);

        setIconPosition(1, 2);

    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
