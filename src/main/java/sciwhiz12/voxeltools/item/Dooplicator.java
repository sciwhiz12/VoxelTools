package sciwhiz12.voxeltools.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import sciwhiz12.voxeltools.util.ChatUtil;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Dooplicator extends Item {
    public Dooplicator(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (!world.isRemote && PermissionUtil.checkForPermission(context.getPlayer())) {
            BlockState state = world.getBlockState(context.getPos());
            Item i = state.getBlock().asItem();
            if (i != null) {
                ItemStack stack = new ItemStack(state.getBlock().asItem());
                stack.setCount(stack.getMaxStackSize());
                if (context.getPlayer().inventory.addItemStackToInventory(stack)) {
                    ChatUtil.sendIndexedMessage(
                        context.getPlayer(), new TranslationTextComponent(
                            "voxeltools.dooplicator.dooped", new StringTextComponent(
                                String.valueOf(stack.getMaxStackSize())
                            ).applyTextStyle(TextFormatting.DARK_PURPLE),
                            new TranslationTextComponent(state.getBlock().getTranslationKey())
                                .applyTextStyle(TextFormatting.GREEN)
                        ).applyTextStyle(TextFormatting.BLUE)
                    );
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }
}
