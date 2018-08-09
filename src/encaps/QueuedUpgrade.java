package encaps;

import db.Upgrade;

public class QueuedUpgrade {
    private Upgrade upgrade;
    private long initTime;

    public QueuedUpgrade(Upgrade upgrade, long initTime) {
        this.upgrade = upgrade;
        this.initTime = initTime;
    }

    public Upgrade getUpgrade() {
        return upgrade;
    }

    public long getInitTime() {
        return initTime;
    }

    public void setInitTime(long initTime) {
        this.initTime = initTime;
    }
}
