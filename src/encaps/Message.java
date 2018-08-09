package encaps;

public class Message {

    public enum MessageType {
        createUnit,
        attack,
        build,
        cancel,
        research,
        stop,
        move,
        setGatherPoint,
        requestTarget,
        applyEffectsToAttackTarget,
        setBuildingUnitOnCursor,
        setCursorForUnitTargetAbility,
        setCursorForGroundTargetAbility,
        useAbilityByUnit,
        summonUnit,
        applyEffectToUnitsInRadius,
        clearCursor,
        createBuilding,
        showBlueprint,
        clearBlueprint,
        requestingAbilityTargetAtPoint,
        hideUnit,
        refreshSelection,

    }

    private MessageType messageType;
    private Object data;

    public Message(MessageType messageType, Object data) {
        this.messageType = messageType;
        this.data = data;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Object getData() {
        return data;
    }
}
