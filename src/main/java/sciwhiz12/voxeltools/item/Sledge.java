package sciwhiz12.voxeltools.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sciwhiz12.voxeltools.util.MoveUtil;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Sledge extends Item implements ILeftClicker.OnBlock {
    public Sledge(Properties properties) {
        super(properties);
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        if (player.isServerWorld() && PermissionUtil.checkForPermission(player)) {
            BlockPos target = pos.offset(face.getOpposite());
            MoveUtil.moveBlock(player, pos, target, false, true);
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        return true;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (!world.isRemote && PermissionUtil.checkForPermission(player)) {
            BlockPos pos = context.getPos();
            BlockPos target = pos.offset(context.getFace());
            MoveUtil.moveBlock(player, pos, target, player.isCrouching(), true);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
