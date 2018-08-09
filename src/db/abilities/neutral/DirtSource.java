package db.abilities.neutral;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import enums.AbilityType;

public class DirtSource extends Ability {

    private int maxDirt = 50000;
    private int dirt = maxDirt;

    public DirtSource(Unit owner) throws SlickException {
        super(AbilityType.dirtSourceAbility);

        setIconPosition(1, 3);
    }

    public int getMaxCapacity() {
        return maxDirt;
    }

    public int getDirt() {
        return dirt;
    }

    public int gatherDirt(int amount) {
        // if you can, take amount
        if (dirt - amount >= 0) {
            dirt -= amount;
            return amount;
            // else take whatever is remaining
        } else {
            int change = dirt;
            dirt = 0;
            return change;
        }
    }

}
