package sciwhiz12.voxeltools.item;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sciwhiz12.voxeltools.util.BlockUtil;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Pliers extends Item implements ILeftClicker.OnBlock {
    public Pliers(Properties properties) {
        super(properties);
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction face) {
        if (player.isServerWorld() && PermissionUtil.checkForPermission(player)) {
            BlockPos target = pos.offset(face.getOpposite());
            BlockUtil.moveBlock(world, pos, target, (w, p) -> player.isCrouching() || w.isAirBlock(p) || w.getBlockState(p)
                    .getBlock() instanceof FlowingFluidBlock, (w, p) -> false);
        }
        return;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (!world.isRemote && PermissionUtil.checkForPermission(context.getPlayer())) {
            BlockPos pos = context.getPos();
            BlockPos target = pos.offset(context.getFace());
            BlockUtil.moveBlock(world, pos, target,
                    (w, p) -> context.getPlayer().isCrouching() || w.isAirBlock(p) || w.getBlockState(p)
                            .getBlock() instanceof FlowingFluidBlock, (w, p) -> false);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
