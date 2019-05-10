package sciwhiz12.voxeltools.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraftforge.common.ToolType;

public class BaseItem extends Item {
	public BaseItem(Properties props) {
		super(props.setNoRepair().addToolType(ToolType.PICKAXE, ItemTier.DIAMOND.getHarvestLevel()).defaultMaxDamage(0)
				.maxStackSize(1));
	}
}
