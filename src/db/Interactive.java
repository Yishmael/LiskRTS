package db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import encaps.Message;
import encaps.MethodAndArgs;
import gameplay.Requirement;

public abstract class Interactive {

    private ArrayList<Requirement> requirements = new ArrayList<>();
    private ArrayList<MethodAndArgs> methods = new ArrayList<>();
    private int iconRow, iconColumn;
    private String hoverText = "NIL" + '\n' + "NIL";
    private Image iconImage;
    protected Message queuedMessage;

    public void addRequirement(Requirement req) {
        if (!hasRequirement(req)) {
            requirements.add(new Requirement(req.getRequirement()));
        }
    }

    public void meetRequirement(Requirement req) {
        for (Requirement r: requirements) {
            if (r.getRequirement() == req.getRequirement()) {
                if (!r.isMet()) {
                    r.setMet(true);

                }
            }
        }
    }

    public ArrayList<Requirement> getRequirements() {
        return requirements;
    }

    public boolean hasRequirement(Requirement req) {
        for (Requirement r: requirements) {
            if (r.getRequirement() == req.getRequirement()) {
                return true;
            }
        }
        return false;
    }

    public boolean isEnabled() {
        for (Requirement req: requirements) {
            if (!req.isMet()) {
                return false;
            }
        }
        return true;
    }

    public void callAllMethodsOnUnit(Unit unit) {
        for (MethodAndArgs mnr: methods) {
            try {
                mnr.getMethod().invoke(unit, mnr.getArgs());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void addCalledMethod(String methodName, @SuppressWarnings("rawtypes") Class[] parameterTypes,
            Object[] args) {
        try {
            Method m = Unit.class.getMethod(methodName, parameterTypes);
            methods.add(new MethodAndArgs(m, args));
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

    }

    public void setIconPosition(int iconRow, int iconColumn) {
        this.iconRow = iconRow;
        this.iconColumn = iconColumn;
    }

    public int getRow() {
        return iconRow;
    }

    public int getColumn() {
        return iconColumn;
    }

    public void setHoverText(String hoverText) {
        this.hoverText = hoverText;
    }

    public void setIconImage(Image iconImage) {
        this.iconImage = iconImage;
    }

    public Image getIconImage() {
        return iconImage;
    }

    public String getHoverText() {
        return hoverText;
    }

    public Message popQueuedMessage() throws SlickException {
        if (queuedMessage == null) {
            return null;
        }

        Message msg = new Message(queuedMessage.getMessageType(), queuedMessage.getData());
        queuedMessage = null;

        return msg;
    }

    public void setQueuedMessage(Message message) {
        queuedMessage = message;
    }

}
