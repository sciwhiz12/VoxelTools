package sciwhiz12.voxeltools.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.util.MoveUtil;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Sledge extends Item implements IVoxelTool {
    public Sledge(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        BlockPos target = pos.offset(face, -1);
        MoveUtil.moveBlock(player, pos, target, false, true);
        return true;
    }

    @Override
    public Result hasLeftClickBlockAction(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        return PermissionUtil.checkForPermission(player) ? Result.ALLOW : Result.DEFAULT;
    }

    @Override
    public boolean onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        BlockPos target = pos.offset(face);
        MoveUtil.moveBlock(player, pos, target, player.isCrouching(), true);
        return true;
    }

    @Override
    public Result hasRightClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return PermissionUtil.checkForPermission(player) ? Result.ALLOW : Result.DEFAULT;
    }
}
