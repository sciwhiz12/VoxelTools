package sciwhiz12.voxeltools.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PaintbrushUtil {
    public static final String TAG_ID_BLOCKSTATE = "blockstate";
    public static final String TAG_ID_BLOCKNAME = "blockname";

    public static CompoundNBT storeBlockState(CompoundNBT tag, BlockState state) {
        if (tag.contains(TAG_ID_BLOCKSTATE)) tag.remove(TAG_ID_BLOCKSTATE);
        tag.putString(TAG_ID_BLOCKNAME, state.getBlock().getRegistryName().toString());
        CompoundNBT statetag = tag.getCompound(TAG_ID_BLOCKSTATE);
        if (!state.getProperties().isEmpty()) {
            for (IProperty<?> prop : state.getProperties()) {
                if (prop instanceof BooleanProperty) {
                    statetag.putBoolean(prop.getName(), (Boolean) state.get(prop));
                } else if (prop instanceof IntegerProperty) {
                    statetag.putInt(prop.getName(), (Integer) state.get(prop));
                } else if (prop instanceof DirectionProperty) {
                    statetag.putString(prop.getName(), ((Direction) state.get(prop)).getName());
                } else if (prop instanceof EnumProperty<?>) {
                    EnumProperty<?> enump = (EnumProperty<?>) prop;
                    statetag.putString(prop.getName(), state.get(enump).getName());
                }
            }
            tag.put(TAG_ID_BLOCKSTATE, statetag);
        } else {
            tag.remove(TAG_ID_BLOCKSTATE);
        }

        return tag;
    }

    public static <T extends Enum<T> & IStringSerializable> BlockState getBlockState(
            CompoundNBT tag) {
        Block b = getBlockFromName(tag.getString(TAG_ID_BLOCKNAME));
        BlockState state = b.getDefaultState();
        if (tag.contains(TAG_ID_BLOCKSTATE)) {
            CompoundNBT statetag = tag.getCompound(TAG_ID_BLOCKSTATE);
            for (IProperty<?> prop : state.getProperties()) {
                String propName = prop.getName();
                if (statetag.contains(propName)) {
                    if (prop instanceof BooleanProperty) {
                        state = state.with((BooleanProperty) prop, statetag.getBoolean(propName));
                    } else if (prop instanceof IntegerProperty) {
                        state = state.with((IntegerProperty) prop, statetag.getInt(propName));
                    } else if (prop instanceof DirectionProperty) {
                        state = state.with(
                                (DirectionProperty) prop, Direction.byName(
                                        statetag.getString(propName)
                                )
                        );
                    } else if (prop instanceof EnumProperty<?>) {
                        @SuppressWarnings("unchecked")
                        EnumProperty<T> enump = (EnumProperty<T>) prop;
                        state = state.with(
                                enump, enump.parseValue(statetag.getString(propName)).get()
                        );
                    }
                }
            }
        }
        return state;
    }

    public static String toStringFromState(BlockState state) {
        String str = "[";
        boolean first = true;
        for (IProperty<?> prop : state.getProperties()) {
            if (!first) { str += ","; }
            first = false;
            if (prop instanceof BooleanProperty) {
                str += prop.getName() + "=" + state.get(prop);
            } else if (prop instanceof IntegerProperty) {
                str += prop.getName() + "=" + state.get(prop);
            } else if (prop instanceof EnumProperty<?>) {
                str += prop.getName() + "=" + ((IStringSerializable) state.get(prop)).getName();
            }
        }
        return str + "]";
    }

    public static Block getBlockFromName(String resourceLocation) {
        return GameRegistry.findRegistry(Block.class).getValue(
                ResourceLocation.tryCreate(resourceLocation)
        );
    }

    public static RayTraceResult rangedRayTrace(World worldIn, PlayerEntity player,
            RayTraceContext.FluidMode fluidMode, double range) {
        float f = player.rotationPitch;
        float f1 = player.rotationYaw;
        Vec3d vec3d = player.getEyePosition(1.0F);
        float f2 = MathHelper.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -MathHelper.cos(-f * ((float) Math.PI / 180F));
        float f5 = MathHelper.sin(-f * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3d vec3d1 = vec3d.add((double) f6 * range, (double) f5 * range, (double) f7 * range);
        return worldIn.rayTraceBlocks(
                new RayTraceContext(
                        vec3d, vec3d1, RayTraceContext.BlockMode.OUTLINE, fluidMode, player
                )
        );
    }
}
