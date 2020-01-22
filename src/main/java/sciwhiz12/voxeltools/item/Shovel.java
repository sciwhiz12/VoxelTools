package sciwhiz12.voxeltools.item;

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
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.VoxelTools;
import sciwhiz12.voxeltools.VxConfig;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Shovel extends Item implements IVoxelTool {
    public static final ResourceLocation TAG_GROUND = new ResourceLocation(
            VoxelTools.MODID, "ground"
    );

    public Shovel(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_GROUND);
        for (BlockPos targetPos : getDigRadius(pos)) {
            if (col.contains(world.getBlockState(targetPos).getBlock())) {
                world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
            }
        }
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
        if (player.isCrouching()) {
            if (VxConfig.ServerConfig.shovelFlattenRadius == 0) return false;
            Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_GROUND);
            for (BlockPos targetPos : getFlattenRadius(pos)) {
                if (col.contains(world.getBlockState(targetPos).getBlock())) {
                    world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
                }
            }
        }
        return false;
    }

    @Override
    public Result hasRightClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return PermissionUtil.checkForPermission(player) ? Result.ALLOW : Result.DEFAULT;
    }

    private Iterable<BlockPos> getDigRadius(BlockPos origin) {
        int x = VxConfig.ServerConfig.shovelDigRadiusX;
        int y = VxConfig.ServerConfig.shovelDigRadiusY;
        int z = VxConfig.ServerConfig.shovelDigRadiusZ;
        BlockPos cornerOne = origin.add(x, y, z);
        BlockPos cornerTwo = origin.add(-x, -y, -z);
        return BlockPos.getAllInBoxMutable(cornerOne, cornerTwo);
    }

    private Iterable<BlockPos> getFlattenRadius(BlockPos origin) {
        int radius = VxConfig.ServerConfig.shovelFlattenRadius;
        int height = VxConfig.ServerConfig.shovelFlattenHeight;
        int offset = VxConfig.ServerConfig.shovelFlattenHeightOffset;
        BlockPos cornerOne = origin.add(radius, offset, radius);
        BlockPos cornerTwo = origin.add(-radius, offset + height, -radius);
        return BlockPos.getAllInBoxMutable(cornerOne, cornerTwo);
    }
}
