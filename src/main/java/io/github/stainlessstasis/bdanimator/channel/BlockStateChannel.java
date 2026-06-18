package io.github.stainlessstasis.bdanimator.channel;

import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class BlockStateChannel {
    private final List<Keyframe<BlockState>> keyframes;

    public BlockStateChannel(List<Keyframe<BlockState>> keyframes) {
        this.keyframes = List.copyOf(keyframes);
    }

    public BlockStateChannel withStartValue(BlockState state) {
        List<Keyframe<BlockState>> newKeyframes = new ArrayList<>(keyframes);
        newKeyframes.set(0, new Keyframe<>(newKeyframes.getFirst().time(), state, newKeyframes.getFirst().easing()));

        // fixes inheritance without additional keyframes causing it to move toward default values
        if (newKeyframes.size() == 2) {
            newKeyframes.set(1, new Keyframe<>(newKeyframes.get(1).time(), state, newKeyframes.get(1).easing()));
        }

        return new BlockStateChannel(newKeyframes);
    }

    public BlockState getLastKeyframeValue() {
        return keyframes.getLast().value();
    }

    public BlockState evaluate(float t) {
        BlockState result = keyframes.getFirst().value();
        for (Keyframe<BlockState> keyframe : keyframes) {
            if (t >= keyframe.time()) {
                result = keyframe.value();
            } else {
                break;
            }
        }
        return result;
    }
}
