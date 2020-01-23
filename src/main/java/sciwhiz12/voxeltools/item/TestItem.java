package sciwhiz12.voxeltools.item;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import sciwhiz12.voxeltools.event.ActionType;
import sciwhiz12.voxeltools.util.ChatUtil;

public class TestItem extends Item implements IVoxelTool {
    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onLeftClickEmpty(PlayerEntity player, World world, Hand hand) {
        printInfo("Left Click Empty", player, world, hand, null, null);
    }

    @Override
    public ActionType hasLeftClickEmptyAction(PlayerEntity player, World world, Hand hand) {
        return ActionType.CANCEL;
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        printInfo("Left Click Block", player, world, hand, pos, face);
    }

    @Override
    public ActionType hasLeftClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return ActionType.CANCEL;
    }

    @Override
    public void onRightClickEmpty(PlayerEntity player, World world, Hand hand) {
        printInfo("Right Click Empty", player, world, hand, null, null);
    }

    @Override
    public ActionType hasRightClickEmptyAction(PlayerEntity player, World world, Hand hand) {
        return ActionType.CANCEL;
    }

    @Override
    public void onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        printInfo("Right Click Block", player, world, hand, pos, face);;
    }

    @Override
    public ActionType hasRightClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return ActionType.CANCEL;
    }

    public static void printInfo(String infoString, PlayerEntity player, World world, Hand hand,
            @Nullable BlockPos pos, @Nullable Direction face) {
        Style whiteBold = new Style().setBold(true).setColor(TextFormatting.WHITE);
        ITextComponent text = new StringTextComponent("").appendSibling(
                new StringTextComponent("[TEST_ITEM] ").applyTextStyles(
                        TextFormatting.GREEN, TextFormatting.BOLD
                ).appendSibling(new StringTextComponent(infoString).setStyle(whiteBold))
        ).appendText("\n").appendSibling(
                new StringTextComponent("  logical side: ").applyTextStyle(TextFormatting.DARK_AQUA)
                        .appendSibling(
                                new StringTextComponent(world.isRemote ? "CLIENT" : "SERVER")
                                        .applyTextStyles(
                                                TextFormatting.ITALIC, TextFormatting.WHITE
                                        )
                        )
        ).appendText("\n").appendSibling(
                new StringTextComponent("  player: ").applyTextStyle(TextFormatting.DARK_AQUA)
                        .appendSibling(
                                player.getName().applyTextStyles(
                                        TextFormatting.ITALIC, TextFormatting.WHITE
                                )
                        )
        ).appendText("\n").appendSibling(
                new StringTextComponent("  hand: ").applyTextStyle(TextFormatting.DARK_AQUA)
                        .appendSibling(
                                new StringTextComponent(hand.toString()).applyTextStyles(
                                        TextFormatting.ITALIC, TextFormatting.WHITE
                                )
                        )
        );
        if (pos != null) {
            text.appendText("\n").appendSibling(
                    new StringTextComponent("  pos: ").applyTextStyle(TextFormatting.DARK_AQUA)
                            .appendSibling(
                                    new StringTextComponent(pos.toString()).applyTextStyles(
                                            TextFormatting.ITALIC, TextFormatting.WHITE
                                    )
                            )
            );
        }
        if (world != null) {
            text.appendText("\n").appendSibling(
                    new StringTextComponent("  dimension: ").applyTextStyle(
                            TextFormatting.DARK_AQUA
                    ).appendSibling(
                            new StringTextComponent(player.dimension.getRegistryName().toString())
                                    .applyTextStyles(TextFormatting.ITALIC, TextFormatting.WHITE)
                    )
            );
        }
        ChatUtil.sendIndexedMessage(player, text);
    }
}
