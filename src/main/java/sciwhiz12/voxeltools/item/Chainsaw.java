package sciwhiz12.voxeltools.item;

import java.util.Collections;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sciwhiz12.voxeltools.VoxelTools;
import sciwhiz12.voxeltools.VxConfig;
import sciwhiz12.voxeltools.event.ActionType;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Chainsaw extends Item implements IVoxelTool {
    public static final ResourceLocation TAG_VEGETATION = new ResourceLocation(
            VoxelTools.MODID, "vegetation"
    );
    public static final ResourceLocation TAG_TREE_STUFF = new ResourceLocation(
            VoxelTools.MODID, "tree_stuff"
    );

    public Chainsaw(Properties properties) {
        super(properties);
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_TREE_STUFF);
        for (BlockPos targetPos : getDestroyRadius(VxConfig.ServerConfig.chainsawCutRadius, pos)) {
            if (col.contains(player.world.getBlockState(targetPos).getBlock())) {
                player.world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
            }
        }
    }

    @Override
    public ActionType hasLeftClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return PermissionUtil.checkForPermission(player) ? ActionType.CONTINUE : ActionType.PASS;
    }

    @Override
    public void onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        if (!player.isCrouching()) {
            Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_VEGETATION);
            for (BlockPos targetPos : getDestroyRadius(
                    VxConfig.ServerConfig.chainsawCleanRadius, pos
            )) {
                if (col.contains(world.getBlockState(targetPos).getBlock())) {
                    world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
                }
            }
        }
    }

    @Override
    public ActionType hasRightClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return PermissionUtil.checkForPermission(player) ? ActionType.CONTINUE : ActionType.PASS;
    }

    private Iterable<BlockPos> getDestroyRadius(int radius, BlockPos origin) {
        if (radius <= 0) { return Collections.emptyList(); }
        BlockPos cornerOne = origin.add(radius, radius, radius);
        BlockPos cornerTwo = origin.add(-radius, -radius, -radius);
        return BlockPos.getAllInBoxMutable(cornerOne, cornerTwo);
    }
}
