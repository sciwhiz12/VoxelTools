package sciwhiz12.voxeltools.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.Event.Result;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Dooplicator extends Item implements IVoxelTool {
	public Dooplicator(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote && PermissionUtil.checkForPermission(context.getPlayer())) {
			BlockState state = context.getWorld().getBlockState(context.getPos());
			Item i = state.getBlock().asItem();
			if (i != null) {
				ItemStack stack = new ItemStack(state.getBlock().asItem());
				if (stack != null) {
					stack.setCount(stack.getMaxStackSize());
					if (context.getPlayer().inventory.addItemStackToInventory(stack))
						context.getPlayer()
								.sendMessage(new TranslationTextComponent("voxeltools.dooplicator.dooped",
										new StringTextComponent(String.valueOf(stack.getMaxStackSize()))
												.applyTextStyle(TextFormatting.DARK_PURPLE),
										new TranslationTextComponent(state.getBlock().getTranslationKey())
												.applyTextStyle(TextFormatting.GREEN))
														.applyTextStyle(TextFormatting.BLUE));
				}
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	public Result onLeftClickBlock(PlayerEntity player, BlockPos pos, Direction dir) {
		return Result.DEFAULT;
	}
}
