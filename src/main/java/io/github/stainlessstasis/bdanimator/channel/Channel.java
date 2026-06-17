package io.github.stainlessstasis.bdanimator.channel;

public interface Channel<T> {
    void evaluate(float t, T destination);
}
