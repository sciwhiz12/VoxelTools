package sciwhiz12.voxeltools.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class MoveUtil {
	public static boolean moveBlock(EntityPlayer player, BlockPos originPos, BlockPos targetPos, boolean noPhys,
			boolean deleteOrigin, Target deleteTarget) {
		if (deleteTarget == Target.ALWAYS || (deleteTarget == Target.ONLY_AIR && player.world.isAirBlock(targetPos))) {
			int flags = 2 | 64;
			if (noPhys)
				flags |= 16 | 32;
			IBlockState from = player.world.getBlockState(originPos);
			player.world.playEvent(2001, originPos, Block.getStateId(from));
			if (deleteOrigin && player.world.setBlockState(originPos, Blocks.AIR.getDefaultState(), flags)) {
				player.world.setBlockState(targetPos, from, flags);
				return true;
			}
		}
		return false;
	}

	public enum Target {
		ALWAYS, NEVER, ONLY_AIR
	}
}
