package gooey;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import db.Command;
import db.Interactive;
import db.Unit;
import db.Upgrade;
import db.abilities.Ability;
import enums.UnitType;
import enums.UpgradeType;
import gooey.BottomMenu.View;

public class Button {

    private Image iconImage;
    private BottomMenu.View view;

    private Interactive icon;

    private Rectangle rect = new Rectangle(0, 0, 30, 30);

    public Button(Interactive icon, View view) throws SlickException {
        this.icon = icon;
        if (Ability.class.isInstance(icon)) {
            this.iconImage = new Image(((Ability) icon).getType().getIconImagePath());
        } else if (Command.class.isInstance(icon)) {
            this.iconImage = new Image(((Command) icon).getType().getIconImagePath());
        } else if (Unit.class.isInstance(icon)) {
            this.iconImage = ((Unit) icon).getType().getIconImage();
        } else if (Upgrade.class.isInstance(icon)) {
            Upgrade upg = (Upgrade) icon;
            if (UpgradeType.class.isInstance(upg.getUpgrade())) {
                this.iconImage = new Image(((UpgradeType) upg.getUpgrade()).getIconImagePath());
            } else if (UnitType.class.isInstance(upg.getUpgrade())) {
                this.iconImage = ((UnitType) upg.getUpgrade()).getIconImage();
            }
        }

        this.view = view;
    }

    public void drawAt(Graphics g, int x, int y, Color color) {
        rect.setX(x);
        rect.setY(y);
        g.drawImage(iconImage, x, y, color);
        // g.draw(rect);
    }

    public void drawAt(Graphics g, int x, int y) {
        if (icon.isEnabled()) {
            drawAt(g, x, y, Color.white);
        } else {

            drawAt(g, x, y, new Color(1, 1, 1, 0.3f));
        }
    }

    public View getView() {
        return view;
    }

    public Rectangle getRectangle() {
        return rect;
    }

    public boolean isVisibleInView(View view) {
        return this.view == view;
    }

    public Image getButtonImage() {
        return iconImage;
    }

    public Interactive getIcon() {
        return icon;
    }
}
