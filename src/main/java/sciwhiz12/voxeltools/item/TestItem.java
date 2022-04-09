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
    public ActionResultType useOn(ItemUseContext context) {
        if (context.getPlayer() != null) {
            printInfo("onItemUse: RightClick on Block", context.getPlayer(), context.getLevel(), context.getHand(),
                    context.getClickedPos(), context.getClickedFace());
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        printInfo("onItemRightClick: RightClick on Empty", player, world, hand, null, null);
        return ActionResult.success(player.getItemInHand(hand));
    }

    public static void printInfo(String infoString, PlayerEntity player, World world, Hand hand, @Nullable BlockPos pos,
            @Nullable Direction face) {
        UnaryOperator<Style> whiteBold = style -> style.withBold(true).applyFormat(TextFormatting.WHITE);
        IFormattableTextComponent text = new StringTextComponent("")
                .append(new StringTextComponent("[TEST_ITEM] ").withStyle(TextFormatting.GREEN, TextFormatting.BOLD)
                        .append(new StringTextComponent(infoString).withStyle(whiteBold))).append("\n")
                .append(new StringTextComponent("  logical side: ").withStyle(TextFormatting.DARK_AQUA)
                        .append(new StringTextComponent(world.isClientSide ? "CLIENT" : "SERVER")
                                .withStyle(TextFormatting.ITALIC, TextFormatting.WHITE))).append("\n")
                .append(new StringTextComponent("  player: ").withStyle(TextFormatting.DARK_AQUA)
                        .append(player.getName().plainCopy().withStyle(TextFormatting.ITALIC, TextFormatting.WHITE)));
        if (hand != null) {
            text.append("\n").append(new StringTextComponent("  hand: ").withStyle(TextFormatting.DARK_AQUA)
                    .append(new StringTextComponent(hand.toString())
                            .withStyle(TextFormatting.ITALIC, TextFormatting.WHITE)));
        }
        if (pos != null) {
            text.append("\n").append(new StringTextComponent("  pos: ").withStyle(TextFormatting.DARK_AQUA)
                    .append(new StringTextComponent(pos.toString())
                            .withStyle(TextFormatting.ITALIC, TextFormatting.WHITE)));
        }
        text.append("\n").append(new StringTextComponent("  dimension: ").withStyle(TextFormatting.DARK_AQUA)
                .append(new StringTextComponent(player.level.dimension().location().toString())
                        .withStyle(TextFormatting.ITALIC, TextFormatting.WHITE)));
        player.displayClientMessage(text, false);
    }
}
