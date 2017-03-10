package com.github.zubnix.rtcdcjava;

@FunctionalInterface
public interface OnDataChannelStringMessage {
    void onDataChannel(String message);
}