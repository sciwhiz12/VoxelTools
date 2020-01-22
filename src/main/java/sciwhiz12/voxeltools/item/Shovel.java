package sciwhiz12.voxeltools.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.VoxelTools;
import sciwhiz12.voxeltools.VxConfig;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Shovel extends Item implements IVoxelTool {
	public static final ResourceLocation TAG_GROUND = new ResourceLocation(VoxelTools.MODID, "ground");

	public Shovel(Properties properties) {
		super(properties);
	}

	public Result onLeftClickBlock(PlayerEntity player, BlockPos pos, Direction face) {
		if (!player.world.isRemote && PermissionUtil.checkForPermission(player)) {
			Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_GROUND);
			for (BlockPos targetPos : getDigRadius(pos)) {
				if (col.contains(player.world.getBlockState(targetPos).getBlock())) {
					player.world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
				}
			}
			return Result.DENY;
		}
		return Result.DEFAULT;
	}

	public ActionResultType onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote && PermissionUtil.checkForPermission(context.getPlayer())) {
			if (context.getPlayer().isCrouching()) {
				if (VxConfig.SERVER.shovelFlattenRadius.get() == 0)
					return ActionResultType.PASS;
				Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_GROUND);
				World world = context.getWorld();
				for (BlockPos targetPos : getFlattenRadius(context.getPos())) {
					if (col.contains(world.getBlockState(targetPos).getBlock())) {
						world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
					}
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	private Iterable<BlockPos> getDigRadius(BlockPos origin) {
		int x = VxConfig.SERVER.shovelDigRadiusX.get();
		int y = VxConfig.SERVER.shovelDigRadiusY.get();
		int z = VxConfig.SERVER.shovelDigRadiusZ.get();
		BlockPos cornerOne = origin.add(x, y, z);
		BlockPos cornerTwo = origin.add(-x, -y, -z);
		return BlockPos.getAllInBoxMutable(cornerOne, cornerTwo);
	}

	private Iterable<BlockPos> getFlattenRadius(BlockPos origin) {
		int radius = VxConfig.SERVER.shovelFlattenRadius.get();
		int height = VxConfig.SERVER.shovelFlattenHeight.get();
		int offset = VxConfig.SERVER.shovelFlattenHeightOffset.get();
		BlockPos cornerOne = origin.add(radius, offset, radius);
		BlockPos cornerTwo = origin.add(-radius, offset + height, -radius);
		return BlockPos.getAllInBoxMutable(cornerOne, cornerTwo);
	}
}
