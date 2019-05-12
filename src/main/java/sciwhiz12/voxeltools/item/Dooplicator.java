package sciwhiz12.voxeltools.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import sciwhiz12.voxeltools.VxConfig;

public class Dooplicator extends BaseItem {
	public Dooplicator() {
		super(new Properties(), "dooplicator");
	}

	public EnumActionResult onItemUse(ItemUseContext context) {
		if (VxConfig.SERVER.allowItemUse.get() && !context.getWorld().isRemote) {
			IBlockState state = context.getWorld().getBlockState(context.getPos());
			Item i = state.getBlock().asItem();
			if (i != null) {
				ItemStack stack = new ItemStack(state.getBlock().asItem());
				if (stack != null) {
					stack.setCount(stack.getMaxStackSize());
					if (context.getPlayer().inventory.addItemStackToInventory(stack))
						context.getPlayer()
								.sendMessage(new TextComponentTranslation("voxeltools.dooplicator.dooped",
										new TextComponentString(String.valueOf(stack.getMaxStackSize()))
												.applyTextStyle(TextFormatting.DARK_PURPLE),
										new TextComponentTranslation(state.getBlock().getTranslationKey())
												.applyTextStyle(TextFormatting.GREEN))
														.applyTextStyle(TextFormatting.BLUE));
				}
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
}
