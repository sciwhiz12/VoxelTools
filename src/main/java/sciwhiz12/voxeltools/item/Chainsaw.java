package sciwhiz12.voxeltools.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sciwhiz12.voxeltools.VoxelTools;
import sciwhiz12.voxeltools.VxConfig;
import sciwhiz12.voxeltools.util.PermissionUtil;

import java.util.Collections;

public class Chainsaw extends Item implements ILeftClicker.OnBlock {
    public static final ResourceLocation TAG_VEGETATION = new ResourceLocation(VoxelTools.MODID, "vegetation");
    public static final ResourceLocation TAG_TREE_STUFF = new ResourceLocation(VoxelTools.MODID, "tree_stuff");

    public Chainsaw(Properties properties) {
        super(properties);
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction face) {
        if (player.isServerWorld() && PermissionUtil.checkForPermission(player)) {
            Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_TREE_STUFF);
            for (BlockPos targetPos : getDestroyRadius(VxConfig.ServerConfig.chainsawCutRadius, pos)) {
                if (col.contains(player.world.getBlockState(targetPos).getBlock())) {
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
        if (!world.isRemote && PermissionUtil.checkForPermission(context.getPlayer())) {
            if (!context.getPlayer().isCrouching()) {
                Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_VEGETATION);
                for (BlockPos targetPos : getDestroyRadius(VxConfig.ServerConfig.chainsawCleanRadius, context.getPos())) {
                    if (col.contains(world.getBlockState(targetPos).getBlock())) {
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
