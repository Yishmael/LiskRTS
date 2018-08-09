package enums;

import db.abilities.Ability.AbilityTargetType;

public enum AbilityType {
    // Sminos
    stingAbility(
            "Sting",
            AbilityTargetType.none,
            "Deals damage over time to enemies",
            "res/images/races/lisk/abilities/icons/btn_sting.png"),
    rushAbility(
            "Rush",
            AbilityTargetType.noTarget,
            "Increase your unit's movement speed",
            "res/images/races/lisk/abilities/icons/btn_rush.png"),
    swarmAuraAbility(
            "Swarm Aura",
            AbilityTargetType.none,
            "Increases nearby units' movement speed",
            "res/images/races/lisk/abilities/icons/btn_swarm.png"),
    sunstrikeAbility(
            "Sunstrike",
            AbilityTargetType.unitTarget,
            "Strikes the target ground point with the power of the sun",
            "res/images/races/lisk/abilities/icons/btn_sunstrike.png"),
    burningGroundAura(
            "Burning ground",
            AbilityTargetType.none,
            "Burns nearby units for fire damage",
            "res/images/races/lisk/abilities/icons/btn_burningground.png"),
    passiveInvisibility(
            "Passive invisibility",
            AbilityTargetType.none,
            "This unit is invisible",
            "res/images/gooey/icons/btn_dummy.png"),

    // Nagazsh
    hexAbility(
            "Hex",
            AbilityTargetType.unitTarget,
            "Turns an enemy into a harmless creature",
            "res/images/gooey/icons/btn_dummy.png"),
    meteorAbility(
            "Meteor",
            AbilityTargetType.groundTarget,
            "Summons a meteor from the sky",
            "res/images/gooey/icons/btn_dummy.png"),

    // neutral
    gatherAbility(
            "Gather dirt",
            AbilityTargetType.unitTarget,
            "Gather dirt from Dirt mounds",
            "res/images/races/lisk/abilities/icons/btn_gather.png"),
    groundNourishmentAbility(
            "Ground nourishment",
            AbilityTargetType.none,
            "Gives nearby buildings increased armor and HP regeneration",
            "res/images/races/airon/abilities/icons/btn_root.png"),
    dirtSourceAbility(
            "Gatherable dirt",
            AbilityTargetType.none,
            "Gather dirt from here",
            "res/images/races/neutral/abilities/icons/btn_gatherdirt.png"),
    deliverDirtAbility(
            "Deliver dirt",
            AbilityTargetType.none,
            "Deliver dirt here",
            "res/images/races/neutral/abilities/icons/btn_deliverdirt.png"),
    passiveUnitCargoAbility(
            "Unit cargo",
            AbilityTargetType.none,
            "Can carry a unit",
            "res/images/races/neutral/abilities/icons/btn_unitcargo.png"),

    unloadUnitAbility(
            "Unload unit",
            AbilityTargetType.noTarget,
            "Unload a unit carried inside",
            "res/images/races/neutral/abilities/icons/btn_unloadunit.png"),

    gatherPointAbility(
            "Set unit gather point",
            AbilityTargetType.groundTarget,
            "Set a gather point where created units will move",
            "res/images/gooey/icons/btn_gatherpoint.png"),

    ;

    private String name, hoverInfoText, iconImagePath;
    private AbilityTargetType abilityTargetType;

    AbilityType(String name, AbilityTargetType abilityTargetType, String hoverInfoText,
            String iconImagePath) {
        this.name = name;
        this.abilityTargetType = abilityTargetType;
        this.hoverInfoText = hoverInfoText;
        this.iconImagePath = iconImagePath;
    }

    public AbilityTargetType getAbilityTargetType() {
        return abilityTargetType;
    }

    public String getHoverInfoText() {
        return name + "\n" + hoverInfoText;
    }

    public String getIconImagePath() {
        return iconImagePath;
    }

    public String getName() {
        return name;
    }

}
