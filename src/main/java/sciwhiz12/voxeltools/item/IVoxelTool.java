package sciwhiz12.voxeltools.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event.Result;

public interface IVoxelTool {
	public Result onLeftClickBlock(PlayerEntity player, BlockPos pos, Direction dir);

	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand);

	public ActionResultType onItemUse(ItemUseContext context);
}
