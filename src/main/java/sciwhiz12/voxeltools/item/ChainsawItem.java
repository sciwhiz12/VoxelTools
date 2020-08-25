package sciwhiz12.voxeltools.item;

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
import net.minecraft.world.server.ServerWorld;
import sciwhiz12.voxeltools.VxConfig;
import sciwhiz12.voxeltools.VxTags;
import sciwhiz12.voxeltools.util.PermissionUtil;

import java.util.Collections;

public class ChainsawItem extends Item implements ILeftClicker.OnBlock {
    public ChainsawItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction face) {
        if (player.isServerWorld() && PermissionUtil.checkForPermission(player)) {
            for (BlockPos targetPos : getDestroyRadius(VxConfig.Server.chainsawCutRadius, pos)) {
                if (VxTags.TREE_MATTER.contains(player.world.getBlockState(targetPos).getBlock())) {
                    player.world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
                }
            }
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        return true;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (!world.isRemote && player != null && PermissionUtil.checkForPermission(player)) {
            ServerWorld serverWorld = (ServerWorld) world;

            if (!player.isCrouching()) {
                for (BlockPos targetPos : getDestroyRadius(VxConfig.Server.chainsawCleanRadius, context.getPos())) {
                    if (VxTags.VEGETATION.contains(world.getBlockState(targetPos).getBlock())) {
                        world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
                    }
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    private Iterable<BlockPos> getDestroyRadius(int radius, BlockPos origin) {
        if (radius <= 0) { return Collections.emptyList(); }
        BlockPos cornerOne = origin.add(radius, radius, radius);
        BlockPos cornerTwo = origin.add(-radius, -radius, -radius);
        return BlockPos.getAllInBoxMutable(cornerOne, cornerTwo);
    }
}
