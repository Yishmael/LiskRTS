package enums;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public enum UnitType {

    // Sminos
    viper(
            "Viper",
            "A builder",
            "res/images/races/lisk/units/models/viper.png",
            "res/images/races/lisk/units/models/viper.png",
            "res/images/races/lisk/units/icons/btn_viper.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    predator(
            "Predator",
            "A predator",
            "res/images/races/lisk/units/models/predator.png",
            "res/images/races/lisk/units/models/predator.png",
            "res/images/races/lisk/units/icons/btn_predator.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    vespWarrior(
            "Vesp Warrior",
            "A vespar warrior",
            "res/images/races/lisk/units/models/vespar.png",
            "res/images/races/lisk/units/models/vespar.png",
            "res/images/races/lisk/units/icons/btn_vespar.png",
            new UnitClass[] { UnitClass.organic, UnitClass.flying }),
    bloodworm(
            "Bloodworm",
            "Squishy units that can infest an enemy",
            "res/images/races/lisk/units/models/bloodworm.png",
            "res/images/races/lisk/units/models/bloodworm.png",
            "res/images/races/lisk/units/icons/btn_bloodworm.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    mindeater(
            "Mindeater",
            "Another infestor unit, useful against strong units",
            "res/images/races/lisk/units/models/mindeater.png",
            "res/images/races/lisk/units/models/mindeater.png",
            "res/images/races/lisk/units/icons/btn_mindeater.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),

    treefeller(
            "Treefeller",
            "An insect extremely good at cutting down trees.",
            "res/images/races/lisk/units/models/treefeller.png",
            "res/images/races/lisk/units/models/treefeller.png",
            "res/images/races/lisk/units/icons/btn_treefeller.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    undevelopedEgg(
            "Undeveloped Egg",
            "A unit is still developing inside.",
            "res/images/races/lisk/units/models/uegg.png",
            "res/images/races/lisk/units/models/uegg.png",
            "res/images/races/lisk/units/icons/btn_uegg.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    spire(
            "Spire",
            "Best building EU",
            "res/images/races/lisk/units/models/spire.png",
            "res/images/races/lisk/units/models/spire.png",
            "res/images/races/lisk/units/icons/btn_spire.png",
            new UnitClass[] { UnitClass.structure }),
    emptyCocoon(
            "Empty Cocoon",
            "Immobile unit used for metamorphosis",
            "res/images/races/lisk/units/models/cocoon.png",
            "res/images/races/lisk/units/models/cocoon.png",
            "res/images/races/lisk/units/icons/btn_cocoon.png",
            new UnitClass[] { UnitClass.structure, UnitClass.organic }),
    hive(
            "Hive",
            "Allows higher population cap by providing food to your units.",
            "res/images/races/lisk/units/models/hive.png",
            "res/images/races/lisk/units/models/hive.png",
            "res/images/races/lisk/units/icons/btn_hive.png",
            new UnitClass[] { UnitClass.structure }),
    spiralHideout(
            "Spiral Hideout",
            "Allows creation of Treefellers and can upgrade certain units",
            "res/images/races/lisk/units/models/spiralhideout.png",
            "res/images/races/lisk/units/models/spiralhideout.png",
            "res/images/races/lisk/units/icons/btn_spiralhideout.png",
            new UnitClass[] { UnitClass.structure }),
    vespNest(
            "Vesp Nest",
            "Allows for creation of Vesp Warriors",
            "res/images/races/lisk/units/models/vespnest.png",
            "res/images/races/lisk/units/models/vespnest.png",
            "res/images/races/lisk/units/icons/btn_vespnest.png",
            new UnitClass[] { UnitClass.structure }),
    cave(
            "Cave",
            "Upgrades are done here",
            "res/images/races/lisk/units/models/cave.png",
            "res/images/races/lisk/units/models/cave.png",
            "res/images/races/lisk/units/icons/btn_cave.png",
            new UnitClass[] { UnitClass.structure }),
    wormLair(
            "Worm Lair",
            "Can produce Bloodworms and Mindeaters",
            "res/images/races/lisk/units/models/wormlair.png",
            "res/images/races/lisk/units/models/wormlair.png",
            "res/images/races/lisk/units/icons/btn_wormlair.png",
            new UnitClass[] { UnitClass.structure }),
    honeycomb(
            "Honeycomb",
            "Allows for higher population capacity. Has Eat Honey abiity",
            "res/images/races/lisk/units/models/honeycomb.png",
            "res/images/races/lisk/units/models/honeycomb.png",
            "res/images/races/lisk/units/icons/btn_honeycomb.png",
            new UnitClass[] { UnitClass.structure }),
    hatchery(
            "Hatchery",
            "Can create Predators; strong ranged units",
            "res/images/races/lisk/units/models/hatchery.png",
            "res/images/races/lisk/units/models/hatchery.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.structure }),

    colonyProtector(
            "Colony Protector",
            "An immobile unit with fast ranged attack. Can use Toxic Shot ability.",
            "res/images/races/lisk/units/models/colonyprotector.png",
            "res/images/races/lisk/units/models/colonyprotector.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.structure }),

    // Nagazsh

    servant(
            "Servant",
            "A basic worker unit",
            "res/images/races/nagazsh/units/models/servant.png",
            "res/images/races/nagazsh/units/models/servant.png",
            "res/images/races/nagazsh/units/icons/btn_servant.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    sadist(
            "Sadist",
            "Sadistic unit that gains bonuses when nearby units take damage",
            "res/images/races/nagazsh/units/models/sadist.png",
            "res/images/races/nagazsh/units/models/sadist.png",
            "res/images/races/nagazsh/units/icons/btn_sadist.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    summoner(
            "Summoner",
            "This unit can invoke most devastating spells and summon demons from Below",
            "res/images/races/nagazsh/units/models/darksummoner.png",
            "res/images/races/nagazsh/units/models/darksummoner.png",
            "res/images/races/nagazsh/units/icons/btn_darksummoner.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    underlord(
            "Underlord",
            "Strong melee unit that provides auras to nearby units",
            "res/images/races/nagazsh/units/models/underlord.png",
            "res/images/races/nagazsh/units/models/underlord.png",
            "res/images/races/nagazsh/units/icons/btn_underlord.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    miscreation(
            "Miscreation",
            "A failed experiment of black magic",
            "res/images/races/nagazsh/units/models/miscreation.png",
            "res/images/races/nagazsh/units/models/miscreation.png",
            "res/images/races/nagazsh/units/icons/btn_miscreation.png",
            new UnitClass[] { UnitClass.unliving, UnitClass.ground }),
    creeper(
            "Creeper",
            "Scary looking unit designed to creep enemies out",
            "res/images/races/nagazsh/units/models/creeper.png",
            "res/images/races/nagazsh/units/models/creeper.png",
            "res/images/races/nagazsh/units/icons/btn_creeper.png",
            new UnitClass[] { UnitClass.unliving, UnitClass.ground }),
    shade(
            "Shade",
            "A shadow of his former self",
            "res/images/races/nagazsh/units/models/shade.png",
            "res/images/races/nagazsh/units/models/shade.png",
            "res/images/races/nagazsh/units/icons/btn_shade.png",
            new UnitClass[] { UnitClass.ethereal, UnitClass.ground }),
    naz(
            "Naz, the protector of demons",
            "The third mightiest demon that can be summoned into this world",
            "res/images/races/nagazsh/units/models/naz.png",
            "res/images/races/nagazsh/units/models/naz.png",
            "res/images/races/nagazsh/units/icons/btn_naz.png",
            new UnitClass[] { UnitClass.unliving, UnitClass.ground }),
    stalker(
            "Stalker",
            "Very mobile unit used for spying on enemies",
            "res/images/races/nagazsh/units/models/stalker.png",
            "res/images/races/nagazsh/units/models/stalker.png",
            "res/images/races/nagazsh/units/icons/btn_stalker.png",
            new UnitClass[] { UnitClass.organic, UnitClass.flying }),
    engulfer(
            "Engulfer",
            "A giant demon that can swallow enemy units and grant their energy",
            "res/images/races/nagazsh/units/models/engulfer.png",
            "res/images/races/nagazsh/units/models/engulfer.png",
            "res/images/races/nagazsh/units/icons/btn_engulfer.png",
            new UnitClass[] { UnitClass.unliving, UnitClass.ground }),
    darkShrine(
            "Dark shrine",
            "Not very impressive",
            "res/images/races/nagazsh/units/models/darkshrine.png",
            "res/images/races/nagazsh/units/models/darkshrine.png",
            "res/images/races/nagazsh/units/icons/btn_darkshrine.png",
            new UnitClass[] { UnitClass.structure }),
    blackPit(
            "Black pit",
            "Can produce Stalkers",
            "res/images/races/nagazsh/units/models/blackpit.png",
            "res/images/races/nagazsh/units/models/blackpit.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.structure }),
    voidTower(
            "Void tower",
            "A tall structure with powerful attack. Can cast Void Cloud.",
            "res/images/races/nagazsh/units/models/voidtower.png",
            "res/images/races/nagazsh/units/models/voidtower.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.structure }),

    // Airon

    nymph(
            "Nymph",
            "A basic worker unit",
            "res/images/races/airon/units/models/nymph.png",
            "res/images/races/airon/units/models/nymph.png",
            "res/images/races/airon/units/icons/btn_nymph.png",
            new UnitClass[] { UnitClass.ethereal, UnitClass.ground }),
    wildDeer(
            "Wild deer",
            "A mediocre fighting unit with high movement speed",
            "res/images/races/airon/units/models/wilddeer.png",
            "res/images/races/airon/units/models/wilddeer.png",
            "res/images/races/airon/units/icons/btn_wilddeer.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    blackBear(
            "Black bear",
            "Very durable bear with swipe ability",
            "res/images/races/airon/units/models/blackbear.png",
            "res/images/races/airon/units/models/blackbear.png",
            "res/images/races/airon/units/icons/btn_blackbear.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    naturesShaman(
            "Nature's shaman",
            "A supporing unit with heal ability",
            "res/images/races/airon/units/models/naturesshaman.png",
            "res/images/races/airon/units/models/naturesshaman.png",
            "res/images/races/airon/units/icons/btn_naturesshaman.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    artemis(
            "Artemis",
            "A mighty goddess, provides various auras to allied units",
            "res/images/races/airon/units/models/artemis.png",
            "res/images/races/airon/units/models/artemis.png",
            "res/images/races/airon/units/icons/btn_artemis.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    dryad(
            "Dryad",
            "A magical being with root ability",
            "res/images/races/airon/units/models/dryad.png",
            "res/images/races/airon/units/models/dryad.png",
            "res/images/races/airon/units/icons/btn_dryad.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    centaurArcher(
            "Centaur archer",
            "A centaur with a crossbow",
            "res/images/races/airon/units/models/centaurarcher.png",
            "res/images/races/airon/units/models/centaurarcher.png",
            "res/images/races/airon/units/icons/btn_centaurarcher.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    centaurWarrior(
            "Centaur warrior",
            "Close combat unit with mediocre attack and high armor",
            "res/images/races/airon/units/models/centaurwarrior.png",
            "res/images/races/airon/units/models/centaurwarrior.png",
            "res/images/races/airon/units/icons/btn_centaurwarrior.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    satyrSpellcaster(
            "Satyr spellcaster",
            "A creature of the forest mastered in offensive spells",
            "res/images/races/airon/units/models/satyrspellcaster.png",
            "res/images/races/airon/units/models/satyrspellcaster.png",
            "res/images/races/airon/units/icons/btn_satyrspellcaster.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    mountedSatyr(
            "Mounted satyr",
            "A satyr riding a deer with net ability",
            "res/images/races/airon/units/models/mountedsatyr.png",
            "res/images/races/airon/units/models/mountedsatyr.png",
            "res/images/races/airon/units/icons/btn_mountedsatyr.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    entwiningRoot(
            "Entwining root",
            "Extremely durable immovable unit used for defense",
            "res/images/races/airon/units/models/entwiningroot.png",
            "res/images/races/airon/units/models/entwiningroot.png",
            "res/images/races/airon/units/icons/btn_entwiningroot.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    hollowTrunk(
            "Hollow trunk",
            "Main structure that can produce workers",
            "res/images/races/airon/units/models/hollowtrunk.png",
            "res/images/races/airon/units/models/hollowtrunk.png",
            "res/images/races/airon/units/icons/btn_hollowtrunk.png",
            new UnitClass[] { UnitClass.structure }),

    // neutral

    dirtMound(
            "Dirt Mound",
            "Contains dirt",
            "res/images/races/neutral/units/models/dirtmound.png",
            "res/images/races/neutral/units/models/dirtmound.png",
            "res/images/races/neutral/units/icons/btn_dirtmound.png",
            new UnitClass[] { UnitClass.resource }),
    tree1(
            "Tree",
            "Source of lumber",
            "res/images/destructibles/tree1.png",
            "res/images/destructibles/tree1.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.resource }),
    tree2(
            "Tree",
            "Source of lumber",
            "res/images/destructibles/tree2.png",
            "res/images/destructibles/tree2.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.resource }),
    tree3(
            "Tree",
            "Source of lumber",
            "res/images/destructibles/tree3.png",
            "res/images/destructibles/tree3.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.resource }),
    tree4(
            "Tree",
            "Source of lumber",
            "res/images/destructibles/tree4.png",
            "res/images/destructibles/tree4.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.resource }),
    tree5(
            "Tree",
            "Source of lumber",
            "res/images/destructibles/tree5.png",
            "res/images/destructibles/tree5.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.resource }),
    tree6(
            "Tree",
            "Source of lumber",
            "res/images/destructibles/tree6.png",
            "res/images/destructibles/tree6.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.resource }),
    tree7(
            "Tree",
            "Source of lumber",
            "res/images/destructibles/tree7.png",
            "res/images/destructibles/tree7.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.resource }),
    tree8(
            "Tree",
            "Source of lumber",
            "res/images/destructibles/tree8.png",
            "res/images/destructibles/tree8.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.resource }),
    tree9(
            "Tree",
            "Source of lumber",
            "res/images/destructibles/tree9.png",
            "res/images/destructibles/tree9.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.resource }),
    tree10(
            "Tree",
            "Source of lumber",
            "res/images/destructibles/tree10.png",
            "res/images/destructibles/tree10.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.resource }),
    crystal(
            "Crystal",
            "Source of crystal",
            "res/images/destructibles/crystal1.png",
            "res/images/destructibles/crystal1.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.resource }),
    tent(
            "Tent",
            "A small tent",
            "res/images/destructibles/tent1.png",
            "res/images/destructibles/tent1.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.structure }),

    // other
    dummy(
            "Dummy",
            "Dummy",
            "res/images/races/neutral/units/models/dummy.png",
            "res/images/races/neutral/units/models/dummy.png",
            "res/images/races/neutral/units/icons/btn_dirtmound.png",
            new UnitClass[] {}),

    // unused old units

    // Crawler
    rhinob(
            "Rhinob",
            "Highly resistant unit used best as a defense",
            "res/images/races/lisk/units/models/rhinob.png",
            "res/images/races/lisk/units/models/rhinob.png",
            "res/images/races/lisk/units/icons/btn_rhinob.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    beetle(
            "Beetle",
            "A beetle",
            "res/images/races/lisk/units/models/beetle.png",
            "res/images/races/lisk/units/models/beetle.png",
            "res/images/races/lisk/units/icons/btn_beetle.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    dragonfly(
            "Dragonfly",
            "A dragonfly",
            "res/images/races/lisk/units/models/dragonfly.png",
            "res/images/races/lisk/units/models/dragonfly.png",
            "res/images/races/lisk/units/icons/btn_dragonfly.png",
            new UnitClass[] { UnitClass.organic, UnitClass.flying }),
    hercobet(
            "Hercobet",
            "Highly armored unit great for close combat",
            "res/images/races/lisk/units/models/hercobet.png",
            "res/images/races/lisk/units/models/hercobet.png",
            "res/images/races/lisk/units/icons/btn_hercobet.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    beevil(
            "Beevil",
            "Mobile insect that can infest enemies with larvae that grow into a Beevil",
            "res/images/races/lisk/units/models/beevil.png",
            "res/images/races/lisk/units/models/beevil.png",
            "res/images/races/lisk/units/icons/btn_beevil.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    arach(
            "Arach",
            "A spider whose lightning speed and poison are unmatched",
            "res/images/races/lisk/units/models/arach.png",
            "res/images/races/lisk/units/models/arach.png",
            "res/images/races/lisk/units/icons/btn_arach.png",
            new UnitClass[] { UnitClass.organic, UnitClass.ground }),
    buriedtrap(
            "Buried trap",
            "Used for capturing enemy units",
            "res/images/races/lisk/units/models/buriedtrap.png",
            "res/images/races/lisk/units/models/buriedtrap.png",
            "res/images/gooey/icons/btn_dummy.png",
            new UnitClass[] { UnitClass.structure }),;

    private String name, hoverInfoText;
    private String modelImagePath, avatarImagePath, iconImagePath;
    private UnitClass[] unitClasses;

    private UnitType(String name, String hoverInfoText, String modelImagePath, String avatarPath,
            String iconImagePath, UnitClass[] unitClasses) {
        this.name = name;
        this.hoverInfoText = hoverInfoText;
        this.modelImagePath = modelImagePath;
        this.avatarImagePath = avatarPath;
        this.iconImagePath = iconImagePath;
        this.unitClasses = unitClasses;
    }

    public UnitClass[] getUnitClasses() {
        return unitClasses;
    }

    public boolean isUnitClass(UnitClass uc) {
        for (UnitClass unitClass: unitClasses) {
            if (uc == unitClass) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getHoverInfoText() {
        return name + "\n" + hoverInfoText;
    }

    public Image getModelImage() throws SlickException {
        return new Image(modelImagePath);
    }

    public Image getAvatarImage() throws SlickException {
        return new Image(avatarImagePath);
    }

    public Image getIconImage() throws SlickException {
        return new Image(iconImagePath);
    }

    public boolean isVisible() {
        return true;
    }

}
