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
import sciwhiz12.voxeltools.item.ClockItem;

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
            for (int i = 0; i < event.player.inventory.getContainerSize(); ++i) {
                ItemStack stack = event.player.inventory.getItem(i);
                if (stack.getItem() instanceof ClockItem && stack.getCount() > 0) {
                    if (ClockItem.isActive(stack)) {
                        hasClock = true;
                        clockStack = stack;
                        break;
                    }
                }
            }
            ItemStack stack = event.player.inventory.getCarried();
            if (stack.getItem() instanceof ClockItem && stack.getCount() > 0) {
                if (ClockItem.isActive(stack)) {
                    hasClock = true;
                    clockStack = stack;
                }
            }
            if (hasClock) {
                if (!freezeTime) {
                    freezeTime = true;
                    frozenTime = (event.player.level.getDayTime() / 24000L) + ClockItem.getStoredTime(clockStack);
                }
            } else {
                freezeTime = false;
            }
        }
    }

    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void onRenderTick(RenderTickEvent event) {
        if (Minecraft.getInstance().level != null && freezeTime) {
            if (event.phase == Phase.START) {
                previousTime = Minecraft.getInstance().level.getDayTime();
                Minecraft.getInstance().level.setDayTime(frozenTime);
            } else if (event.phase == Phase.END) {
                Minecraft.getInstance().level.setDayTime(previousTime);
            }
        }
    }
}
