package io.github.stainlessstasis.bdanimator.animation;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record VfxSnapshot(
        Vector3f translation, Vector3f scale, Quaternionf rotation, Vector3f overlayColor, float overlayIntensity, BlockState blockState
) {
    public static final VfxSnapshot DEFAULT = new VfxSnapshot(
            new Vector3f(0), new Vector3f(1), new Quaternionf(), new Vector3f(1), 0f, Blocks.AIR.defaultBlockState()
    );
}
