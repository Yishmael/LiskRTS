package db.effects.nagazsh;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import db.effects.Effect;
import enums.EffectType;
import enums.UnitClass;

public class HexEffect extends Effect {

    private Image hexImage;

    public HexEffect() throws SlickException {
        super(EffectType.hexEffect, new UnitClass[] { UnitClass.organic, UnitClass.enemy });

        hexImage = new Image("res/images/destructibles/tree1.png");

        addApplyingMethod("setModelImage", new Class[] { Image.class }, new Object[] { hexImage });
        addApplyingMethod("setAvatarImage", new Class[] { Image.class }, new Object[] { hexImage });

        addClearingMethod("resetModelImage", new Class[] {}, new Object[] {});
        addClearingMethod("resetAvatarImage", new Class[] {}, new Object[] {});

        hasTicks = true;
        duration = 2500;
    }

}
