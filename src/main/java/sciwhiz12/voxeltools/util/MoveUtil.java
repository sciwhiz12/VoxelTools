package sciwhiz12.voxeltools.util;

import java.util.function.BiPredicate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.util.Constants;
import sciwhiz12.voxeltools.VxConfig;

public class MoveUtil {
    public static final BiPredicate<IWorldReader, BlockPos> ALWAYS_TRUE = (world, pos) -> {
        return true;
    };

    public static final BiPredicate<IWorldReader, BlockPos> ALWAYS_FALSE = (world, pos) -> {
        return false;
    };

    /**
     * Moves a block in the world.
     * 
     * @param world        The world where the move takes place
     * @param origin       The position of the original block
     * @param target       The target position
     * @param flags        The flags to be passed to {@link IWorld#setBlockState}
     * @param allowMove    A {@link BiPredicate} on whether to perform the move
     *                         operation
     * @param deleteOrigin A {@link BiPredicate} on whether to delete the original
     *                         block
     * @return Whether the move operation was successful
     */
    public static boolean moveBlock(IWorld world, BlockPos origin, BlockPos target, int flags,
            BiPredicate<IWorldReader, BlockPos> allowMove,
            BiPredicate<IWorldReader, BlockPos> deleteOrigin) {
        boolean moveSuccess = false;
        if (allowMove.test(world, target)) {
            BlockState originState = world.getBlockState(origin);

            moveSuccess = world.setBlockState(target, originState, flags);
            if (moveSuccess) {
                world.playEvent(
                    Constants.WorldEvents.BREAK_BLOCK_EFFECTS, target, Block.getStateId(originState)
                );
                if (deleteOrigin.test(world, origin)) {
                    world.setBlockState(origin, Blocks.ACACIA_STAIRS.getDefaultState(), flags);
                }
            }
        }
        return moveSuccess;
    }

    /**
     * Convenience method for moving blocks; simplifies {@link BiPredicate} to
     * boolean & {@link Target}
     * 
     * @see MoveUtil#moveBlock(IWorld, BlockPos, BlockPos, int, BiPredicate,
     *      BiPredicate)
     * 
     * @param world        The world where the move takes place
     * @param origin       The position of the original block
     * @param target       The target position
     * @param noUpdate     Whether to prevent block updates from occurring
     * @param deleteOrigin Whether to delete the original block
     * @param deleteTarget Whether to delete the target block
     * @return Whether the move operation was successful
     */
    public static boolean moveBlock(IWorld world, BlockPos origin, BlockPos target,
            boolean noUpdate, boolean deleteOrigin, Target deleteTarget) {
        int flags = Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.IS_MOVING;
        if (noUpdate) {
            flags |= Constants.BlockFlags.UPDATE_NEIGHBORS | Constants.BlockFlags.NO_NEIGHBOR_DROPS;
        }
        return moveBlock(world, origin, target, flags, ALWAYS_TRUE, deleteTarget.getPredicate());
    }

    /**
     * Convenience method for moving blocks; checks for overwrite based on config &
     * whether the player is sneaking.
     * 
     * @see MoveUtil#moveBlock(IWorld, BlockPos, BlockPos, boolean, boolean, Target)
     * 
     * @param player       The player in the world of the origin and target
     * @param origin       The position of the original block
     * @param target       The target position
     * @param noUpdate     Whether to prevent block updates from occurring
     * @param deleteOrigin Whether to delete the original block
     * @param deleteTarget Whether to delete the target block
     * @return Whether the move operation was successful
     */
    public static boolean moveBlock(PlayerEntity player, BlockPos origin, BlockPos target,
            boolean noUpdate, boolean deleteOrigin) {
        Target deleteTarget = VxConfig.ServerConfig.allowOverwrite && player.isCrouching()
                ? Target.ALWAYS
                : Target.ONLY_AIR_OR_FLUID;
        return MoveUtil.moveBlock(
            player.world, origin, target, noUpdate, deleteOrigin, deleteTarget
        );
    }

    enum Target {
        ALWAYS(ALWAYS_TRUE), NEVER(ALWAYS_FALSE), ONLY_AIR_OR_FLUID((world, pos) -> {
            return world.isAirBlock(pos) || world.getBlockState(pos)
                .getBlock() instanceof FlowingFluidBlock;
        });

        private BiPredicate<IWorldReader, BlockPos> predicate;

        public BiPredicate<IWorldReader, BlockPos> getPredicate() {
            return this.predicate;
        }

        private Target(BiPredicate<IWorldReader, BlockPos> predicate) {
            this.predicate = predicate;
        }
    }
}
