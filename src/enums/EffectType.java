package enums;

public enum EffectType {
    stingEffect(
            "Sting poison",
            "this unit is taking damage over time",
            "res/images/none.png",
            "res/images/races/lisk/abilities/icons/btn_sting.png"),
    swarmEffect(
            "Swarm aura",
            "this unit has increased movement speed",
            "res/images/races/lisk/abilities/models/swarm.png",
            "res/images/races/lisk/abilities/icons/btn_swarm.png"),
    groundNourishmentEffect(
            "Ground nourishment",
            "this structure is gaining additional HP",
            "res/images/races/airon/abilities/models/root.png",
            "res/images/races/airon/abilities/icons/btn_root.png"),
    burningGroundEffect(
            "Burning ground",
            "this unit is taking fire damage over time",
            "res/images/races/lisk/abilities/models/burningground.png",
            "res/images/races/lisk/abilities/icons/btn_burningground.png"),
    invisibilityEffect(
            "Invisibility",
            "this unit is invisible to the enemy",
            "res/images/races/lisk/abilities/models/invisibility.png",
            "res/images/gooey/icons/btn_dummy.png"),

    hexEffect(
            "Hexed",
            "this unit is turned into a cuddly animal",
            "res/images/none.png",
            "res/images/gooey/icons/btn_dummy.png"),

    noneEffect("NONE", "NONE", "res/images/none.png", "res/images/gooey/icons/btn_none.png"),

    ;

    private String name, hoverInfoText, effectImagePath, iconImagePath;

    EffectType(String name, String hoverInfoText, String effectImagePath, String iconImagePath) {
        this.name = name;
        this.hoverInfoText = hoverInfoText;
        this.effectImagePath = effectImagePath;
        this.iconImagePath = iconImagePath;
    }

    public String getHoverInfoText() {
        return name + "\n" + hoverInfoText;
    }

    public String getName() {
        return name;
    }

    public String getEffectImagePath() {
        return effectImagePath;
    }

    public String getIconImagePath() {
        return iconImagePath;
    }

}
