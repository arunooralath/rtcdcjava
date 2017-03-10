package com.github.zubnix.rtcdcjava;


import java.nio.ByteBuffer;

@FunctionalInterface
public interface OnEncryptedByteBufferChunk {
    void onEncrypted(ByteBuffer chunk);
}
