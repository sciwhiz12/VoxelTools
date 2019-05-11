package sciwhiz12.voxeltools.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event.Result;

public class Pliers extends BaseItem {
	public Pliers() {
		super(new Properties(), "pliers");
	}
	
	public Result onLeftClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) {
		if (!player.world.isRemote) {
			BlockPos target = pos.offset(face, -1);
			if (player.world.isAirBlock(target) || player.isSneaking()) {
				int flags = 2 | 64;
				IBlockState from = player.world.getBlockState(pos);
				player.world.playEvent(2001, pos, Block.getStateId(from));
				if (player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), flags)) {
					player.world.setBlockState(target, from, flags);
					return Result.DENY;
				}
			}
		}
		return Result.DEFAULT;
	}

	public EnumActionResult onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote) {
			BlockPos pos = context.getPos();
			World world = context.getWorld();
			BlockPos target = pos.offset(context.getFace());
			if (world.isAirBlock(target) || context.getPlayer().isSneaking()) {
				int flags = 2 | 64;
				IBlockState from = world.getBlockState(pos);
				world.playEvent(2001, pos, Block.getStateId(from));
				if (context.getPlayer().isSneaking() || world.setBlockState(pos, Blocks.AIR.getDefaultState(), flags))
					if (world.setBlockState(target, from, flags))
						return EnumActionResult.SUCCESS;
					else
						world.setBlockState(pos, from, flags);
			}
		}
		return EnumActionResult.PASS;
	}
}
