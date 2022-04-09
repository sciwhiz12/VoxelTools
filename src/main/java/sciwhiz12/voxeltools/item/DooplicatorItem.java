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
import sciwhiz12.voxeltools.util.PermissionUtil;

public class DooplicatorItem extends Item {
    public DooplicatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        PlayerEntity player = context.getPlayer();
        if (!world.isClientSide && player != null && PermissionUtil.checkForPermission(player)) {
            BlockState state = world.getBlockState(context.getClickedPos());
            Item i = state.getBlock().asItem();
            ItemStack stack = new ItemStack(state.getBlock().asItem());
            stack.setCount(stack.getMaxStackSize());
            if (player.inventory.add(stack)) {
                player.displayClientMessage(new TranslationTextComponent("status.voxeltools.dooplicator.dooped",
                        new StringTextComponent(String.valueOf(stack.getMaxStackSize()))
                                .withStyle(TextFormatting.DARK_PURPLE),
                        new TranslationTextComponent(state.getBlock().getDescriptionId()).withStyle(TextFormatting.GREEN))
                        .withStyle(TextFormatting.BLUE), false);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }
}
