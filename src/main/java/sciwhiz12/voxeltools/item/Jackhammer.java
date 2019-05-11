package sciwhiz12.voxeltools.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.Event.Result;

public class Jackhammer extends BaseItem {

	public Jackhammer() {
		super(new Properties(), "jackhammer");
	}

	@Override
	public Result onLeftClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) {
		if (!player.world.isRemote) {
			player.world.playEvent(2001, pos, Block.getStateId(player.world.getBlockState(pos)));
			player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2 | 16 | 32);
			return Result.DENY;
		}
		return Result.DEFAULT;
	}

	@Override
	public EnumActionResult onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote) {
			context.getWorld().playEvent(2001, context.getPos(), Block.getStateId(context.getWorld().getBlockState(context.getPos())));
			context.getWorld().setBlockState(context.getPos(), Blocks.AIR.getDefaultState(), 2 | 16 | 32);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
}
