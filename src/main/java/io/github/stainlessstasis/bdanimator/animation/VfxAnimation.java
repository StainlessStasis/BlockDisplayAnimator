package io.github.stainlessstasis.bdanimator.animation;

import io.github.stainlessstasis.bdanimator.channel.BlockStateChannel;
import io.github.stainlessstasis.bdanimator.channel.KeyframedChannel;
import io.github.stainlessstasis.bdanimator.entity.VfxEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;
import java.util.function.Consumer;

public record VfxAnimation(
        KeyframedChannel<Vector3f, Vector3f> translationChannel,
        KeyframedChannel<Vector3f, Vector3f> scaleChannel,
        KeyframedChannel<Vector3f, Quaternionf> rotationChannel,
        KeyframedChannel<Vector3f, Vector3f> overlayColorChannel,
        KeyframedChannel<Float, float[]> overlayIntensityChannel,
        BlockStateChannel blockStateChannel,
        int durationTicks,
        @Nullable Consumer<VfxEntity> onStart,
        @Nullable Consumer<VfxEntity> onEnd,
        Map<Float, Consumer<VfxEntity>> keyframeCallbacks
) {}
