package sciwhiz12.voxeltools.item;

import static sciwhiz12.voxeltools.util.BlockUtil.toStringFromState;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
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
import sciwhiz12.voxeltools.util.BlockUtil;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Paintbrush extends Item implements ILeftClicker.OnBlock {
    public static final String TAG_ID_STOREDBLOCK = "StoredBlock";

    public Paintbrush(Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn,
            List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTag() || !stack.getTag().contains(TAG_ID_STOREDBLOCK)) {
            tooltip.add(
                new TranslationTextComponent("tooltip.voxeltools.paintbrush.empty").applyTextStyle(
                    TextFormatting.GRAY
                )
            );
            return;
        }
        BlockState state = NBTUtil.readBlockState(stack.getChildTag(TAG_ID_STOREDBLOCK));
        tooltip.add(
            new TranslationTextComponent(
                "tooltip.voxeltools.paintbrush.blockname", new TranslationTextComponent(
                    state.getBlock().getTranslationKey()
                ).applyTextStyle(TextFormatting.GREEN)
            ).applyTextStyle(TextFormatting.GRAY)
        );
        if (!state.getBlock().getStateContainer().getProperties().isEmpty()) {
            if (Screen.hasShiftDown()) {
                tooltip.add(
                    new TranslationTextComponent(
                        "tooltip.voxeltools.paintbrush.blockstate", new StringTextComponent(
                            toStringFromState(state)
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
        if (player.isServerWorld() && PermissionUtil.checkForPermission(player)) {
            ItemStack stack = player.getHeldItem(hand);
            if (player.isCrouching()) {
                stack.removeChildTag(TAG_ID_STOREDBLOCK);
                sendStatus(player, "status.voxeltools.paintbrush.clear", TextFormatting.BLUE);
            } else {
                BlockState state = player.world.getBlockState(pos);
                stack.setTagInfo(TAG_ID_STOREDBLOCK, NBTUtil.writeBlockState(state));
                sendStatus(
                    player, "status.voxeltools.paintbrush.saved", TextFormatting.BLUE, state
                        .getBlock().getTranslationKey()
                );
            }
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        return true;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (!world.isRemote && PermissionUtil.checkForPermission(context.getPlayer())) {
            ItemStack stack = context.getItem();
            if (!stack.hasTag() || !stack.getTag().contains(TAG_ID_STOREDBLOCK)) {
                sendEmptyStatus(context.getPlayer());
                return ActionResultType.PASS;
            }
            BlockState state = NBTUtil.readBlockState(stack.getChildTag(TAG_ID_STOREDBLOCK));
            world.setBlockState(context.getPos(), state);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote && PermissionUtil.checkForPermission(player)) {
            if (!stack.hasTag() || !stack.getTag().contains(TAG_ID_STOREDBLOCK)) {
                sendEmptyStatus(player);
                return ActionResult.resultPass(stack);
            }
            BlockState state = NBTUtil.readBlockState(stack.getChildTag(TAG_ID_STOREDBLOCK));
            double reach = player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
            if (player.isCrouching()) {
                reach = Math.max(VxConfig.ServerConfig.paintbrushRange, reach);
            }
            RayTraceResult trace = BlockUtil.rangedRayTrace(
                world, player, RayTraceContext.FluidMode.ANY, reach
            );
            if (trace != null && trace.getType() == Type.BLOCK) {
                BlockPos pos = ((BlockRayTraceResult) trace).getPos();
                world.setBlockState(pos, state);
            } else if (trace == null || trace.getType() == Type.MISS) {
                sendStatus(
                    player, "status.voxeltools.paintbrush.current", TextFormatting.GRAY, state
                        .getBlock().getTranslationKey()
                );
            }
            return ActionResult.resultSuccess(stack);
        }
        return ActionResult.resultPass(stack);
    }

    private static void sendEmptyStatus(PlayerEntity player) {
        sendStatus(player, "status.voxeltools.paintbrush.empty", TextFormatting.RED);
    }

    private static void sendStatus(PlayerEntity player, String translationKey,
            TextFormatting customColor) {
        sendStatus(player, translationKey, customColor, null);
    }

    private static void sendStatus(PlayerEntity player, String translationKey,
            TextFormatting customColor, String extraKey) {
        ITextComponent extra = null;
        if (extraKey != null) {
            extra = new TranslationTextComponent((String) extraKey).applyTextStyle(
                TextFormatting.GREEN
            );
        }
        player.sendStatusMessage(
            new TranslationTextComponent(translationKey, extra).applyTextStyle(customColor), true
        );
    }
}
