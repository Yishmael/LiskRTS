package db;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

import db.abilities.Ability;
import db.abilities.Ability.AbilityTargetType;
import db.abilities.airon.GroundNourishment;
import db.abilities.lisk.PassiveInvisibility;
import db.abilities.lisk.Sting;
import db.abilities.lisk.Sunstrike;
import db.abilities.lisk.SwarmAura;
import db.abilities.nagazsh.Hex;
import db.abilities.nagazsh.Meteor;
import db.abilities.neutral.Deliver;
import db.abilities.neutral.DirtSource;
import db.abilities.neutral.PassiveUnitCargo;
import enums.TeamTag;
import enums.UnitType;
import enums.UpgradeType;
import gameplay.Requirement;
import handlers.UpgradeHandler;
import zpathfinding.Pathfinder;

public class UnitFactory {

    private ArrayList<UpgradeType> completedUpgrades = new ArrayList<>();
    private ArrayList<UnitType> createdUnits = new ArrayList<>();

    public Unit createUnitAt(UnitType unitType, float x, float y) throws SlickException {
        Unit unit = new Unit(x, y);

        unit.setUnitType(unitType);
        unit.setModelImage(unitType.getModelImage());
        unit.setAvatarImage(unitType.getAvatarImage());
        unit.setIconImage(unitType.getIconImage());

        switch (unitType) {
        // Sminos
        case viper:
            unit.setHealth(100, 100);
            // unit.addUnitAbility(new GatherDirt());
            unit.setMoveSpeed(250);
            unit.setDamage(5);
            unit.setAttackCooldown(1400);
            unit.addApplicableUpgrade(new Requirement(UnitType.hive));
            unit.setIconPosition(0, 0);
            break;
        case predator:
            unit.setHealth(120, 120);
            unit.setDamage(17);
            unit.setAttackPoint(400);
            unit.setAttackCooldown(950);
            unit.setAttackBackswing(300);
            unit.setAttackRange(220);
            unit.setProjectileSpeed(200);
            unit.setMoveSpeed(250);
            unit.addUnitAbility(new Sting(unit));
            unit.addUnitAbility(new SwarmAura(unit));
            unit.addUnitAbility(new Sunstrike(unit));
            unit.addApplicableUpgrade(new Requirement(UpgradeType.scalesUpgrade));
            unit.setIconPosition(0, 1);
            break;
        case undevelopedEgg:
            break;
        case beetle:
        case treefeller:
            unit.setHealth(110, 110);
            unit.setDamage(13);
            unit.setAttackPoint(200);
            unit.setAttackCooldown(550);
            unit.setAttackBackswing(100);
            unit.setMoveSpeed(220);
            unit.addUnitAbility(new Sting(unit));
            unit.addApplicableUpgrade(new Requirement(UpgradeType.scalesUpgrade));
            unit.setIconPosition(0, 1);
            break;
        case bloodworm:
            unit.setHealth(50, 50);
            unit.setMoveSpeed(190);
            // unit.addUnitAbility(new Infest(unit));
            // unit.addApplicableUpgrade(new Requirement(UpgradeType.scalesUpgrade));
            unit.setIconPosition(0, 0);
            break;
        case mindeater:
            unit.setHealth(70, 70);
            unit.setMoveSpeed(190);
            // unit.addUnitAbility(new Infest(unit));
            // unit.addApplicableUpgrade(new Requirement(UpgradeType.scalesUpgrade));
            unit.setIconPosition(0, 1);
            break;
        case vespWarrior:
            break;
        case beevil:
            break;
        case dragonfly:
            break;
        case rhinob:
            break;
        case hercobet:
            break;
        case arach:
            break;
        case spire:
            unit.setScale(3);
            unit.setHealth(700, 700);
            unit.addUnitAbility(new Deliver(unit));
            unit.setIconPosition(0, 0);
            break;
        case hive:
            unit.setScale(1.6f);
            unit.setHealth(300, 300);
            unit.setPopulationUsage(-5);
            unit.setIconPosition(0, 1);
            break;
        case spiralHideout:
            unit.setScale(2.3f);
            unit.setHealth(330, 330);
            unit.addAvailableUpgrade(new Upgrade(UpgradeType.ectopicNerves));
            unit.addAvailableUpgrade(new Upgrade(UpgradeType.naturalHabitat));
            unit.setIconPosition(0, 2);
            break;
        case vespNest:
            unit.setScale(1.3f);
            unit.setHealth(330, 330);
            unit.setIconPosition(0, 3);
            break;
        case wormLair:
            unit.setScale(2);
            unit.setHealth(400, 400);
            unit.setIconPosition(1, 0);
            break;
        case honeycomb:
            unit.setScale(1.5f);
            unit.setHealth(300, 300);
            // unit.addUnitAbility(new Honeycomb());
            unit.setIconPosition(1, 1);
            break;
        case cave:
            unit.setScale(3);
            unit.setHealth(400, 400);
            for (UpgradeType upgradeType: UpgradeType.values()) {
                if (upgradeType == UpgradeType.ectopicNerves) {
                    break;
                }
                unit.addAvailableUpgrade(new Upgrade(upgradeType));
            }
            unit.addUnitAbility(new GroundNourishment(unit));
            unit.setIconPosition(1, 2);
            break;
        case hatchery:
            unit.setScale(3);
            unit.setHealth(500, 500);
            unit.addAvailableUpgrade(new Upgrade(UpgradeType.insectLegionUpgrade));
            unit.setIconPosition(1, 3);
            break;
        case buriedtrap:
            unit.setScale(2);
            unit.setHealth(100, 100);
            unit.addUnitAbility(new PassiveInvisibility(unit));
            unit.setIconPosition(2, 0);
            break;
        case colonyProtector:
            unit.setScale(1.2f);
            unit.setDamage(11);
            unit.setAttackCooldown(550);
            unit.setAttackRange(600);
            unit.setProjectileSpeed(1100);
            unit.setHealth(130, 130);
            unit.setIconPosition(2, 1);
            unit.addUnitAbility(new Sting(unit));
            break;
        case emptyCocoon:
            unit.setScale(1.6f);
            unit.addUnitAbility(new PassiveUnitCargo(unit));
            unit.setHealth(300, 300);
            unit.setMoveSpeed(100);
            unit.setIconPosition(2, 2);
            break;
        // Nagazsh
        case servant:
            unit.setHealth(100, 100);
            unit.setDamage(6);
            unit.setMoveSpeed(200);
            unit.setAttackCooldown(1700);
            break;
        case sadist:
            break;
        case summoner:
            unit.setHealth(300, 300);
            unit.setDamage(12);
            unit.setAttackCooldown(2000);
            unit.addUnitAbility(new Hex(unit));
            unit.addUnitAbility(new Meteor(unit));
            unit.setProjectileSpeed(190);
            unit.setAttackRange(120);
            unit.setMoveSpeed(220);
            break;
        case underlord:
            break;
        case miscreation:
            unit.setHealth(150, 150);
            unit.setDamage(17);
            unit.setAttackCooldown(1100);
            unit.setMoveSpeed(230);
            unit.setIconPosition(1, 0);
            break;
        case creeper:
            break;
        case shade:
            break;
        case naz:
            break;
        case stalker:
            unit.setScale(1.4f);
            unit.setHealth(650, 650);
            unit.setDamage(30);
            unit.setAttackCooldown(1540);
            unit.setProjectileSpeed(120);
            unit.setMoveSpeed(290);
            break;
        case engulfer:
            break;
        case darkShrine:
            unit.addApplicableUpgrade(new Requirement(UpgradeType.cultOfTheShadowsUpgrade));
            unit.setScale(1.5f);
            unit.setHealth(700, 700);
            unit.addUnitAbility(new Deliver(unit));
            unit.setIconPosition(0, 0);
            break;
        case blackPit:
            unit.setScale(1f);
            unit.setHealth(300, 300);
            unit.addUnitAbility(new Deliver(unit));
            unit.setIconPosition(0, 1);
            break;
        case voidTower:
            unit.setScale(2);
            unit.setDamage(25);
            unit.setAttackCooldown(2040);
            unit.setAttackRange(560);
            unit.setProjectileSpeed(400);
            unit.setHealth(170, 170);
            unit.setIconPosition(0, 2);
            break;
        // Airon
        case nymph:
        case wildDeer:
        case blackBear:
        case naturesShaman:
        case artemis:
        case dryad:
        case centaurArcher:
        case centaurWarrior:
        case satyrSpellcaster:
        case mountedSatyr:
        case entwiningRoot:
        case hollowTrunk:
        case dirtMound:
            unit.setTeamTag(TeamTag.friendly);
            unit.setScale(3);
            unit.setHealth(5000, 5000);
            unit.addUnitAbility(new DirtSource(unit));
            break;
        case dummy:
            unit.setHealth(10, 10);
            break;
        default:
            throw new SlickException("Invalid unit name: " + unitType);
        }

        for (Ability ability: unit.getUnitAbilities()) {
            if (ability.isEnabled() && ability.getType().getAbilityTargetType() == AbilityTargetType.none) {
                ability.callAllMethodsOnUnit(unit);
            }
        }
        for (UpgradeType upgradeType: completedUpgrades) {
            UpgradeHandler.applyUpgradeToUnit(unit, upgradeType);
            if (unit.hasAvailableUpgrade(upgradeType)) {
                unit.removeAvailableUpgrade(upgradeType);
            }
        }

        unit.setHoverText(unit.getType().getHoverInfoText());

        unit.setPathfinder(new Pathfinder());

        return unit;

    }

