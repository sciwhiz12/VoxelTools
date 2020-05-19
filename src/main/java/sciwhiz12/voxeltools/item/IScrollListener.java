package sciwhiz12.voxeltools.item;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IScrollListener {
    /**
     * Client only method; only called on client-side
     * @return whether to cancel the scroll event and send a packet to server
     */
    public boolean shouldSendScrollEvent(ClientPlayerEntity player, double scrollDelta);
    
    public void onScroll(ItemStack stack, PlayerEntity entity, double scrollDelta);
}
