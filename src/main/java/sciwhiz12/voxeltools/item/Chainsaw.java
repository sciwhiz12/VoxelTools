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

	public Result onLeftClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) {
		if (!player.world.isRemote && VxConfig.SERVER.hasPermission(player)) {
			Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_TREE_STUFF);
			for (MutableBlockPos targetPos : getDestroyRadius(VxConfig.SERVER.chainsawCutRadius, pos)) {
				if (col.contains(player.world.getBlockState(targetPos).getBlock())) {
					player.world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
				}
			}
			return Result.DENY;
		}
		return Result.DEFAULT;
	}

	public EnumActionResult onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote && VxConfig.SERVER.hasPermission(context.getPlayer())) {
			if (!context.getPlayer().isSneaking()) {
				if (VxConfig.SERVER.shovelFlattenRadius.get() == 0)
					return EnumActionResult.PASS;
				Tag<Block> col = BlockTags.getCollection().getOrCreate(TAG_VEGETATION);
				World world = context.getWorld();
				for (MutableBlockPos targetPos : getDestroyRadius(VxConfig.SERVER.chainsawCleanRadius, context.getPos())) {
					if (col.contains(world.getBlockState(targetPos).getBlock())) {
						world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
					}
				}
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}

	private Iterable<MutableBlockPos> getDestroyRadius(IntValue radiusConfig, BlockPos origin) {
		int r = radiusConfig.get();
		BlockPos cornerOne = origin.add(r, r, r);
		BlockPos cornerTwo = origin.add(-r, -r, -r);
		return BlockPos.getAllInBoxMutable(cornerOne, cornerTwo);
	}
}
