package sciwhiz12.voxeltools.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.VxConfig;
import sciwhiz12.voxeltools.VxConfig.ClickType;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Jackhammer extends Item implements IVoxelTool {
	public Jackhammer(Properties properties) {
		super(properties);
	}

	@Override
	public Result onLeftClickBlock(PlayerEntity player, BlockPos pos, Direction face) {
		if (!player.world.isRemote && PermissionUtil.checkForPermission(player)) {
			player.world.playEvent(2001, pos, Block.getStateId(player.world.getBlockState(pos)));
			int flags = 2;
			ClickType option = VxConfig.SERVER.jackhammerNoPhys.get();
			if (option == ClickType.LEFT_CLICK_ONLY || option == ClickType.BOTH)
				flags |= 16 | 32;
			player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), flags);
			return Result.DENY;
		}
		return Result.DEFAULT;
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote && PermissionUtil.checkForPermission(context.getPlayer())) {
			context.getWorld().playEvent(2001, context.getPos(),
					Block.getStateId(context.getWorld().getBlockState(context.getPos())));
			int flags = 2;
			ClickType option = VxConfig.SERVER.jackhammerNoPhys.get();
			if (option == ClickType.RIGHT_CLICK_ONLY || option == ClickType.BOTH)
				flags |= 16 | 32;
			context.getWorld().setBlockState(context.getPos(), Blocks.AIR.getDefaultState(), flags);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
