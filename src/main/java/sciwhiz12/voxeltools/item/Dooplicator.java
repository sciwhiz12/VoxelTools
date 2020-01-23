package sciwhiz12.voxeltools.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import sciwhiz12.voxeltools.event.ActionType;
import sciwhiz12.voxeltools.util.ChatUtil;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Dooplicator extends Item implements IVoxelTool {
    public Dooplicator(Properties properties) {
        super(properties);
    }

    @Override
    public void onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        BlockState state = world.getBlockState(pos);
        Item i = state.getBlock().asItem();
        if (i != null) {
            ItemStack stack = new ItemStack(state.getBlock().asItem());
            if (!stack.isEmpty()) {
                stack.setCount(stack.getMaxStackSize());
                if (player.inventory.addItemStackToInventory(stack)) {
                    ChatUtil.sendIndexedMessage(
                            player, new TranslationTextComponent(
                                    "voxeltools.dooplicator.dooped", new StringTextComponent(
                                            String.valueOf(stack.getMaxStackSize())
                                    ).applyTextStyle(TextFormatting.DARK_PURPLE),
                                    new TranslationTextComponent(
                                            state.getBlock().getTranslationKey()
                                    ).applyTextStyle(TextFormatting.GREEN)
                            ).applyTextStyle(TextFormatting.BLUE)
                    );
                }
            }
        }
    }

    @Override
    public ActionType hasRightClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return PermissionUtil.checkForPermission(player) ? ActionType.CANCEL : ActionType.PASS;
    }
}
