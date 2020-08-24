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
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Shovel extends Item implements ILeftClicker.OnBlock {
    public static final ResourceLocation TAG_GROUND = new ResourceLocation(VoxelTools.MODID, "ground");

    public Shovel(Properties properties) {
        super(properties);
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction face) {
        if (player.isServerWorld() && PermissionUtil.checkForPermission(player)) {
            ITag<Block> col = BlockTags.getCollection().func_241834_b(TAG_GROUND);
            for (BlockPos targetPos : getDigRadius(pos)) {
                if (col.contains(world.getBlockState(targetPos).getBlock())) {
                    world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
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
        if (!world.isRemote && player != null && PermissionUtil
                .checkForPermission(player) && VxConfig.Server.shovelFlattenRadius != 0) {
            BlockPos pos = context.getPos();
            if (player.isCrouching()) {
                ITag<Block> col = BlockTags.getCollection().func_241834_b(TAG_GROUND);
                for (BlockPos targetPos : getFlattenRadius(pos)) {
                    if (col.contains(world.getBlockState(targetPos).getBlock())) {
                        world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
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
        BlockPos cornerOne = origin.add(x, y, z);
        BlockPos cornerTwo = origin.add(-x, -y, -z);
        return BlockPos.getAllInBoxMutable(cornerOne, cornerTwo);
    }

    private Iterable<BlockPos> getFlattenRadius(BlockPos origin) {
        int radius = VxConfig.Server.shovelFlattenRadius;
        int height = VxConfig.Server.shovelFlattenHeight;
        int offset = VxConfig.Server.shovelFlattenHeightOffset;
        BlockPos cornerOne = origin.add(radius, offset, radius);
        BlockPos cornerTwo = origin.add(-radius, offset + height, -radius);
        return BlockPos.getAllInBoxMutable(cornerOne, cornerTwo);
    }
}
