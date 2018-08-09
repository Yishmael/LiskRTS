package enums;

public enum UnitClass {
    // absolute
    ground,
    ethereal,
    flying,
    structure,
    organic,
    unliving,
    mechanical,
    resource,

    // relative
    enemy,
    friend,
    self,;

    // can ability affect unit?
    // , ability team, unit
    public static boolean canSourceAffectTarget(TeamTag sourceTeam, UnitClass[] affectedClasses,
            TeamTag targetTeam, UnitClass[] targetClasess) {

        // if teams are enemies
        if (sourceTeam.isEnemyOf(targetTeam)) {
            // ability cannot target enemy
            if (!hasUnitClass(affectedClasses, UnitClass.enemy)) {
                return false;
            }
            // if teams are friends
        } else {
            // if cannot target friend
            if (!hasUnitClass(affectedClasses, UnitClass.friend)) {
                return false;
            }
        }
        // target is right team, but cannot be affected due to unit tags
        if (!existsAbsoluteMatch(affectedClasses, targetClasess)) {
            return false;
        }

        return true;
    }

    private static boolean existsAbsoluteMatch(UnitClass[] first, UnitClass[] second) {
        for (UnitClass uc1: first) {
            for (UnitClass uc2: second) {
                // exclude relative tags
                if (uc1 != UnitClass.enemy && uc1 != UnitClass.friend && uc1 != UnitClass.self) {
                    if (uc1 == uc2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean hasUnitClass(UnitClass[] classes, UnitClass uc) {
        for (UnitClass ucl: classes) {
            if (ucl == uc) {
                return true;
            }
        }
        return false;
    }
}
