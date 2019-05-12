package sciwhiz12.voxeltools.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.VxConfig;
import sciwhiz12.voxeltools.util.MoveUtil;

public class Pliers extends BaseItem {
	public Pliers() {
		super(new Properties(), "pliers");
	}

	public Result onLeftClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) {
		if (VxConfig.SERVER.allowItemUse.get() && !player.world.isRemote) {
			BlockPos target = pos.offset(face, -1);
			if (MoveUtil.moveBlock(player, pos, target, false, true)) {
				return Result.DENY;
			}
		}
		return Result.DEFAULT;
	}

	public EnumActionResult onItemUse(ItemUseContext context) {
		if (VxConfig.SERVER.allowItemUse.get() && !context.getWorld().isRemote) {
			EntityPlayer player = context.getPlayer();
			BlockPos target = context.getPos().offset(context.getFace());
			if (MoveUtil.moveBlock(player, context.getPos(), target, false, !player.isSneaking())) {
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}
}
