package gameplay;

import java.util.Calendar;
import java.util.Date;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import other.Consts;

public class GameClock {
    private enum PeriodName {
        day("Rise and shine"), night("Lights out");
        String text;

        PeriodName(String text) {
            this.text = text;
        }

        private String getText() {
            return text;
        }
    }

    private long startTime, lastTickTime, currentTime;
    private PeriodName periodName = PeriodName.day;
    private Calendar calendar = Calendar.getInstance();

    public GameClock() {
        startTime = Sys.getTime();
        currentTime = startTime;
    }

    public void update(int dt) {
        if (Sys.getTime() - lastTickTime >= 1000) {
            lastTickTime = Sys.getTime();
            currentTime += 1000;
        }

        if (currentTime - startTime >= 5 * 60 * 1000) {
            currentTime = startTime;
            periodName = PeriodName.values()[(periodName.ordinal() + 1) % PeriodName.values().length];
            System.out.println(periodName.getText());
        }
    }

    public void draw(Graphics g) {
        g.drawString("" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND),
                Consts.SCREEN_WIDTH / 2 - 50, 10);
        calendar.setTime(new Date(Sys.getTime() - startTime));

        if (periodName == PeriodName.day) {
            g.setColor(Color.yellow);
            g.fill(new Rectangle(Consts.SCREEN_WIDTH / 2, 10, 20, 20));
        } else if (periodName == PeriodName.night) {
            g.setColor(new Color(100, 80, 200));
            g.fill(new Rectangle(Consts.SCREEN_WIDTH / 2, 10, 20, 20));
        }
    }
}
