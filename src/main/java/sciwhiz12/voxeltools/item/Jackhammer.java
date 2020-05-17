package sciwhiz12.voxeltools.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sciwhiz12.voxeltools.event.ActionType;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Jackhammer extends Item implements IVoxelTool {
    public Jackhammer(Properties properties) {
        super(properties);
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        if (player.isCreative()) return;
        player.world.playEvent(2001, pos, Block.getStateId(player.world.getBlockState(pos)));
        player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
    }

    @Override
    public ActionType hasLeftClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return PermissionUtil.checkForPermission(player) ? ActionType.CONTINUE : ActionType.PASS;
    }

    @Override
    public void onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        player.world.playEvent(2001, pos, Block.getStateId(player.world.getBlockState(pos)));
        player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2 | 16 | 32);
    }

    @Override
    public ActionType hasRightClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return PermissionUtil.checkForPermission(player) ? ActionType.CONTINUE : ActionType.PASS;
    }
}