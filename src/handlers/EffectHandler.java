package handlers;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.effects.Effect;
import enums.UnitClass;

public class EffectHandler {

    public static void applyEffectToUnitByUnit(Unit byUnit, Unit unit, Effect effect) throws SlickException {
        // remove this when abilities have list of effects
        if (unit == null) {
            return;
        }

        if (unit.isAffectedBy(effect.getType())) {
            // System.out.println(effect.getType().getName() + " refreshed on " +
            unit.affectBy(effect);
            return;
        }

        boolean canAffectSelf = false;
        if (unit.hashCode() == byUnit.hashCode()) {
            for (UnitClass uc: effect.getAffectedUnitClasses()) {
                if (uc == UnitClass.self) {
                    canAffectSelf = true;
                    break;
                }
            }
            if (!canAffectSelf) {
                return;
            }
        } else if (!UnitClass.canSourceAffectTarget(byUnit.getTeamTag(), effect.getAffectedUnitClasses(),
                unit.getTeamTag(), unit.getType().getUnitClasses())) {
            return;
        }
        unit.affectBy(effect);
    }
}
