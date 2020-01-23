package sciwhiz12.voxeltools.item;

import static sciwhiz12.voxeltools.util.BlockUtil.TAG_ID_BLOCKNAME;
import static sciwhiz12.voxeltools.util.BlockUtil.TAG_ID_BLOCKSTATE;
import static sciwhiz12.voxeltools.util.BlockUtil.getBlockFromName;
import static sciwhiz12.voxeltools.util.BlockUtil.getBlockState;
import static sciwhiz12.voxeltools.util.BlockUtil.rangedRayTrace;
import static sciwhiz12.voxeltools.util.BlockUtil.storeBlockState;
import static sciwhiz12.voxeltools.util.BlockUtil.toStringFromState;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sciwhiz12.voxeltools.VxConfig;
import sciwhiz12.voxeltools.event.ActionType;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Paintbrush extends Item implements IVoxelTool {
    public Paintbrush(Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn,
            List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tag = stack.getOrCreateTag();
        tooltip.add(
                new TranslationTextComponent(
                        "tooltip.voxeltools.paintbrush.blockname", new TranslationTextComponent(
                                getBlockFromName(tag.getString(TAG_ID_BLOCKNAME))
                                        .getTranslationKey()
                        ).applyTextStyle(TextFormatting.GREEN)
                ).applyTextStyle(TextFormatting.GRAY)
        );
        if (tag.contains(TAG_ID_BLOCKSTATE)) {
            if (Screen.hasShiftDown()) {
                tooltip.add(
                        new TranslationTextComponent(
                                "tooltip.voxeltools.paintbrush.blockstate", new StringTextComponent(
                                        toStringFromState(getBlockState(tag))
                                ).applyTextStyle(TextFormatting.GREEN)
                        ).applyTextStyle(TextFormatting.GRAY)
                );
            } else {
                tooltip.add(
                        new TranslationTextComponent("tooltip.voxeltools.paintbrush.sneak")
                                .applyTextStyle(TextFormatting.GRAY)
                );
            }
        }
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() != this) stack = player.getHeldItemOffhand();
        CompoundNBT tag = stack.getOrCreateTag();
        if (player.isCrouching()) {
            stack.setTag(storeBlockState(tag, Blocks.AIR.getDefaultState()));
            ((ServerPlayerEntity) player).sendStatusMessage(
                    new TranslationTextComponent("status.voxeltools.paintbrush.clear")
                            .applyTextStyle(TextFormatting.BLUE), true
            );
        } else {
            BlockState state = player.world.getBlockState(pos);
            stack.setTag(storeBlockState(tag, state));
            ((ServerPlayerEntity) player).sendStatusMessage(
                    new TranslationTextComponent(
                            "status.voxeltools.paintbrush.saved", new TranslationTextComponent(
                                    getBlockFromName(tag.getString(TAG_ID_BLOCKNAME))
                                            .getTranslationKey()
                            ).applyTextStyle(TextFormatting.GREEN)
                    ).applyTextStyle(TextFormatting.BLUE), true
            );
        }
    }

    @Override
    public ActionType hasLeftClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return PermissionUtil.checkForPermission(player) ? ActionType.CANCEL : ActionType.PASS;
    }

    @Override
    public void onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction face) {
        ItemStack stack = player.getHeldItem(hand);
        BlockState state = getBlockState(stack.getOrCreateTag());
        world.setBlockState(pos, state);
    }

    @Override
    public ActionType hasRightClickBlockAction(PlayerEntity player, World world, Hand hand,
            BlockPos pos, Direction face) {
        return PermissionUtil.checkForPermission(player) ? ActionType.CANCEL : ActionType.PASS;
    }

    @Override
    public void onRightClickEmpty(PlayerEntity player, World world, Hand hand) {
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.getItem() != this) stack = player.getHeldItemOffhand();
        double reach = player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
        if (player.isCrouching()) {
            reach = Math.max(VxConfig.ServerConfig.paintbrushRange, reach);
        }
        RayTraceResult trace = rangedRayTrace(world, player, RayTraceContext.FluidMode.ANY, reach);
        if (trace != null && trace.getType() == Type.BLOCK) {
            BlockState state = getBlockState(stack.getOrCreateTag());
            BlockPos pos = ((BlockRayTraceResult) trace).getPos();
            world.setBlockState(pos, state);
        } else if (trace == null || trace.getType() == Type.MISS) {
            ((ServerPlayerEntity) player).sendStatusMessage(
                    new TranslationTextComponent(
                            "status.voxeltools.paintbrush.current", new TranslationTextComponent(
                                    getBlockFromName(
                                            stack.getOrCreateTag().getString(TAG_ID_BLOCKNAME)
                                    ).getTranslationKey()
                            ).applyTextStyle(TextFormatting.GREEN)
                    ).applyTextStyle(TextFormatting.GRAY), true
            );
        }
    }

    @Override
    public ActionType hasRightClickEmptyAction(PlayerEntity player, World world, Hand hand) {
        return PermissionUtil.checkForPermission(player) ? ActionType.CANCEL : ActionType.PASS;
    }
}
