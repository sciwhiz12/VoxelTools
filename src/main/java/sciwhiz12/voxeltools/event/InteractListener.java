package sciwhiz12.voxeltools.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.PacketDistributor;
import sciwhiz12.voxeltools.VoxelTools;
import sciwhiz12.voxeltools.item.ILeftClicker;
import sciwhiz12.voxeltools.net.LeftClickEmptyPacket;
import sciwhiz12.voxeltools.net.VxNetwork;

@EventBusSubscriber(bus = Bus.FORGE, modid = VoxelTools.MODID)
public class InteractListener {
    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        // Client side event
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty() || !(stack.getItem() instanceof ILeftClicker.OnEmpty)) { return; }
        VxNetwork.CHANNEL.send(
            PacketDistributor.SERVER.noArg(), new LeftClickEmptyPacket(event.getHand())
        );
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty() || !(stack.getItem() instanceof ILeftClicker.OnBlock)) { return; }
        ILeftClicker.OnBlock item = (ILeftClicker.OnBlock) event.getItemStack().getItem();
        item.onLeftClickBlock(
            event.getPlayer(), event.getWorld(), event.getHand(), event.getPos(), event.getFace()
        );
        event.setUseBlock(Result.DENY);
        // event.setUseItem(Result.DENY);
        // event.setCanceled(true);
    }
}
