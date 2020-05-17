package sciwhiz12.voxeltools.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ILeftClicker {
    public interface OnEmpty extends ILeftClicker {
        public void onLeftClickEmpty(PlayerEntity player, World world, Hand hand);
    }

    public interface OnBlock extends ILeftClicker {
        public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
                Direction face);
    }

    public interface OnBoth extends OnEmpty, OnBlock {}
}
