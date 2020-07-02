package sciwhiz12.voxeltools.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import sciwhiz12.voxeltools.VoxelTools;
import sciwhiz12.voxeltools.item.Clock;

@EventBusSubscriber(value = Dist.CLIENT, bus = Bus.FORGE, modid = VoxelTools.MODID)
public class TimeInterceptor {

    public static volatile boolean freezeTime = false;
    public static volatile long frozenTime = 0L;
    private static volatile long previousTime = 0L;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        if (event.phase == Phase.START && event.side == LogicalSide.CLIENT) {
            boolean hasClock = false;
            ItemStack clockStack = ItemStack.EMPTY;
            for (int i = 0; i < event.player.inventory.getSizeInventory(); ++i) {
                ItemStack stack = event.player.inventory.getStackInSlot(i);
                if (stack.getItem() instanceof Clock && stack.getCount() > 0) {
                    if (Clock.isActive(stack)) {
                        hasClock = true;
                        clockStack = stack;
                        break;
                    }
                }
            }
            ItemStack stack = event.player.inventory.getItemStack();
            if (stack.getItem() instanceof Clock && stack.getCount() > 0) {
                if (Clock.isActive(stack)) {
                    hasClock = true;
                    clockStack = stack;
                }
            }
            if (hasClock) {
                if (!freezeTime) {
                    freezeTime = true;
                    frozenTime = (event.player.world.getDayTime() / 24000L) + Clock.getStoredTime(clockStack);
                }
            } else {
                freezeTime = false;
            }
        }
    }

    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void onRenderTick(RenderTickEvent event) {
        if (Minecraft.getInstance().world != null && freezeTime) {
            if (event.phase == Phase.START) {
                previousTime = Minecraft.getInstance().world.getDayTime();
                Minecraft.getInstance().world.setDayTime(frozenTime);
            } else if (event.phase == Phase.END) {
                Minecraft.getInstance().world.setDayTime(previousTime);
            }
        }
    }
}
