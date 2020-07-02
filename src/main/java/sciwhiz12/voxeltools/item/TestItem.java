package sciwhiz12.voxeltools.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import sciwhiz12.voxeltools.util.ChatUtil;

import javax.annotation.Nullable;

public class TestItem extends Item implements ILeftClicker.OnBoth {
    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onLeftClickEmpty(PlayerEntity player, World world, Hand hand) {
        printInfo("onLeftClickEmpty: LeftClick on Empty", player, world, hand, null, null);
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction face) {
        printInfo("onLeftClickBlock: LeftClick on Block", player, world, hand, null, null);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        printInfo("onItemUse: RightClick on Block", context.getPlayer(), context.getWorld(), context.getHand(),
                context.getPos(), context.getFace());
        return ActionResultType.SUCCESS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        printInfo("onItemRightClick: RightClick on Empty", player, world, hand, null, null);
        return ActionResult.resultSuccess(player.getHeldItem(hand));
    }

    public static void printInfo(String infoString, PlayerEntity player, World world, Hand hand, @Nullable BlockPos pos,
            @Nullable Direction face) {
        Style whiteBold = new Style().setBold(true).setColor(TextFormatting.WHITE);
        ITextComponent text = new StringTextComponent("").appendSibling(
                new StringTextComponent("[TEST_ITEM] ").applyTextStyles(TextFormatting.GREEN, TextFormatting.BOLD)
                        .appendSibling(new StringTextComponent(infoString).setStyle(whiteBold))).appendText("\n")
                .appendSibling(new StringTextComponent("  logical side: ").applyTextStyle(TextFormatting.DARK_AQUA)
                        .appendSibling(new StringTextComponent(world.isRemote ? "CLIENT" : "SERVER")
                                .applyTextStyles(TextFormatting.ITALIC, TextFormatting.WHITE))).appendText("\n")
                .appendSibling(new StringTextComponent("  player: ").applyTextStyle(TextFormatting.DARK_AQUA)
                        .appendSibling(player.getName().applyTextStyles(TextFormatting.ITALIC, TextFormatting.WHITE)));
        if (hand != null) {
            text.appendText("\n").appendSibling(new StringTextComponent("  hand: ").applyTextStyle(TextFormatting.DARK_AQUA)
                    .appendSibling(new StringTextComponent(hand.toString())
                            .applyTextStyles(TextFormatting.ITALIC, TextFormatting.WHITE)));
        }
        if (pos != null) {
            text.appendText("\n").appendSibling(new StringTextComponent("  pos: ").applyTextStyle(TextFormatting.DARK_AQUA)
                    .appendSibling(new StringTextComponent(pos.toString())
                            .applyTextStyles(TextFormatting.ITALIC, TextFormatting.WHITE)));
        }
        if (world != null) {
            text.appendText("\n").appendSibling(
                    new StringTextComponent("  dimension: ").applyTextStyle(TextFormatting.DARK_AQUA).appendSibling(
                            new StringTextComponent(player.dimension.getRegistryName().toString())
                                    .applyTextStyles(TextFormatting.ITALIC, TextFormatting.WHITE)));
        }
        ChatUtil.sendIndexedMessage(player, text);
    }
}
