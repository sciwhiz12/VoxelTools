package sciwhiz12.voxeltools.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event.Result;

public interface IVoxelTool {
    public default void onLeftClickEmpty(PlayerEntity player, World world, Hand hand) {}

    public default Result hasLeftClickEmptyAction(PlayerEntity player, World world, Hand hand) {
        return Result.DEFAULT;
    }

    public default boolean onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        return false;
    }

    public default Result hasLeftClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return Result.DEFAULT;
    }

    public default void onRightClickEmpty(PlayerEntity player, World world, Hand hand) {}

    public default Result hasRightClickEmptyAction(PlayerEntity player, World world, Hand hand) {
        return Result.DEFAULT;
    }

    public default boolean onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        return false;
    }

    public default Result hasRightClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return Result.DEFAULT;
    }
}
