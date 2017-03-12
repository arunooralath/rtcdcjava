package com.github.zubnix.rtcdcjava;


import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

public class DTLSWrapper {

    private final ArrayBlockingQueue<ByteBuffer> encryptionQueue = new ArrayBlockingQueue<>(128);
    private final ArrayBlockingQueue<ByteBuffer> decryptionQueue = new ArrayBlockingQueue<>(128);

    @Nonnull
    private Optional<OnEncryptedByteBufferChunk> onEncryptedByteBufferChunk = Optional.empty();
    @Nonnull
    private Optional<OnDecryptedByteBufferChunk> onDecryptedByteBufferChunk = Optional.empty();


    public RTCCertificate certificate() {
        return null;
    }

    public boolean init() {
        return false;
    }

    /**
     * Decrypts a chunk. Blocks if no chunk is available. After decryption, the {@link OnDecryptedByteBufferChunk} callback is called. Optionally, this method can be continuously called by a separate decryption thread.
     */
    public void doDecryption() {
        //TODO find out how to do bytebuffer decrypton with bc
    }

    /**
     * Encrypts a chunk. Blocks if no chunk is available. After encryption, the {@link OnEncryptedByteBufferChunk} callback is called. Optionally, this method can be continuously called by a separate decryption thread.
     */
    public void doEncryption() {
        //TODO find out how to do bytebuffer encrypton with bc

    }

    public void encrypt(@Nonnull ByteBuffer data) {
        this.encryptionQueue.add(data);
    }

    public void decrypt(@Nonnull ByteBuffer data) {
        this.decryptionQueue.add(data);
    }

    public void set(@Nonnull OnEncryptedByteBufferChunk onEncryptedByteBufferChunk) {
        this.onEncryptedByteBufferChunk = Optional.of(onEncryptedByteBufferChunk);
    }

    public void removeOnEncryptedByteBufferChunk() {
        this.onEncryptedByteBufferChunk = Optional.empty();
    }

    public void set(@Nonnull OnDecryptedByteBufferChunk onDecryptedByteBufferChunk) {
        this.onDecryptedByteBufferChunk = Optional.of(onDecryptedByteBufferChunk);
    }

    public void removeOnDecryptedByteBufferChunk() {
        this.onDecryptedByteBufferChunk = Optional.empty();
    }


}
