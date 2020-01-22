package sciwhiz12.voxeltools.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event.Result;

public interface IVoxelTool {
    public void onLeftClickEmpty(PlayerEntity player, World world, Hand hand);

    public Result hasLeftClickEmptyAction(PlayerEntity player, World world,
            Hand hand);

    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face);

    public Result hasLeftClickBlockAction(PlayerEntity player, World world,
            Hand hand, BlockPos pos, Direction face);

    public void onRightClickEmpty(PlayerEntity player, World world, Hand hand);

    public Result hasRightClickEmptyAction(PlayerEntity player, World world,
            Hand hand);

    public void onRightClickBlock(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face);

    public Result hasRightClickBlockAction(PlayerEntity player, World world,
            Hand hand, BlockPos pos, Direction face);
}
