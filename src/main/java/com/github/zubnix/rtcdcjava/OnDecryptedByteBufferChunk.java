package com.github.zubnix.rtcdcjava;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface OnDecryptedByteBufferChunk {
    void onDencrypted(ByteBuffer chunk);
}
