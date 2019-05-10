package sciwhiz12.voxeltools.item;

import sciwhiz12.voxeltools.VoxelTools;

public class TestItem extends BaseItem {
	public TestItem() {
		super(new Properties());
		this.setRegistryName(VoxelTools.MODID, "test_item");
	}
}
