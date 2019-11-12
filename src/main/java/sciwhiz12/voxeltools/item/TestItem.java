package sciwhiz12.voxeltools.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event.Result;

public class TestItem extends BaseItem {
	public TestItem() {
		super(new Properties(), "test_item");
	}

	public Result onLeftClickBlock(PlayerEntity player, BlockPos pos, Direction face) {
		if (!player.world.isRemote) {
			player.sendMessage(new TranslationTextComponent("voxeltools.test_item.leftblock"));
			return Result.DENY;
		}
		return Result.DEFAULT;
	}

	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (!world.isRemote) {
			RayTraceResult res = rayTrace(world, player, FluidMode.NONE);
			if (res == null || res.getType() == Type.MISS) {
				player.sendMessage(new TranslationTextComponent("voxeltools.test_item.rightitem"));
				return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
			}
		}
		return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
	}

	public ActionResultType onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote) {
			context.getPlayer().sendMessage(new TranslationTextComponent("voxeltools.test_item.rightblock"));
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
