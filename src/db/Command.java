package db;

import org.newdawn.slick.SlickException;

import enums.CommandType;

public class Command extends Interactive {
    private CommandType commandType;

    public Command(CommandType commandType) throws SlickException {
        this.commandType = commandType;

        switch (commandType) {
        case attack:
            setIconPosition(0, 0);
            break;
        case stop:
            setIconPosition(0, 1);
            break;
        case move:
            setIconPosition(0, 2);
            break;
        case build:
            setIconPosition(0, 3);
            break;
        case cancel:
            setIconPosition(2, 3);
            break;
        default:
            throw new SlickException("Invalid command: " + commandType);
        }
        setHoverText(commandType.getHoverInfoText());
    }

    public CommandType getType() {
        return commandType;
    }
}
