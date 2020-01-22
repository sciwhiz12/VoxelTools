package sciwhiz12.voxeltools.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Jackhammer extends Item implements IVoxelTool {
    public Jackhammer(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        if (player.isCreative()) return false;
        player.world.playEvent(2001, pos, Block.getStateId(player.world.getBlockState(pos)));
        player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
        return false;
    }

    @Override
    public Result hasLeftClickBlockAction(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        return PermissionUtil.checkForPermission(player) ? Result.ALLOW : Result.DEFAULT;
    }

    @Override
    public boolean onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        player.world.playEvent(2001, pos, Block.getStateId(player.world.getBlockState(pos)));
        player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2 | 16 | 32);
        return false;
    }

    @Override
    public Result hasRightClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return PermissionUtil.checkForPermission(player) ? Result.ALLOW : Result.DEFAULT;
    }
}
