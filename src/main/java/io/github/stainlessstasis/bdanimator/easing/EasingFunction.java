package io.github.stainlessstasis.bdanimator.easing;

@FunctionalInterface
public interface EasingFunction {
    float apply(float t);
}
