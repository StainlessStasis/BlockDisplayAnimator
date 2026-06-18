package io.github.stainlessstasis.bdanimator.channel;

public interface Channel<T> {
    T evaluate(float t, T destination);
}
