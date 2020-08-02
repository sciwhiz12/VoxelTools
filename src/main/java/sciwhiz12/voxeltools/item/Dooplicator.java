package sciwhiz12.voxeltools.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
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
        PlayerEntity player = context.getPlayer();
        if (!world.isRemote && player != null && PermissionUtil.checkForPermission(player)) {
            BlockState state = world.getBlockState(context.getPos());
            Item i = state.getBlock().asItem();
            ItemStack stack = new ItemStack(state.getBlock().asItem());
            stack.setCount(stack.getMaxStackSize());
            if (player.inventory.addItemStackToInventory(stack)) {
                ChatUtil.sendIndexedMessage(context.getPlayer(),
                        new TranslationTextComponent("voxeltools.dooplicator.dooped",
                                new StringTextComponent(String.valueOf(stack.getMaxStackSize()))
                                        .mergeStyle(TextFormatting.DARK_PURPLE),
                                new TranslationTextComponent(state.getBlock().getTranslationKey())
                                        .mergeStyle(TextFormatting.GREEN)).mergeStyle(TextFormatting.BLUE));
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }
}
