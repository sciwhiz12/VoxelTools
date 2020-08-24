package sciwhiz12.voxeltools.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import sciwhiz12.voxeltools.net.SetFreezeTimePacket;
import sciwhiz12.voxeltools.net.VxNetwork;
import sciwhiz12.voxeltools.util.PermissionUtil;

public class Clock extends Item implements IScrollListener {
    public static final String TAG_ENABLED = "Active";
    public static final String TAG_FIXED_TIME = "StoredTime";
    public static final long TIME_SCROLL_INCREMENT = 500;

    public Clock(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote && PermissionUtil.checkForPermission(player)) {
            if (player.isCrouching()) {
                boolean newState = !isActive(stack);
                stack.getOrCreateTag().putBoolean(TAG_ENABLED, newState);
                float pitch = newState ? 1.1F : 0.9F;
                world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(),
                        SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.3F, pitch);
                checkConflicts(stack, player);
                checkActiveAndUpdate(stack, player);
            }
            printStatus(player, stack);
            return ActionResult.resultSuccess(stack);
        }
        return ActionResult.resultPass(stack);
    }

    // Client side only
    @Override
    public boolean shouldSendScrollEvent(ClientPlayerEntity player, double scrollDelta) {
        return player.isSneaking() && !player.isSpectator();
    }

    @Override
    public void onScroll(ItemStack stack, PlayerEntity player, double scrollDelta) {
        long time = getStoredTime(stack);
        time = (long) (time + TIME_SCROLL_INCREMENT * Math.copySign(1.0D, scrollDelta));
        setStoredTime(stack, time);
        checkActiveAndUpdate(stack, player);
        player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.UI_BUTTON_CLICK,
                SoundCategory.PLAYERS, 0.25F, 0.8F);
        printStatus(player, stack);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (isActive(stack)) { setActive(stack, false); }
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof PlayerEntity && isActive(stack)) {
            checkConflicts(stack, (PlayerEntity) entityIn);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return isActive(stack);
    }

    public void printStatus(PlayerEntity player, ItemStack stack) {
        IFormattableTextComponent status;
        if (isActive(stack)) {
            status = new TranslationTextComponent("status.voxeltools.clock.active").mergeStyle(TextFormatting.GREEN);
        } else {
            status = new TranslationTextComponent("status.voxeltools.clock.inactive").mergeStyle(TextFormatting.RED);
        }
        IFormattableTextComponent worldTime = new StringTextComponent(
                String.valueOf(parseTime(player.world.getDayTime() % 24000L)))
                .mergeStyle(TextFormatting.BLUE, TextFormatting.BOLD);
        IFormattableTextComponent storedTime = new StringTextComponent(String.valueOf(parseTime(getStoredTime(stack))))
                .mergeStyle(TextFormatting.GOLD, TextFormatting.BOLD);

        player.sendStatusMessage(new TranslationTextComponent("status.voxeltools.clock", worldTime, storedTime,
                status.mergeStyle(TextFormatting.BOLD)).mergeStyle(TextFormatting.DARK_GRAY), true);
    }

    public static void setStoredTime(ItemStack stack, long time) {
        if (time >= 24000L) time -= 24000L;
        if (time < 0) time += 24000L;
        stack.getOrCreateTag().putLong(TAG_FIXED_TIME, time);
    }

    public static long getStoredTime(ItemStack stack) {
        return stack.getOrCreateTag().getLong(TAG_FIXED_TIME);
    }

    public static boolean isActive(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(TAG_ENABLED);
    }

    public static void setActive(ItemStack stack, boolean active) {
        stack.getOrCreateTag().putBoolean(TAG_ENABLED, active);
    }

    public static void checkActiveAndUpdate(ItemStack stack, PlayerEntity player) {
        boolean freeze = isActive(stack);
        long time = 0;
        if (freeze) { time = (player.world.getDayTime() / 24000L) + getStoredTime(stack); }
        VxNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayerEntity) player)),
                new SetFreezeTimePacket(freeze, time));
    }

    public static void checkConflicts(ItemStack stack, PlayerEntity player) {
        for (NonNullList<ItemStack> inv : ImmutableList
                .of(player.inventory.mainInventory, player.inventory.offHandInventory)) {
            for (ItemStack invStack : inv) {
                if (!invStack.isEmpty() && invStack != stack && invStack.getItem() == VxItems.clock.get()) {
                    setActive(invStack, false);
                }
            }
        }
    }

    private static String parseTime(long time) {
        int hours = (int) ((time / 1000 + 6) % 24);
        int minutes = (int) (60 * (time % 1000) / 1000);
        return String.format("%02d:%02d", hours, minutes);
    }
}
