package enums;

import org.newdawn.slick.Color;

public enum TeamTag {
    team1(new Color(0, 1f, 0, 1f)),
    team2(new Color(1f, 0, 0, 1f)),
    friendly(new Color(1, 1, 0, 1f)),
    hostile(new Color(1f, 0.4f, 1f, 1f));

    ;

    private Color color;

    TeamTag(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public boolean isEnemyOf(TeamTag teamTag) {
        if (this == friendly || teamTag == friendly) {
            return false;
        }

        if (this == hostile || teamTag == hostile) {
            return true;
        }

        if (teamTag != this) {
            return true;
        }

        return false;
    }
}
