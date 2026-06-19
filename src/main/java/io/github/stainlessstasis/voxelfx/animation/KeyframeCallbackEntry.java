package io.github.stainlessstasis.voxelfx.animation;

import io.github.stainlessstasis.voxelfx.entity.VfxEntity;

import java.util.function.Consumer;

public record KeyframeCallbackEntry(float time, Consumer<VfxEntity> callback) {
}
