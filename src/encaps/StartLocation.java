package encaps;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

import db.Unit;
import db.UnitFactory;
import enums.TeamTag;
import enums.UnitType;
import other.Consts;

public class StartLocation {

    private ArrayList<Unit> units = new ArrayList<>();

    public StartLocation(TeamTag team, int x, int y) throws SlickException {
        UnitFactory uf = new UnitFactory();
        switch (team) {
        case team1:
            units.add(uf.createUnitAt(UnitType.spire, x * Consts.TILE_SIZE, y * Consts.TILE_SIZE));
            uf.addCreatableUnitsToUnit((units.get(units.size() - 1)));
            units.add(uf.createUnitAt(UnitType.cave, x * Consts.TILE_SIZE + 120, y * Consts.TILE_SIZE));
            uf.addCreatableUnitsToUnit((units.get(units.size() - 1)));
            units.add(uf.createUnitAt(UnitType.viper, x * Consts.TILE_SIZE, y * Consts.TILE_SIZE + 120));
            uf.addCreatableUnitsToUnit((units.get(units.size() - 1)));
            units.add(uf.createUnitAt(UnitType.predator, x * Consts.TILE_SIZE, y * Consts.TILE_SIZE + 240));
            uf.addCreatableUnitsToUnit((units.get(units.size() - 1)));
            break;
        case team2:
            units.add(uf.createUnitAt(UnitType.darkShrine, x * Consts.TILE_SIZE, y * Consts.TILE_SIZE));
            uf.addCreatableUnitsToUnit((units.get(units.size() - 1)));
            units.add(uf.createUnitAt(UnitType.voidTower, x * Consts.TILE_SIZE + 120, y * Consts.TILE_SIZE));
            uf.addCreatableUnitsToUnit((units.get(units.size() - 1)));
            units.add(uf.createUnitAt(UnitType.servant, x * Consts.TILE_SIZE, y * Consts.TILE_SIZE + 120));
            uf.addCreatableUnitsToUnit((units.get(units.size() - 1)));
            units.add(uf.createUnitAt(UnitType.summoner, x * Consts.TILE_SIZE, y * Consts.TILE_SIZE + 240));
            uf.addCreatableUnitsToUnit((units.get(units.size() - 1)));
            break;
        default:
            throw new SlickException("Invalid team: " + team);
        }
        for (Unit unit: units) {
            unit.setTeamTag(team);
        }
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

}
