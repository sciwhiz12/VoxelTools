package sciwhiz12.voxeltools.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.VoxelTools;
import sciwhiz12.voxeltools.VxConfig;

public class Chainsaw extends BaseItem {
	public static final ResourceLocation TAG_VEGETATION = new ResourceLocation(VoxelTools.MODID, "vegetation");
	public static final ResourceLocation TAG_TREE_STUFF = new ResourceLocation(VoxelTools.MODID, "tree_stuff");

	public Chainsaw() {
		super(new Properties(), "chainsaw");
	}

	public Result onLeftClickBlock(PlayerEntity player, BlockPos pos, Direction face) {
		if (!player.world.isRemote && VxConfig.SERVER.hasPermission(player)) {
			Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_TREE_STUFF);
			for (BlockPos targetPos : getDestroyRadius(VxConfig.SERVER.chainsawCutRadius, pos)) {
				if (col.contains(player.world.getBlockState(targetPos).getBlock())) {
					player.world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
				}
			}
			return Result.DENY;
		}
		return Result.DEFAULT;
	}

	public ActionResultType onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote && VxConfig.SERVER.hasPermission(context.getPlayer())) {
			if (!context.getPlayer().isSneaking()) {
				if (VxConfig.SERVER.shovelFlattenRadius.get() == 0)
					return ActionResultType.PASS;
				Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_VEGETATION);
				World world = context.getWorld();
				for (BlockPos targetPos : getDestroyRadius(VxConfig.SERVER.chainsawCleanRadius, context.getPos())) {
					if (col.contains(world.getBlockState(targetPos).getBlock())) {
						world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
					}
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	private Iterable<BlockPos> getDestroyRadius(IntValue radiusConfig, BlockPos origin) {
		int r = radiusConfig.get();
		BlockPos cornerOne = origin.add(r, r, r);
		BlockPos cornerTwo = origin.add(-r, -r, -r);
		return BlockPos.getAllInBoxMutable(cornerOne, cornerTwo);
	}
}
