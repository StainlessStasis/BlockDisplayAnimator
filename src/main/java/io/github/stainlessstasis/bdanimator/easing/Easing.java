package io.github.stainlessstasis.bdanimator.easing;

public record Easing(EasingFunction formula) {
    public float apply(float t) {
        return formula.apply(t);
    }

    @FunctionalInterface
    public interface EasingFunction {
        float apply(float t);
    }
}
