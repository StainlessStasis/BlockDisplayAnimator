package io.github.stainlessstasis.bdanimator.channel;

import io.github.stainlessstasis.bdanimator.easing.Easing;

public record Keyframe<T>(float time, T value, Easing easing) {}
