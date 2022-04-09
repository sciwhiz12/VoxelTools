package sciwhiz12.voxeltools.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.PacketDistributor;
import sciwhiz12.voxeltools.VoxelTools;
import sciwhiz12.voxeltools.item.IScrollListener;
import sciwhiz12.voxeltools.net.ScrollPacket;
import sciwhiz12.voxeltools.net.VxNetwork;

@EventBusSubscriber(value = Dist.CLIENT, bus = Bus.FORGE, modid = VoxelTools.MODID)
public class ScrollListener {
    @SubscribeEvent
    public static void onScrollInput(InputEvent.MouseScrollEvent event) {
        // Client side event
        @SuppressWarnings("resource")
        ClientPlayerEntity player = Minecraft.getInstance().player;
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof IScrollListener && !event.isCanceled()) {
                boolean sendPacket = ((IScrollListener) stack.getItem())
                        .shouldSendScrollEvent(player, event.getScrollDelta());
                if (sendPacket) {
                    event.setCanceled(true);
                    VxNetwork.CHANNEL.send(PacketDistributor.SERVER.noArg(), new ScrollPacket(hand, event.getScrollDelta()));
                }
            }
        }
    }
}
