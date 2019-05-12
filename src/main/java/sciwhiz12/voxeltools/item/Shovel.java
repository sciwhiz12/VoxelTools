package sciwhiz12.voxeltools.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.VoxelTools;
import sciwhiz12.voxeltools.VxConfig;

public class Shovel extends BaseItem {
	public static final ResourceLocation TAG_GROUND = new ResourceLocation(VoxelTools.MODID, "ground");

	public Shovel() {
		super(new Properties(), "shovel");
	}

	public Result onLeftClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) {
		if (VxConfig.SERVER.allowItemUse.get() && !player.world.isRemote) {
			Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_GROUND);
			for (MutableBlockPos targetPos : BlockPos.getAllInBoxMutable(pos.add(1, 1, 1), pos.add(-1, -1, -1))) {
				if (col.contains(player.world.getBlockState(targetPos).getBlock())) {
					player.world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
				}
			}
			return Result.DENY;
		}
		return Result.DEFAULT;
	}

	public EnumActionResult onItemUse(ItemUseContext context) {
		if (VxConfig.SERVER.allowItemUse.get() && !context.getWorld().isRemote && context.getPlayer().isSneaking()) {
			Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_GROUND);
			BlockPos centerPos = context.getPos();
			World world = context.getWorld();
			for (MutableBlockPos targetPos : BlockPos.getAllInBoxMutable(centerPos.add(2, 1, 2),
					centerPos.add(-2, 1, -2))) {
				if (col.contains(world.getBlockState(targetPos).getBlock())) {
					world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
				}
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
}