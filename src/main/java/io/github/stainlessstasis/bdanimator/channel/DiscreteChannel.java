package io.github.stainlessstasis.bdanimator.channel;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DiscreteChannel<T> implements Channel<T> {
    private final List<Keyframe<T>> keyframes;

    public DiscreteChannel(List<Keyframe<T>> keyframes) {
        this.keyframes = List.copyOf(keyframes);
    }

    public T getLastKeyframeValue() {
        return keyframes.getLast().value();
    }

    public T evaluate(float t, @Nullable T fallbackStartValue) {
        T result = (fallbackStartValue != null) ? fallbackStartValue : keyframes.getFirst().value();
        for (int i = 1; i < keyframes.size(); i++) {
            Keyframe<T> keyframe = keyframes.get(i);
            if (t >= keyframe.time()) {
                result = keyframe.value();
            } else {
                break;
            }
        }
        return result;
    }
}
