package encaps;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

import db.Unit;
import enums.TeamTag;
import zdestructibles.Tree;

public class CollectionSpawner {

    public static final int trees = 0;
    public static final int camp1 = 1;

    private ArrayList<Unit> units = new ArrayList<>();

    public CollectionSpawner(int camp, int x, int y) throws SlickException {

        switch (camp) {
        case trees:
            for (int i = 0; i < 100; i++) {
                units.add(new Tree((int) (1 + Math.random() * 9),
                        x + (int) (Math.random() * 30 + 50 * (i % 15)),
                        y + (int) (Math.random() * 10 + 80 * (i / 15))));
            }
            for (Unit unit: units) {
                unit.setTeamTag(TeamTag.friendly);
            }
            break;
        case camp1:
            // UnitFactory uf = new UnitFactory();
            // units.add(uf.createUnit(UnitType.blackPit, x, y));
            // units.add(uf.createUnit(UnitType.blackPit, x + 150, y));
            // units.add(uf.createUnit(UnitType.blackPit, x + 75, y - 75));
            // units.add(uf.createUnit(UnitType.blackPit, x + 40, y + 85));
            // units.add(uf.createUnit(UnitType.blackPit, x + 120, y + 85));
            for (Unit unit: units) {
                unit.setTeamTag(TeamTag.friendly);
            }
            break;
        default:
            throw new SlickException("Unknown collection: " + camp);
        }

    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

}
