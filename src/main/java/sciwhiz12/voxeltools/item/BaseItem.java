package sciwhiz12.voxeltools.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.VoxelTools;

public class BaseItem extends Item {
	public BaseItem(Properties props, String regname) {
		super(props.setNoRepair().addToolType(ToolType.PICKAXE, ItemTier.DIAMOND.getHarvestLevel()).defaultMaxDamage(0)
				.maxStackSize(1));
		this.setRegistryName(VoxelTools.MODID, regname);
	}

	// used when left-clicked on a block
	// DENY if we have action, DEFAULT or ALLOW if we don't
	public Result onLeftClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) {
		return Result.DEFAULT;
	}

	// used when right-clicked on nothing
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	// used when right-clicked on a block
	public EnumActionResult onItemUse(ItemUseContext context) {
		return EnumActionResult.PASS;
	}
}
