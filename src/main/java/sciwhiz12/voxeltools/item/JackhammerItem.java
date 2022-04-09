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

public class JackhammerItem extends Item implements ILeftClicker.OnBlock {
    public JackhammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction face) {
        if (player.isEffectiveAi() && PermissionUtil.checkForPermission(player)) {
            player.level.levelEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos,
                    Block.getId(player.level.getBlockState(pos)));
            player.level.setBlock(pos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.DEFAULT);
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        return true;
    }

    public boolean canAttackBlock(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return false;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (!world.isClientSide && PermissionUtil.checkForPermission(context.getPlayer())) {
            BlockPos pos = context.getClickedPos();
            world.levelEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos,
                    Block.getId(context.getPlayer().level.getBlockState(pos)));
            world.setBlock(pos, Blocks.AIR.defaultBlockState(),
                    Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.UPDATE_NEIGHBORS | Constants.BlockFlags.NO_NEIGHBOR_DROPS);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
