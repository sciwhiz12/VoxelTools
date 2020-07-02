package sciwhiz12.voxeltools.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ILeftClicker {
    interface OnEmpty extends ILeftClicker {
        void onLeftClickEmpty(PlayerEntity player, World world, Hand hand);
    }

    interface OnBlock extends ILeftClicker {
        void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction face);
    }

    interface OnBoth extends OnEmpty, OnBlock {}
}
