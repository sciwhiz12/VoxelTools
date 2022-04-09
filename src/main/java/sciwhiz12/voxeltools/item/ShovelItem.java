package sciwhiz12.voxeltools.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sciwhiz12.voxeltools.VoxelTools;
import sciwhiz12.voxeltools.VxConfig;
import sciwhiz12.voxeltools.VxTags;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class ShovelItem extends Item implements ILeftClicker.OnBlock {
    public ShovelItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction face) {
        if (player.isEffectiveAi() && PermissionUtil.checkForPermission(player)) {
            for (BlockPos targetPos : getDigRadius(pos)) {
                if (VxTags.GROUND.contains(world.getBlockState(targetPos).getBlock())) {
                    world.setBlockAndUpdate(targetPos, Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        return true;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        PlayerEntity player = context.getPlayer();
        if (!world.isClientSide && player != null && PermissionUtil
                .checkForPermission(player) && VxConfig.Server.shovelFlattenRadius != 0) {
            BlockPos pos = context.getClickedPos();
            if (player.isCrouching()) {
                for (BlockPos targetPos : getFlattenRadius(pos)) {
                    if (VxTags.GROUND.contains(world.getBlockState(targetPos).getBlock())) {
                        world.setBlockAndUpdate(targetPos, Blocks.AIR.defaultBlockState());
                    }
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    private Iterable<BlockPos> getDigRadius(BlockPos origin) {
        int x = VxConfig.Server.shovelDigRadiusX;
        int y = VxConfig.Server.shovelDigRadiusY;
        int z = VxConfig.Server.shovelDigRadiusZ;
        BlockPos cornerOne = origin.offset(x, y, z);
        BlockPos cornerTwo = origin.offset(-x, -y, -z);
        return BlockPos.betweenClosed(cornerOne, cornerTwo);
    }

    private Iterable<BlockPos> getFlattenRadius(BlockPos origin) {
        int radius = VxConfig.Server.shovelFlattenRadius;
        int height = VxConfig.Server.shovelFlattenHeight;
        int offset = VxConfig.Server.shovelFlattenHeightOffset;
        BlockPos cornerOne = origin.offset(radius, offset, radius);
        BlockPos cornerTwo = origin.offset(-radius, offset + height, -radius);
        return BlockPos.betweenClosed(cornerOne, cornerTwo);
    }
}
