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
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Jackhammer extends Item implements IVoxelTool {
	public Jackhammer(Properties properties) {
		super(properties);
	}

	@Override
	public Result onLeftClickBlock(PlayerEntity player, BlockPos pos, Direction face) {
		if (!player.world.isRemote && PermissionUtil.checkForPermission(player)) {
			player.world.playEvent(2001, pos, Block.getStateId(player.world.getBlockState(pos)));
			player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			return Result.DENY;
		}
		return Result.DEFAULT;
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote && PermissionUtil.checkForPermission(context.getPlayer())) {
			context.getWorld().playEvent(2001, context.getPos(),
					Block.getStateId(context.getWorld().getBlockState(context.getPos())));
			context.getWorld().setBlockState(context.getPos(), Blocks.AIR.getDefaultState(), 2 | 16 | 32);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
