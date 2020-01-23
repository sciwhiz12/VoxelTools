package sciwhiz12.voxeltools.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sciwhiz12.voxeltools.event.ActionType;

public interface IVoxelTool {
    public default void onLeftClickEmpty(PlayerEntity player, World world, Hand hand) {}

    public default ActionType hasLeftClickEmptyAction(PlayerEntity player, World world, Hand hand) {
        return ActionType.PASS;
    }

    public default void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {}

    public default ActionType hasLeftClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return ActionType.PASS;
    }

    public default void onRightClickEmpty(PlayerEntity player, World world, Hand hand) {}

    public default ActionType hasRightClickEmptyAction(PlayerEntity player, World world,
            Hand hand) {
        return ActionType.PASS;
    }

    public default void onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {}

    public default ActionType hasRightClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return ActionType.PASS;
    }
}
