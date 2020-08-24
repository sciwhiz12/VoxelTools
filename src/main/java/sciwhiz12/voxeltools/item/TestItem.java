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
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.UnaryOperator;

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
        if (context.getPlayer() != null) {
            printInfo("onItemUse: RightClick on Block", context.getPlayer(), context.getWorld(), context.getHand(),
                    context.getPos(), context.getFace());
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        printInfo("onItemRightClick: RightClick on Empty", player, world, hand, null, null);
        return ActionResult.resultSuccess(player.getHeldItem(hand));
    }

    public static void printInfo(String infoString, PlayerEntity player, World world, Hand hand, @Nullable BlockPos pos,
            @Nullable Direction face) {
        UnaryOperator<Style> whiteBold = style -> style.setBold(true).applyFormatting(TextFormatting.WHITE);
        IFormattableTextComponent text = new StringTextComponent("").append(
                new StringTextComponent("[TEST_ITEM] ").mergeStyle(TextFormatting.GREEN, TextFormatting.BOLD)
                        .append(new StringTextComponent(infoString).modifyStyle(whiteBold))).appendString("\n")
                .append(new StringTextComponent("  logical side: ").mergeStyle(TextFormatting.DARK_AQUA)
                        .append(new StringTextComponent(world.isRemote ? "CLIENT" : "SERVER")
                                .mergeStyle(TextFormatting.ITALIC, TextFormatting.WHITE))).appendString("\n")
                .append(new StringTextComponent("  player: ").mergeStyle(TextFormatting.DARK_AQUA)
                        .append(player.getName().copyRaw()
                                .mergeStyle(TextFormatting.ITALIC, TextFormatting.WHITE)));
        if (hand != null) {
            text.appendString("\n").append(
                    new StringTextComponent("  hand: ").mergeStyle(TextFormatting.DARK_AQUA).append(
                            new StringTextComponent(hand.toString())
                                    .mergeStyle(TextFormatting.ITALIC, TextFormatting.WHITE)));
        }
        if (pos != null) {
            text.appendString("\n").append(
                    new StringTextComponent("  pos: ").mergeStyle(TextFormatting.DARK_AQUA).append(
                            new StringTextComponent(pos.toString())
                                    .mergeStyle(TextFormatting.ITALIC, TextFormatting.WHITE)));
        }
        text.appendString("\n").append(
                new StringTextComponent("  dimension: ").mergeStyle(TextFormatting.DARK_AQUA).append(
                        new StringTextComponent(player.world.getDimensionKey().func_240901_a_().toString())
                                .mergeStyle(TextFormatting.ITALIC, TextFormatting.WHITE)));
        player.sendStatusMessage(text, false);
    }
}
