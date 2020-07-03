package sciwhiz12.voxeltools.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Map.Entry;
import java.util.function.BiPredicate;

public class BlockUtil {
    public static final String TAG_ID_STOREDBLOCK = "StoredBlock";

    public static String toStringFromState(BlockState state) {
        StringBuilder str = new StringBuilder();
        str.append("[");
        boolean first = true;
        ImmutableMap<Property<?>, Comparable<?>> immutablemap = state.getValues();
        if (!immutablemap.isEmpty()) {
            for (Entry<Property<?>, Comparable<?>> entry : immutablemap.entrySet()) {
                if (!first) str.append(",");
                first = false;
                Property<?> prop = entry.getKey();
                str.append(prop.getName());
                str.append("=");
                str.append(getName(prop, entry.getValue()));
            }
        }
        return str.append("]").toString();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> String getName(Property<T> prop, Comparable<?> val) {
        return prop.getName((T) val);
    }

    public static RayTraceResult rangedRayTrace(World worldIn, PlayerEntity player, RayTraceContext.FluidMode fluidMode,
            double range) {
        float f = player.rotationPitch;
        float f1 = player.rotationYaw;
        Vector3d vec3d = player.getEyePosition(1.0F);
        float f2 = MathHelper.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -MathHelper.cos(-f * ((float) Math.PI / 180F));
        float f5 = MathHelper.sin(-f * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vector3d vec3d1 = vec3d.add((double) f6 * range, (double) f5 * range, (double) f7 * range);
        return worldIn
                .rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.OUTLINE, fluidMode, player));
    }

    /**
     * Moves a block in the world.
     *
     * @param world        The world where the move takes place
     * @param origin       The position of the original block
     * @param target       The target position
     * @param allowMove    A {@link BiPredicate} on whether to perform the move
     *                     operation
     * @param deleteOrigin A {@link BiPredicate} on whether to delete the original
     *                     block
     * @return Whether the move operation was successful
     */
    public static boolean moveBlock(World world, BlockPos origin, BlockPos target,
            BiPredicate<IWorldReader, BlockPos> allowMove, BiPredicate<IWorldReader, BlockPos> deleteOrigin) {
        boolean moveSuccess = false;
        if (allowMove.test(world, target)) {
            BlockState originState = world.getBlockState(origin);
            TileEntity originTile = world.getTileEntity(origin);

            moveSuccess = world.setBlockState(target, originState, Constants.BlockFlags.DEFAULT);

            if (moveSuccess) {
                if (deleteOrigin.test(world, origin)) {
                    world.setBlockState(origin, Blocks.AIR.getDefaultState(), Constants.BlockFlags.DEFAULT);
                    if (originTile != null) { world.setTileEntity(origin, null); }
                }
                if (originTile != null) {
                    originTile.setPos(target);
                    originTile.validate();
                    originTile.markDirty();
                    world.setTileEntity(target, originTile);
                }
                world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, target, Block.getStateId(originState));
            }
        }
        return moveSuccess;
    }
}
