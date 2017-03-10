package com.github.zubnix.rtcdcjava;


import java.io.IOException;
import java.nio.ByteBuffer;

public class DataChannel implements AutoCloseable {

    /**
     * Get the Stream ID for the DataChannel.
     *
     * @return XXX: Stream IDs *are* unique.
     */
    public short getStreamId() {
        return 0;
    }

    /**
     * Get the channel type.
     *
     * @return
     */
    public byte getChannelType() {
        return 0;
    }

    /**
     * Get the label for the DataChannel.
     *
     * @return XXX: Labels are *not* unique.
     */
    public String getLabel() {
        return "";
    }

    /**
     * Get the protocol for the DataChannel.
     *
     * @return
     */
    public String getProtocol() {
        return "";
    }

    /**
     * Send calls throws an exception if it is no longer operational, ie. an error or close event has been detected.
     *
     * @param message
     *
     * @throws IOException
     */
    void send(String message) throws IOException {

    }

    /**
     * Send calls throws an exception if it is no longer operational, ie. an error or close event has been detected.
     *
     * @param message
     *
     * @throws IOException
     */
    void send(ByteBuffer message) throws IOException {

    }

    /**
     * Called when the remote peer 'acks' our data channel
     * This is only called when we were the peer who created the data channel.
     * Receiving this message means its 'safe' to send messages, but messages
     * can be sent before this is received (its just unknown if they'll arrive).
     *
     * @param onDataChannelOpen
     */
    public void set(OnDataChannelOpen onDataChannelOpen) {

    }

    /**
     * Called when we receive a string.
     *
     * @param onDataChannelStringMessage
     */
    public void set(OnDataChannelStringMessage onDataChannelStringMessage) {

    }

    /**
     * Called when we receive a binary blob.
     *
     * @param onDataChannelByteBufferMessage
     */
    public void set(OnDataChannelByteBufferMessage onDataChannelByteBufferMessage) {

    }

    /**
     * Called when the DataChannel has been cleanly closed.
     * NOT called after the Close() method has been called
     * NOT called after an error has been received.
     *
     * @param onDataChannelClosed
     */
    public void set(OnDataChannelClosed onDataChannelClosed) {

    }

    /**
     * Called when there has been an error in the underlying transport and the
     * data channel is no longer valid.
     *
     * @param onDataChannelErrorMessage
     */
    public void set(OnDataChannelErrorMessage onDataChannelErrorMessage) {

    }

    @Override
    public void close() {

    }
}
