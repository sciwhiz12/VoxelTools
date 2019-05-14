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
		if (!player.world.isRemote && VxConfig.SERVER.hasPermission(player)) {
			Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_GROUND);
			for (MutableBlockPos targetPos : getDigRadius(pos)) {
				if (col.contains(player.world.getBlockState(targetPos).getBlock())) {
					player.world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
				}
			}
			return Result.DENY;
		}
		return Result.DEFAULT;
	}

	public EnumActionResult onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote && VxConfig.SERVER.hasPermission(context.getPlayer())
				&& context.getPlayer().isSneaking()) {
			if (VxConfig.SERVER.shovelFlattenRadius.get() == 0)
				return EnumActionResult.PASS;
			Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_GROUND);
			World world = context.getWorld();
			for (MutableBlockPos targetPos : getFlattenRadius(context.getPos())) {
				if (col.contains(world.getBlockState(targetPos).getBlock())) {
					world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
				}
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

	private Iterable<MutableBlockPos> getDigRadius(BlockPos origin) {
		int x = VxConfig.SERVER.shovelDigRadiusX.get();
		int y = VxConfig.SERVER.shovelDigRadiusY.get();
		int z = VxConfig.SERVER.shovelDigRadiusZ.get();
		BlockPos cornerOne = origin.add(x, y, z);
		BlockPos cornerTwo = origin.add(-x, -y, -z);
		return BlockPos.getAllInBoxMutable(cornerOne, cornerTwo);
	}

	private Iterable<MutableBlockPos> getFlattenRadius(BlockPos origin) {
		int radius = VxConfig.SERVER.shovelFlattenRadius.get();
		int height = VxConfig.SERVER.shovelFlattenHeight.get();
		int offset = VxConfig.SERVER.shovelFlattenHeightOffset.get();
		BlockPos cornerOne = origin.add(radius, offset, radius);
		BlockPos cornerTwo = origin.add(-radius, offset + height, -radius);
		return BlockPos.getAllInBoxMutable(cornerOne, cornerTwo);
	}
}