    public void addCreatableUnitsToUnit(Unit unit) throws SlickException {
        switch (unit.getType()) {
        case viper:
            // unit.addBuildableUnit(UnitType.spire, false);
            // unit.addBuildableUnit(UnitType.hive, false);
            // unit.addBuildableUnit(UnitType.spiralHideout, false);
            // unit.addBuildableUnit(UnitType.buriedtrap, false);
            // unit.addBuildableUnit(UnitType.hatchery, false);
            for (int i = UnitType.spire.ordinal(); i < UnitType.servant.ordinal(); i++) {
                unit.addBuildableUnit(UnitType.values()[i], false);
            }
            break;
        case spire:
            unit.addCreatableUnit(UnitType.viper, true);
            unit.addCreatableUnit(UnitType.predator, true);
            unit.addCreatableUnit(UnitType.emptyCocoon, true);
            break;
        case spiralHideout:
            unit.addCreatableUnit(UnitType.treefeller, true);
            break;
        case vespNest:
            unit.addCreatableUnit(UnitType.vespWarrior, true);
            break;
        case wormLair:
            unit.addCreatableUnit(UnitType.bloodworm, true);
            unit.addCreatableUnit(UnitType.mindeater, true);
            break;
        case hatchery:
            unit.addCreatableUnit(UnitType.predator, true);
            break;
        case servant:
            unit.addBuildableUnit(UnitType.darkShrine, false);
            unit.addBuildableUnit(UnitType.blackPit, false);
            unit.addBuildableUnit(UnitType.voidTower, false);
            break;
        case darkShrine:
            unit.addCreatableUnit(UnitType.servant, true);
            unit.addCreatableUnit(UnitType.miscreation, true);
            break;
        case blackPit:
            unit.addCreatableUnit(UnitType.stalker, true);
            break;
        default:
            break;
        }
    }

    public Unit createUnit(UnitType unitType) throws SlickException {
        return createUnitAt(unitType, 0, 0);
    }

    public void addCompletedUpgrade(UpgradeType upgradeType) {
        if (!completedUpgrades.contains(upgradeType)) {
            completedUpgrades.add(upgradeType);
        }
    }

    public void addCreatedUnit(UnitType unitType) {
        if (!createdUnits.contains(unitType)) {
            createdUnits.add(unitType);
        }
    }

    public ArrayList<UnitType> getCreatedUnits() {
        return createdUnits;
    }

    public ArrayList<UpgradeType> getCompletedUpgrades() {
        return completedUpgrades;
    }

}
