package com.github.zubnix.rtcdcjava;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface OnDataChannelByteBufferMessage {
    void onDataChannel(ByteBuffer message);
}
