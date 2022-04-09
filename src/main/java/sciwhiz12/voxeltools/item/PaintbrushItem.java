package sciwhiz12.voxeltools.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
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
import net.minecraftforge.common.ForgeMod;
import sciwhiz12.voxeltools.VxConfig;
import sciwhiz12.voxeltools.client.render.PaintbrushRenderer;
import sciwhiz12.voxeltools.util.BlockUtil;
import sciwhiz12.voxeltools.util.PermissionUtil;

import javax.annotation.Nullable;
import java.util.List;

import static sciwhiz12.voxeltools.util.BlockUtil.toStringFromState;

public class PaintbrushItem extends Item implements ILeftClicker.OnBlock {
    public static final String TAG_ID_STOREDBLOCK = "StoredBlock";

    public PaintbrushItem(Properties properties) {
        super(properties.setISTER(() -> PaintbrushRenderer::new));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTag() || !stack.getOrCreateTag().contains(TAG_ID_STOREDBLOCK)) {
            tooltip.add(new TranslationTextComponent("tooltip.voxeltools.paintbrush.empty").withStyle(TextFormatting.GRAY));
            return;
        }
        BlockState state = NBTUtil.readBlockState(stack.getOrCreateTagElement(TAG_ID_STOREDBLOCK));
        tooltip.add(new TranslationTextComponent("tooltip.voxeltools.paintbrush.block.name",
                new TranslationTextComponent(state.getBlock().getDescriptionId()).withStyle(TextFormatting.GREEN))
                .withStyle(TextFormatting.GRAY));
        if (!state.getBlock().getStateDefinition().getProperties().isEmpty()) {
            if (Screen.hasShiftDown()) {
                tooltip.add(new TranslationTextComponent("tooltip.voxeltools.paintbrush.block.state",
                        new StringTextComponent(toStringFromState(state)).withStyle(TextFormatting.GREEN))
                        .withStyle(TextFormatting.GRAY));
            } else {
                tooltip.add(
                        new TranslationTextComponent("tooltip.voxeltools.paintbrush.sneak").withStyle(TextFormatting.GRAY));
            }
        }
    }

    @Override
    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction face) {
        if (player.isEffectiveAi() && PermissionUtil.checkForPermission(player)) {
            ItemStack stack = player.getItemInHand(hand);
            if (player.isCrouching()) {
                stack.removeTagKey(TAG_ID_STOREDBLOCK);
                sendStatus(player, "status.voxeltools.paintbrush.clear", TextFormatting.BLUE);
            } else {
                BlockState state = player.level.getBlockState(pos);
                stack.addTagElement(TAG_ID_STOREDBLOCK, NBTUtil.writeBlockState(state));
                sendStatus(player, "status.voxeltools.paintbrush.saved", TextFormatting.BLUE,
                        state.getBlock().getDescriptionId());
            }
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        return true;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (!world.isClientSide && PermissionUtil.checkForPermission(context.getPlayer())) {
            ItemStack stack = context.getItemInHand();
            if (!stack.hasTag() || !stack.getOrCreateTag().contains(TAG_ID_STOREDBLOCK)) {
                sendEmptyStatus(context.getPlayer());
                return ActionResultType.PASS;
            }
            BlockState state = NBTUtil.readBlockState(stack.getOrCreateTagElement(TAG_ID_STOREDBLOCK));
            world.setBlockAndUpdate(context.getClickedPos(), state);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide && PermissionUtil.checkForPermission(player)) {
            if (!stack.hasTag() || !stack.getOrCreateTag().contains(TAG_ID_STOREDBLOCK)) {
                sendEmptyStatus(player);
                return ActionResult.pass(stack);
            }
            BlockState state = NBTUtil.readBlockState(stack.getOrCreateTagElement(TAG_ID_STOREDBLOCK));
            double reach = 5.0F;
            ModifiableAttributeInstance reachAttr = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
            if (reachAttr != null) {
                reach = reachAttr.getValue();
            }
            if (player.isCrouching()) {
                reach = Math.max(VxConfig.Server.paintbrushRange, reach);
            }
            RayTraceResult trace = BlockUtil.rangedRayTrace(world, player, RayTraceContext.FluidMode.ANY, reach);
            if (trace.getType() == Type.BLOCK) {
                BlockPos pos = ((BlockRayTraceResult) trace).getBlockPos();
                world.setBlockAndUpdate(pos, state);
                player.swing(hand, true);
            } else if (trace.getType() == Type.MISS) {
                sendStatus(player, "status.voxeltools.paintbrush.current", TextFormatting.GRAY,
                        state.getBlock().getDescriptionId());
            }
            return ActionResult.success(stack);
        }
        return ActionResult.pass(stack);
    }

    private static void sendEmptyStatus(PlayerEntity player) {
        sendStatus(player, "status.voxeltools.paintbrush.empty", TextFormatting.RED);
    }

    private static void sendStatus(PlayerEntity player, String translationKey, TextFormatting customColor) {
        sendStatus(player, translationKey, customColor, null);
    }

    private static void sendStatus(PlayerEntity player, String translationKey, TextFormatting customColor, String extraKey) {
        ITextComponent extra = null;
        if (extraKey != null) {
            extra = new TranslationTextComponent(extraKey).withStyle(TextFormatting.GREEN);
        }
        player.displayClientMessage(new TranslationTextComponent(translationKey, extra).withStyle(customColor), true);
    }
}
