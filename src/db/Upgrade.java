package db;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import enums.UnitType;
import enums.UpgradeType;
import gameplay.Requirement;

public class Upgrade extends Interactive {
    private Object upgrade;

    public Upgrade(Object upgrade) throws SlickException {
        this.upgrade = upgrade;

        if (upgrade.getClass() == UpgradeType.class) {
            switch ((UpgradeType) upgrade) {
            case scalesUpgrade:
                addRequirement(new Requirement(UpgradeType.venomUpgrade));
                addCalledMethod("deltaArmor", new Class[] { int.class }, new Object[] { 5 });
                setIconPosition(0, 0);
                break;
            case cultOfTheShadowsUpgrade:
                addCalledMethod("addCreatableUnit", new Class[] { UnitType.class, boolean.class },
                        new Object[] { UnitType.summoner, true });
                setIconPosition(0, 1);
                break;
            case groundNourishmentUpgrade:
                setIconPosition(0, 2);
                break;
            case venomUpgrade:
                setIconPosition(0, 3);
                addCalledMethod("deltaDamage", new Class[] { int.class }, new Object[] { 20 });
                break;
            case enhancedHemolymph:
                setIconPosition(1, 0);
                addCalledMethod("deltaMoveSpeed", new Class[] { int.class }, new Object[] { 15 });
                break;
            case lethalDose:
                setIconPosition(1, 1);
                break;
            case dexterity:
                setIconPosition(1, 2);
                break;
            case ectopicNerves:
                setIconPosition(1, 3);
                break;
            case naturalHabitat:
                setIconPosition(2, 0);
                break;
            case termiteColonyUpgrade:
                setIconPosition(2, 1);
                break;
            case insectLegionUpgrade:
                setIconPosition(2, 2);
                break;
            default:
                throw new SlickException("Unknown upgrade: " + (UpgradeType) upgrade);
            }
            setHoverText(((UpgradeType) upgrade).getHoverInfoText());
            setIconImage(new Image(((UpgradeType) upgrade).getIconImagePath()));

        } else if (upgrade.getClass() == UnitType.class) {
            switch ((UnitType) upgrade) {
            case hive:
                break;
            default:
                throw new SlickException("Unknown unit: " + (UnitType) upgrade);
            }
            setHoverText(((UnitType) upgrade).getHoverInfoText());
        }
    }

    public Object getUpgrade() {
        return upgrade;
    }

    public int getCreationTime() {
        return 1000;
    }
}
