package io.github.stainlessstasis.bdanimator.channel;

import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class KeyframedChannel<S, T> implements Channel<T> {
    private final List<Keyframe<S>> keyframes;
    private final LerpFunction<S, T> lerpFunc;

    public KeyframedChannel(List<Keyframe<S>> keyframes, LerpFunction<S, T> lerpFunc) {
        this.keyframes = List.copyOf(keyframes);
        this.lerpFunc = lerpFunc;
    }

    @Override
    public void evaluate(float t, T destination) {
        Keyframe<S> prev = keyframes.getFirst();
        Keyframe<S> next = keyframes.getLast();

        for (int i = 0; i < keyframes.size() - 1; i++) {
            if (t <= keyframes.get(i + 1).time()) {
                prev = keyframes.get(i);
                next = keyframes.get(i + 1);
                break;
            }
        }

        float segmentT = Mth.inverseLerp(t, prev.time(), next.time());
        float easedT = next.easing().apply(segmentT);
        lerpFunc.lerp(prev.value(), next.value(), easedT, destination);
    }

    public KeyframedChannel<S, T> withStartValue(S startValue) {
        List<Keyframe<S>> newKeyframes = new ArrayList<>(keyframes);
        newKeyframes.set(0, new Keyframe<>(newKeyframes.getFirst().time(), startValue, newKeyframes.getFirst().easing()));

        // fixes inheritance without additional keyframes causing it to move toward default values
        if (newKeyframes.size() == 2) {
            newKeyframes.set(1, new Keyframe<>(newKeyframes.get(1).time(), startValue, newKeyframes.get(1).easing()));
        }

        return new KeyframedChannel<>(newKeyframes, lerpFunc);
    }

    public S getLastKeyframeValue() {
        return keyframes.getLast().value();
    }

    @FunctionalInterface
    public interface LerpFunction<S, T> {
        void lerp(S start, S end, float t, T destination);
    }
}
