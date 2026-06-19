package io.github.stainlessstasis.voxelfx.channel;

import io.github.stainlessstasis.voxelfx.easing.Easing;

public record Keyframe<T>(float time, T value, Easing easing) {}
