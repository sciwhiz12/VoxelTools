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
import sciwhiz12.voxeltools.util.ChatUtil;

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
        IFormattableTextComponent text = new StringTextComponent("").func_230529_a_(
                new StringTextComponent("[TEST_ITEM] ").func_240701_a_(TextFormatting.GREEN, TextFormatting.BOLD)
                        .func_230529_a_(new StringTextComponent(infoString).func_240700_a_(whiteBold))).func_240702_b_("\n")
                .func_230529_a_(new StringTextComponent("  logical side: ").func_240699_a_(TextFormatting.DARK_AQUA)
                        .func_230529_a_(new StringTextComponent(world.isRemote ? "CLIENT" : "SERVER")
                                .func_240701_a_(TextFormatting.ITALIC, TextFormatting.WHITE))).func_240702_b_("\n")
                .func_230529_a_(new StringTextComponent("  player: ").func_240699_a_(TextFormatting.DARK_AQUA)
                        .func_230529_a_(player.getName().copyRaw()
                                .func_240701_a_(TextFormatting.ITALIC, TextFormatting.WHITE)));
        if (hand != null) {
            text.func_240702_b_("\n").func_230529_a_(
                    new StringTextComponent("  hand: ").func_240699_a_(TextFormatting.DARK_AQUA).func_230529_a_(
                            new StringTextComponent(hand.toString())
                                    .func_240701_a_(TextFormatting.ITALIC, TextFormatting.WHITE)));
        }
        if (pos != null) {
            text.func_240702_b_("\n").func_230529_a_(
                    new StringTextComponent("  pos: ").func_240699_a_(TextFormatting.DARK_AQUA).func_230529_a_(
                            new StringTextComponent(pos.toString())
                                    .func_240701_a_(TextFormatting.ITALIC, TextFormatting.WHITE)));
        }
        text.func_240702_b_("\n").func_230529_a_(
                new StringTextComponent("  dimension: ").func_240699_a_(TextFormatting.DARK_AQUA).func_230529_a_(
                        new StringTextComponent(player.world.func_234922_V_().func_240901_a_().toString())
                                .func_240701_a_(TextFormatting.ITALIC, TextFormatting.WHITE)));
        ChatUtil.sendIndexedMessage(player, text);
    }
}
