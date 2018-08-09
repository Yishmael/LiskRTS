
package other;

import org.lwjgl.util.Point;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import db.Unit;
import db.abilities.Ability;
import encaps.Blueprint;
import encaps.Message;
import encaps.Message.MessageType;
import encaps.Vector2i;
import enums.TeamTag;
import enums.UnitClass;
import enums.UpgradeType;
import gameplay.FogOfWar;
import gameplay.GameClock;
import gooey.BottomMenu;
import gooey.BottomMenu.View;
import gooey.ChatBox;
import handlers.EffectHandler;
import zpathfinding.Pathfinder;

public class MainGame extends BasicGame {
    private Input input;
    private int mouseX, mouseY;
    private boolean showGrid;
    private BottomMenu bmenu;
    private Image imageOnCursor;
    private World world;
    private UnitClass[] validTargets;
    private GameClock gameClock = new GameClock();
    // private Server server = new Server();
    // private Client client = new Client();
    private ChatBox chatBox;
    private FogOfWar fow = new FogOfWar();
    private Pathfinder pf = new Pathfinder();

    public MainGame() {
        super("LiskRTS v0.4 (week 4)");
    }

    public static void main(String argv[]) throws SlickException {
        AppGameContainer appgc = new AppGameContainer(new MainGame());
        appgc.setDisplayMode(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT, Consts.FULLSCREEN);
        appgc.setTargetFrameRate((int) (50 * Math.pow(100, 0)));
        appgc.setShowFPS(true);
        appgc.setAlwaysRender(true);
        // appgc.setClearEachFrame(false);
        appgc.setUpdateOnlyWhenVisible(true);
        appgc.start();
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        // gc.setMouseCursor("res/images/gooey/cursors/idle.png", 0, 0);
        chatBox = new ChatBox();
        // server.start();
        // client.run();

        world = new World(pf);
        pf.setUnits(world.getUnits());

        TeamTag team = TeamTag.team1;
        World.team = team;
        bmenu = new BottomMenu(team);
    }

    @Override
    public void update(GameContainer gc, int dt) throws SlickException {
        input = gc.getInput();
        mouseX = input.getMouseX();
        mouseY = input.getMouseY();

        if (dt > 30) {
            dt = 30;
        }
        pf.update(dt);
        handleInput(gc, dt);
        handleOtherKeys(gc, dt);

        for (Unit unit: world.getUnits()) {
            handleMessage(unit.popQueuedMessage());
            for (Ability ability: unit.getUnitAbilities()) {
                handleMessage(ability.popQueuedMessage());
            }
        }
        world.updateUnits(dt);

        if (validTargets == null) {
            world.highlightUnitAt(mouseX, mouseY);
        } else {
            world.highlightUnitClassAt(validTargets, mouseX, mouseY);
        }
        gameClock.update(dt);
    }

