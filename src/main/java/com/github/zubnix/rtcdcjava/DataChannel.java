package com.github.zubnix.rtcdcjava;


import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

public class DataChannel implements AutoCloseable {

    private final PeerConnection peerConnection;
    private final short          streamId;
    private final byte           channelType;
    private final String         label;
    private final String         protocol;

    @Nonnull
    private Optional<OnDataChannelOpen>              onDataChannelOpen              = Optional.empty();
    @Nonnull
    private Optional<OnDataChannelStringMessage>     onDataChannelStringMessage     = Optional.empty();
    @Nonnull
    private Optional<OnDataChannelByteBufferMessage> onDataChannelByteBufferMessage = Optional.empty();
    @Nonnull
    private Optional<OnDataChannelClosed>            onDataChannelClosed            = Optional.empty();
    @Nonnull
    private Optional<OnDataChannelErrorMessage>      onDataChannelErrorMessage      = Optional.empty();

    DataChannel(final PeerConnection peerConnection,
                final short streamId,
                final byte channelType,
                final String label,
                final String protocol) {

        this.peerConnection = peerConnection;
        this.streamId = streamId;
        this.channelType = channelType;
        this.label = label;
        this.protocol = protocol;
    }

    /**
     * Get the Stream ID for the DataChannel.
     *
     * @return XXX: Stream IDs *are* unique.
     */
    public short getStreamId() {
        return this.streamId;
    }

    /**
     * Get the channel type.
     *
     * @return
     */
    public byte getChannelType() {
        return this.channelType;
    }

    /**
     * Get the label for the DataChannel.
     *
     * @return XXX: Labels are *not* unique.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Get the protocol for the DataChannel.
     *
     * @return
     */
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Send calls throws an exception if it is no longer operational, ie. an error or close event has been detected.
     *
     * @param message
     *
     * @throws IOException
     */
    void send(String message) throws IOException {
        this.peerConnection.send(message,
                                 this.streamId);
    }

    /**
     * Send calls throws an exception if it is no longer operational, ie. an error or close event has been detected.
     *
     * @param message
     *
     * @throws IOException
     */
    void send(ByteBuffer message) throws IOException {
        this.peerConnection.send(message,
                                 this.streamId);
    }

    void onOpen() {
        this.onDataChannelOpen.ifPresent(OnDataChannelOpen::onDataChannelOpen);
    }

    void on(final String message) {
        this.onDataChannelStringMessage.ifPresent(callback -> callback.onDataChannel(message));
    }

    void on(final ByteBuffer message) {
        this.onDataChannelByteBufferMessage.ifPresent(callback -> callback.onDataChannel(message));
    }

    void onClose() {
        this.onDataChannelClosed.ifPresent(OnDataChannelClosed::onDataChannelClosed);
    }

    void onError(final String message) {
        this.onDataChannelErrorMessage.ifPresent(callback -> callback.onDataChannelError(message));
    }

    /**
     * Called when the remote peer 'acks' our data channel
     * This is only called when we were the peer who created the data channel.
     * Receiving this message means its 'safe' to send messages, but messages
     * can be sent before this is received (its just unknown if they'll arrive).
     *
     * @param onDataChannelOpen
     */
    public void set(@Nonnull OnDataChannelOpen onDataChannelOpen) {
        this.onDataChannelOpen = Optional.of(onDataChannelOpen);
    }

    /**
     * Called when we receive a string.
     *
     * @param onDataChannelStringMessage
     */
    public void set(@Nonnull OnDataChannelStringMessage onDataChannelStringMessage) {
        this.onDataChannelStringMessage = Optional.of(onDataChannelStringMessage);
    }

    /**
     * Called when we receive a binary blob.
     *
     * @param onDataChannelByteBufferMessage
     */
    public void set(@Nonnull OnDataChannelByteBufferMessage onDataChannelByteBufferMessage) {
        this.onDataChannelByteBufferMessage = Optional.of(onDataChannelByteBufferMessage);
    }

    /**
     * Called when the DataChannel has been cleanly closed.
     * NOT called after the Close() method has been called
     * NOT called after an error has been received.
     *
     * @param onDataChannelClosed
     */
    public void set(@Nonnull OnDataChannelClosed onDataChannelClosed) {
        this.onDataChannelClosed = Optional.of(onDataChannelClosed);
    }

    /**
     * Called when there has been an error in the underlying transport and the
     * data channel is no longer valid.
     *
     * @param onDataChannelErrorMessage
     */
    public void set(@Nonnull OnDataChannelErrorMessage onDataChannelErrorMessage) {
        this.onDataChannelErrorMessage = Optional.of(onDataChannelErrorMessage);
    }

    @Override
    public void close() {

    }
}
