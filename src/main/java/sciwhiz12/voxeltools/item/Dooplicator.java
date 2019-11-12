package sciwhiz12.voxeltools.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import sciwhiz12.voxeltools.VxConfig;

public class Dooplicator extends BaseItem {
	public Dooplicator() {
		super(new Properties(), "dooplicator");
	}

	public ActionResultType onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote && VxConfig.SERVER.hasPermission(context.getPlayer())) {
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
}
