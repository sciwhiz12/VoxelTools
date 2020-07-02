package sciwhiz12.voxeltools.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Jackhammer extends Item implements ILeftClicker.OnBlock {
    public Jackhammer(Properties properties) {
        super(properties);
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction face) {
        if (player.isServerWorld() && PermissionUtil.checkForPermission(player)) {
            player.world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos,
                    Block.getStateId(player.world.getBlockState(pos)));
            player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), Constants.BlockFlags.DEFAULT);
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        return true;
    }

    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return false;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (!world.isRemote && PermissionUtil.checkForPermission(context.getPlayer())) {
            BlockPos pos = context.getPos();
            world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos,
                    Block.getStateId(context.getPlayer().world.getBlockState(pos)));
            world.setBlockState(pos, Blocks.AIR.getDefaultState(),
                    Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.UPDATE_NEIGHBORS | Constants.BlockFlags.NO_NEIGHBOR_DROPS);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
