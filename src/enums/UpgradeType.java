package enums;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public enum UpgradeType {
    // Crawler
    venomUpgrade(
            "Venom",
            "Allows some units to poison enemies on attack",
            "res/images/races/lisk/abilities/icons/btn_venom.png"),
    groundNourishmentUpgrade(
            "Ground nourishment",
            "Gives nearby buildings increased armor and HP regeneration",
            "res/images/races/airon/abilities/icons/btn_root.png"),
    insectLegionUpgrade(
            "Insect legion",
            "Grants your Predators Swarm aura which increases nearby units' movement speed",
            "res/images/races/lisk/abilities/icons/btn_swarm.png"),
    scalesUpgrade("Scales", "Provides units with extra armor", "res/images/gooey/icons/btn_dummy.png"),
    enhancedHemolymph(
            "Enhanced Hemolymph",
            "Increases all your units' health regeneration rate",
            "res/images/gooey/icons/btn_dummy.png"),
    termiteColonyUpgrade(
            "Termite colony",
            "Increases the construction rate of your buildings",
            "res/images/gooey/icons/btn_dummy.png"),
    lethalDose(
            "Lethal Dose",
            "Makes Vesp Warriors cause stackable, potentially lethal poison on hit",
            "res/images/gooey/icons/btn_dummy.png"),
    dexterity(
            "Dexterity",
            "Increases attack and movement speeds of many units",
            "res/images/gooey/icons/btn_dummy.png"),
    ectopicNerves("Ectopic Nerves", "EMPTY", "res/images/gooey/icons/btn_dummy.png"),
    naturalHabitat("Natural Habitat", "EMPTY", "res/images/gooey/icons/btn_dummy.png"),

    // Warlock
    cultOfTheShadowsUpgrade(
            "Cult of the shadows",
            "Allows the creation of Dark summoners",
            "res/images/races/nagazsh/abilities/icons/btn_cots.png"),;

    public String name, hoverInfoText, iconImagePath;

    UpgradeType(String name, String hoverInfoText, String iconImagePath) {
        this.name = name;
        this.hoverInfoText = hoverInfoText;
        this.iconImagePath = iconImagePath;
    }

    public String getIconImagePath() {
        return iconImagePath;
    }

    public String getHoverInfoText() {
        return name + "\n" + hoverInfoText;
    }

    public Image getIconImage() throws SlickException {
        return new Image(iconImagePath);
    }

    public String getName() {
        return name;
    }
}
