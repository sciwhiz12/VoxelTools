package sciwhiz12.voxeltools.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.VxConfig;
import sciwhiz12.voxeltools.util.MoveUtil;

public class Pliers extends BaseItem {
	public Pliers() {
		super(new Properties(), "pliers");
	}

	public Result onLeftClickBlock(PlayerEntity player, BlockPos pos, Direction face) {
		if (!player.world.isRemote && VxConfig.SERVER.hasPermission(player)) {
			BlockPos target = pos.offset(face, -1);
			if (MoveUtil.moveBlock(player, pos, target, false, true)) {
				return Result.DENY;
			}
		}
		return Result.DEFAULT;
	}

	public ActionResultType onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote && VxConfig.SERVER.hasPermission(context.getPlayer())) {
			PlayerEntity player = context.getPlayer();
			BlockPos target = context.getPos().offset(context.getFace());
			if (MoveUtil.moveBlock(player, context.getPos(), target, false, !player.isSneaking())) {
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}
}
