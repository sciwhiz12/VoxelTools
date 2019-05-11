package sciwhiz12.voxeltools.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event.Result;

public class TestItem extends BaseItem {
	public TestItem() {
		super(new Properties(), "test_item");
	}

	public Result onLeftClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) {
		if (!player.world.isRemote) {
			player.sendMessage(new TextComponentTranslation("voxeltools.test_item.leftblock"));
			return Result.DENY;
		}
		return Result.DEFAULT;
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			RayTraceResult res = this.rayTrace(world, player, false);
			if (res == null || res.type == Type.MISS) {
				player.sendMessage(new TextComponentTranslation("voxeltools.test_item.rightitem"));
				return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
			}
		}
		return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
	}

	public EnumActionResult onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote) {
			context.getPlayer().sendMessage(new TextComponentTranslation("voxeltools.test_item.rightblock"));
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
}
