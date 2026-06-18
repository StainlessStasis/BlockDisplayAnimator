package io.github.stainlessstasis.bdanimator.animation;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public record VfxSnapshot(
        Vector3f translation, Vector3f scale, Vector3f rotation, Vector3f overlayColor, float overlayIntensity, BlockState blockState, ItemStack itemStack
) {
    public static final VfxSnapshot DEFAULT = new VfxSnapshot(
            new Vector3f(), new Vector3f(1), new Vector3f(), new Vector3f(1), 0f, Blocks.AIR.defaultBlockState(), ItemStack.EMPTY
    );
}
