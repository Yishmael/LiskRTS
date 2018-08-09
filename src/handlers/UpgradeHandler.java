package handlers;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.Upgrade;
import db.abilities.Ability;
import db.abilities.Ability.AbilityTargetType;
import gameplay.Requirement;

public class UpgradeHandler {

    public static void applyUpgradeToUnit(Unit unit, Object completedThing) throws SlickException {
        if (unit.isFullyUpgraded()) {
            return;
        }

        boolean fullyUpgraded = true;

        // unlock upgrades
        for (Upgrade upgrade: unit.getAvailableUpgrades()) {
            if (!upgrade.isEnabled()) {
                upgrade.meetRequirement(new Requirement(completedThing));
                if (upgrade.isEnabled()) {
                    if (unit.hasApplicableUpgrade(new Requirement(completedThing))) {
                        upgrade.callAllMethodsOnUnit(unit);
                    }
                } else {
                    // maybe without this
                    fullyUpgraded = false;
                }
            }
        }

        // unlock abilities
        for (Ability ability: unit.getUnitAbilities()) {
            if (!ability.isEnabled()) {
                ability.meetRequirement(new Requirement(completedThing));
                if (ability.isEnabled()
                        && ability.getType().getAbilityTargetType() == AbilityTargetType.none) {
                    ability.callAllMethodsOnUnit(unit);
                } else {
                    fullyUpgraded = false;
                }
            }
        }

        // apply 'invisible' upgrades (attributes change)
        for (Requirement req: unit.getApplicableUpgrades()) {
            if (req.getRequirement() == completedThing) {
                if (!req.isMet()) {
                    req.setMet(true);
                    new Upgrade(req.getRequirement()).callAllMethodsOnUnit(unit);
                    // System.out.println(unit.getType() + " upgraded by " + upgradeType);
                }
            } else if (!req.isMet()) {
                fullyUpgraded = false;
            }
        }

        unit.setFullyUpgraded(fullyUpgraded);

        // unlock creatable units
        for (Unit creatableUnit: unit.getCreatableUnits()) {
            for (Requirement req: creatableUnit.getRequirements()) {
                if (req.getRequirement() == completedThing) {
                    if (!req.isMet()) {
                        req.setMet(true);
                        // System.out.println(unit.getType() + " upgraded by " + upgradeType);
                    }
                }
            }
        }
        // unlock buildable structures
        for (Unit buildableUnit: unit.getBuildableUnits()) {
            for (Requirement req: buildableUnit.getRequirements()) {
                if (req.getRequirement() == completedThing) {
                    if (!req.isMet()) {
                        req.setMet(true);
                        // System.out.println(unit.getType() + " upgraded by " + upgradeType);
                    }
                }
            }
        }

        if (fullyUpgraded) {
            // System.out.println(unit.getType() + " is fully upgraded U");
        }
    }
}
