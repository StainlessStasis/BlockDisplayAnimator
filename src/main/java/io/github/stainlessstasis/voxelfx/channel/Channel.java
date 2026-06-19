package io.github.stainlessstasis.voxelfx.channel;

public interface Channel<T> {
    T evaluate(float t, T destination);
}
