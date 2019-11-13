package sciwhiz12.voxeltools.event;

import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import sciwhiz12.voxeltools.item.IVoxelTool;

@EventBusSubscriber(bus = Bus.FORGE)
public class InteractListener {
	@SubscribeEvent
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		Item it = event.getItemStack().getItem();
		if (it instanceof IVoxelTool) {
			if (event.isCanceled())
				return;
			Result res = ((IVoxelTool) it).onLeftClickBlock(event.getPlayer(), event.getPos(), event.getFace());
			switch (res) {
			case DENY:
				event.setUseBlock(res);
				event.setUseItem(res);
				return;
			case DEFAULT:
			case ALLOW:
				return;
			}
		}
	}
}