    private void handleInput(GameContainer gc, int dt) throws SlickException {

        if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (imageOnCursor == null) {
                world.openSelectionBoxAt(mouseX, mouseY);
            }
        }
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (mouseY < Consts.SCREEN_HEIGHT - Consts.bottomMenuHeight) {
                handleMessage(bmenu.handleLeftClickAtScreen(mouseX, mouseY));
            } else {
                handleMessage(bmenu.handleLeftClickAtGui(mouseX, mouseY));
            }
        }
        if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
            imageOnCursor = null;
            if (bmenu.getView() == View.defaultView) {
                if (mouseY < Consts.SCREEN_HEIGHT - Consts.bottomMenuHeight) {
                    world.orderSelectedUnitsTo(mouseX, mouseY);
                }
            } else {
                bmenu.resetView();
            }
        }
        if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (world.isSelectionBoxActive()) {
                world.applyUnitSelection();
                bmenu.setUnit(world.getSelectedUnit());
            }
        }
        if (input.isKeyPressed(Input.KEY_A)) {
            handleMessage(bmenu.handleHotkeyKey(Input.KEY_A));
        } else if (input.isKeyPressed(Input.KEY_O)) {
            handleMessage(bmenu.handleHotkeyKey(Input.KEY_O));
        } else if (input.isKeyPressed(Input.KEY_M)) {
            handleMessage(bmenu.handleHotkeyKey(Input.KEY_M));
        }
        if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (mouseY > Consts.SCREEN_HEIGHT - Consts.bottomMenuHeight) {
                if (bmenu.getMinimapRectangle().contains(mouseX, mouseY)) {
                    updateMinimapLocation();
                }
            }
        }
    }

    private void handleMessage(Message message) throws SlickException {
        if (message == null) {
            return;
        }
        MessageType messageType = message.getMessageType();
        Object data = message.getData();
        // System.out.println("handleMessage(" + messageType + ")");

        switch (messageType) {
        case setCursorForGroundTargetAbility:
            imageOnCursor = new Image("res/images/races/lisk/abilities/models/burningground.png");
            showGrid = false;
            validTargets = ((Ability) data).getAffectedUnitClasses();
            break;
        case createUnit: {
            Unit unit = (Unit) data;
            world.addUnit(unit);
            World.playersResources[unit.getTeamTag().ordinal()].deltaDirt(-unit.getCreationCost());
        }
            break;
        case clearBlueprint: {
            int x = ((int[]) data)[0];
            int y = ((int[]) data)[1];
            world.removeBlueprintAt(x, y);
        }
            break;
        case createBuilding: {
            Unit unit = (Unit) data;
            unit.setCurrentHealth(50);
            world.addUnit(unit);
            world.removeBlueprintAt((int) unit.getX(), (int) unit.getY());
            World.playersResources[unit.getTeamTag().ordinal()].deltaDirt(-unit.getCreationCost());
        }
            break;
        case hideUnit: {
            Unit unit = (Unit) data;
            unit.setCurrentHealth(0);
            if (unit == bmenu.getUnit()) {
                bmenu.clear();
            }
        }
            break;
        case summonUnit: {
            Unit unit = (Unit) data;
            world.addUnit(unit);
        }
            break;
        case showBlueprint:
            world.addBlueprint((Blueprint) data);
            break;
        case applyEffectToUnitsInRadius: {
            Object[] objects = (Object[]) data;
            Unit sourceUnit = (Unit) objects[0];
            Ability abi = (Ability) objects[1];
            int radius = (int) objects[2];
            Point centerPoint = (Point) objects[3];

            for (Unit unit: world.getUnits()) {
                if (Misc.getDistance(unit.getCenterX(), unit.getCenterY(), centerPoint.getX(),
                        centerPoint.getY()) <= radius) {
                    if (UnitClass.canSourceAffectTarget(sourceUnit.getTeamTag(), abi.getAffectedUnitClasses(),
                            unit.getTeamTag(), unit.getType().getUnitClasses())) {
                        abi.callAllMethodsOnUnit(unit);
                    }
                    EffectHandler.applyEffectToUnitByUnit(sourceUnit, unit, abi.getEffect());
                }
            }
        }
            break;
        case requestTarget:
            Unit requestingUnit = (Unit) data;
            world.findTargetForUnit(requestingUnit);
            break;
        case stop:
            world.stopSelectedUnits();
            break;
        case research:
            TeamTag team = (TeamTag) ((Object[]) (data))[0];
            UpgradeType upgradeType = (UpgradeType) ((Object[]) (data))[1];
            world.researchComplete(upgradeType);
            // bmenu.deltaResourceMineral(((UpgradeType)data).getCost());
            World.playersResources[team.ordinal()].deltaTimber(-150);
            break;
        case applyEffectsToAttackTarget:
            world.applyEffectsToUnitFromUnit((Unit) data, ((Unit) data).getTargetUnit());
            break;
        case setBuildingUnitOnCursor:
            Unit unit = (Unit) data;
            imageOnCursor = unit.getModelImage().getScaledCopy(unit.getScale());
            showGrid = true;
            break;
        case requestingAbilityTargetAtPoint: {
            int x = ((int[]) data)[0];
            int y = ((int[]) data)[1];
            Unit target = world.getUnitAt(x, y);
            if (target == null) {
                handleMessage(bmenu.setAbilityTargetPoint(new Vector2i(x, y)));
            } else {
                handleMessage(bmenu.setAbilityTarget(target));
            }
        }
        case refreshSelection:
            bmenu.refreshSelection();
            break;
        case attack:
            break;
        case move:
            break;
        case setGatherPoint:
            break;
        case build:
            break;
        case cancel:
            break;
        case clearCursor:
            imageOnCursor = null;
            validTargets = null;
            break;
        default:
            break;
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        world.draw(g);
        world.drawUnits(g);
        world.drawSelectionBoxAt(g, mouseX, mouseY);

        g.setColor(Color.white);
        fow.draw(g, world.getUnits());
        bmenu.draw(g);
        if (mouseY > Consts.SCREEN_HEIGHT - Consts.bottomMenuHeight) {
            bmenu.drawHoverTextFor(g, mouseX, mouseY);
        }
        world.drawMinimap(g);
        bmenu.drawCurrentScreenOnMinimap(g);
        if (imageOnCursor != null) {
            for (int i = 0; i < 16; i++) {
                if (showGrid) {
                    int x = Misc.getSnapped(mouseX, World.mapX) + 25 * (i % 4);
                    int y = Misc.getSnapped(mouseY, World.mapY) + 25 * (i / 4);

                    if (!Pathfinder.grid[x / 60][y / 60].wall) {
                        g.setColor(Color.white);
                    } else {
                        g.setColor(Color.red);
                    }
                    g.drawRect(x, y, 25, 25);
                }
            }
            // image centerX = snappingX+50, centerY = snappingY+50
            imageOnCursor.draw(Misc.getSnapped(mouseX, World.mapX), Misc.getSnapped(mouseY, World.mapY), 1,
                    new Color(1, 1, 1, 0.5f));
        }

        chatBox.drawText();
        // gameClock.draw(g);
        // pf.draw(g);
    }

    private void updateMinimapLocation() {
        int prevUpperLeftX = (int) ((bmenu.getCurrentScreenX() - bmenu.getMinimapRectangle().getX()) * 2000f
                / bmenu.getMinimapRectangle().getWidth());;
        int prevUpperLeftY = (int) ((bmenu.getCurrentScreenY() - bmenu.getMinimapRectangle().getY()) * 2000f
                / bmenu.getMinimapRectangle().getHeight());
        bmenu.setCurrentScreenOnMinimapCenter(mouseX, mouseY);
        int upperLeftX = (int) ((bmenu.getCurrentScreenX() - bmenu.getMinimapRectangle().getX()) * 2000f
                / bmenu.getMinimapRectangle().getWidth());
        int upperLeftY = (int) ((bmenu.getCurrentScreenY() - bmenu.getMinimapRectangle().getY()) * 2000f
                / bmenu.getMinimapRectangle().getHeight());

        world.deltaMapX(prevUpperLeftX - upperLeftX);
        world.deltaMapY(prevUpperLeftY - upperLeftY);
        Pathfinder.shiftX -= prevUpperLeftX - upperLeftX;
        Pathfinder.shiftY -= prevUpperLeftY - upperLeftY;
        fow.deltaX(prevUpperLeftX - upperLeftX);
        fow.deltaY(prevUpperLeftY - upperLeftY);
        for (Unit unit: world.getUnits()) {
            unit.deltaX(prevUpperLeftX - upperLeftX);
            unit.deltaY(prevUpperLeftY - upperLeftY);
        }
    }

    private void handleOtherKeys(GameContainer gc, int dt) throws SlickException {
        int[] vector = new int[] { 0, 0 };

        if (input.isKeyDown(Input.KEY_UP)) {
            vector[1]++;
        } else if (input.isKeyDown(Input.KEY_DOWN)) {
            vector[1]--;
        }
        if (input.isKeyDown(Input.KEY_LEFT)) {
            vector[0]++;
        } else if (input.isKeyDown(Input.KEY_RIGHT)) {
            vector[0]--;
        }

        if (vector[0] != 0 || mouseX < 3) {
            int dx = (int) (vector[0] * 1000f * dt / 1000);
            world.deltaMapX(dx);
            bmenu.updateCurrentScreenOnMinimap(World.mapX, World.mapY);
            world.deltaUnitsX(dx);
            Pathfinder.shiftX -= dx;
            fow.deltaX(dx);
        }
        if (vector[1] != 0) {
            int dy = (int) (vector[1] * 1000f * dt / 1000);
            world.deltaMapY(dy);
            bmenu.updateCurrentScreenOnMinimap(World.mapX, World.mapY);
            world.deltaUnitsY(dy);
            Pathfinder.shiftY -= dy;
            fow.deltaY(dy);
        }

        if (input.isKeyPressed(Input.KEY_SPACE)) {
            World.team = world.getTeam() == TeamTag.team1 ? TeamTag.team2 : TeamTag.team1;
            bmenu.setTeam(world.getTeam());
        }
        if (input.isKeyPressed(Input.KEY_ENTER)) {
            chatBox.addTextLine(world.getTeam() + ": ");
            // System.out.println(client.sendAndReceive(TeamTag.team1 + ": " + Sys.getTime()));
        }
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            gc.exit();
        }
        if (input.isKeyPressed(Input.KEY_DELETE)) {
            world.deleteSelecteedUnit();
            bmenu.setUnit(world.getSelectedUnit());
        }

        if (input.isMousePressed(Input.MOUSE_MIDDLE_BUTTON)) {
            System.out.println(mouseX + ":" + mouseY);
        }
        if (input.isKeyPressed(Input.KEY_F)) {
            fow.toggle();
        }

        if (Consts.FULLSCREEN) {
            float scrollSpeed = 2500f;
            if (mouseX < 3) {
                world.deltaMapX((int) (+scrollSpeed * dt / 1000));
                bmenu.updateCurrentScreenOnMinimap(World.mapX, World.mapY);
                world.deltaUnitsX((int) (+scrollSpeed * dt / 1000));
            } else if (mouseX > Consts.SCREEN_WIDTH - 3) {
                world.deltaMapX((int) (-scrollSpeed * dt / 1000));
                bmenu.updateCurrentScreenOnMinimap(World.mapX, World.mapY);
                world.deltaUnitsX((int) (-scrollSpeed * dt / 1000));
            }
            if (mouseY < 3) {
                world.deltaMapY((int) (+scrollSpeed * dt / 1000));
                bmenu.updateCurrentScreenOnMinimap(World.mapX, World.mapY);
                world.deltaUnitsY((int) (+scrollSpeed * dt / 1000));
            } else if (mouseY > Consts.SCREEN_HEIGHT - 3) {
                world.deltaMapY((int) (-scrollSpeed * dt / 1000));
                bmenu.updateCurrentScreenOnMinimap(World.mapX, World.mapY);
                world.deltaUnitsY((int) (-scrollSpeed * dt / 1000));
            }
        }

    }
}
