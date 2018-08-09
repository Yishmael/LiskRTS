package encaps;

import db.Unit;

public class QueuedUnit {
    private Unit unit;
    private long initTime;

    public QueuedUnit(Unit unit, long initTime) {
        this.unit = unit;
        this.initTime = initTime;
    }

    public Unit getUnit() {
        return unit;
    }

    public long getInitTime() {
        return initTime;
    }

    public void setInitTime(long initTime) {
        this.initTime = initTime;
    }
}
