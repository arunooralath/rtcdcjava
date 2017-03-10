package com.github.zubnix.rtcdcjava;

@FunctionalInterface
public interface OnIceCandidate {
    void on(IceCandidate iceCandidate);
}
