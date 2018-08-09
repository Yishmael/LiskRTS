package enums;

public enum CommandType {
    attack("Attack", "Attack a unit", "res/images/gooey/icons/btn_attack.png"),
    move("Move", "Move unit to a target area", "res/images/gooey/icons/btn_move.png"),
    stop("Stop", "Stop unit actions", "res/images/gooey/icons/btn_stop.png"),
    build("Build", "Create a structure", "res/images/gooey/icons/btn_build.png"),
    cancel("Cancel", "Cancel current action", "res/images/gooey/icons/btn_cancel.png"),

    ;
    private String name, hoverInfoText;
    private String iconImagePath;

    CommandType(String name, String hoverInfoText, String iconImagePath) {
        this.name = name;
        this.hoverInfoText = hoverInfoText;
        this.iconImagePath = iconImagePath;
    }

    public String getName() {
        return name;
    }

    public String getHoverInfoText() {
        return name + "\n" + hoverInfoText;
    }

    public String getIconImagePath() {
        return iconImagePath;
    }

}
