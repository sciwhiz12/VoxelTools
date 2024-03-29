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

public class PliersItem extends Item implements ILeftClicker.OnBlock {
    public PliersItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction face) {
        if (player.isEffectiveAi() && PermissionUtil.checkForPermission(player)) {
            BlockPos target = pos.relative(face.getOpposite());
            BlockUtil.moveBlock(world, pos, target, (w, p) -> player.isCrouching() || w.isEmptyBlock(p) || w.getBlockState(p)
                    .getBlock() instanceof FlowingFluidBlock, (w, p) -> false);
        }
        return;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (!world.isClientSide && PermissionUtil.checkForPermission(context.getPlayer())) {
            BlockPos pos = context.getClickedPos();
            BlockPos target = pos.relative(context.getClickedFace());
            BlockUtil.moveBlock(world, pos, target,
                    (w, p) -> context.getPlayer().isCrouching() || w.isEmptyBlock(p) || w.getBlockState(p)
                            .getBlock() instanceof FlowingFluidBlock, (w, p) -> false);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
