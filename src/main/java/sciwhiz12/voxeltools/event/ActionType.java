package sciwhiz12.voxeltools.event;

import net.minecraftforge.eventbus.api.Event.Result;

public enum ActionType {
    PASS(Result.DEFAULT), CANCEL(Result.ALLOW), CONTINUE(Result.ALLOW);

    Result result;

    ActionType(Result res) {
        result = res;
    }

    public Result getEventResult() {
        return result;
    }
}
