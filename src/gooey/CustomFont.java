package gooey;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class CustomFont {

    private UnicodeFont uf;

    @SuppressWarnings("unchecked")
    public CustomFont(String fontName) {
        Font font = new Font(fontName, Font.PLAIN, 16);
        uf = new UnicodeFont(font);
        uf.getEffects().add(new ColorEffect(Color.white));
        uf.addAsciiGlyphs();
        try {
            uf.loadGlyphs();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void drawString(String string, int x, int y) {
        uf.drawString(x, y, string);
    }

    public void drawString(String string, int x, int y, org.newdawn.slick.Color color) {
        uf.drawString(x, y, string, color);
    }

}
