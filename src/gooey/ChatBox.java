package gooey;

import java.util.ArrayList;

import other.Consts;

public class ChatBox {
    private ArrayList<String> textLines = new ArrayList<>();
    private CustomFont cf;
    private int yOffset = Consts.SCREEN_HEIGHT - 210;

    public ChatBox() {
        cf = new CustomFont("Tahoma");
    }

    public void drawText() {
        for (int i = 0; i < textLines.size(); i++) {
            cf.drawString(textLines.get(i), 10, yOffset - textLines.size() * 15 + 15 * i);
        }
    }

    public void addTextLine(String textLine) {
        textLines.add(textLine);
        while (textLines.size() > 10) {
            textLines.remove(0);
        }
    }
}
