package sciwhiz12.voxeltools.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.VxConfig;
import sciwhiz12.voxeltools.VxConfig.ClickType;

public class Jackhammer extends BaseItem {

	public Jackhammer() {
		super(new Properties(), "jackhammer");
	}

	@Override
	public Result onLeftClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) {
		if (VxConfig.SERVER.allowItemUse.get() && !player.world.isRemote) {
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
	public EnumActionResult onItemUse(ItemUseContext context) {
		if (VxConfig.SERVER.allowItemUse.get() && !context.getWorld().isRemote) {
			context.getWorld().playEvent(2001, context.getPos(),
					Block.getStateId(context.getWorld().getBlockState(context.getPos())));
			int flags = 2;
			ClickType option = VxConfig.SERVER.jackhammerNoPhys.get();
			if (option == ClickType.RIGHT_CLICK_ONLY || option == ClickType.BOTH)
				flags |= 16 | 32;
			context.getWorld().setBlockState(context.getPos(), Blocks.AIR.getDefaultState(), flags);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
}
