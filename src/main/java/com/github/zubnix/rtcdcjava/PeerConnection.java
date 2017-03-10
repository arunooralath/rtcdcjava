package com.github.zubnix.rtcdcjava;


import java.nio.ByteBuffer;

public class PeerConnection {

    public PeerConnection(RTCConfiguration rtcConfiguration,
                          OnIceCandidate onIceCandidate,
                          OnDataChannel onDataChannel) {

    }

    /**
     * Parse Offer SDP
     */
    public void parseOffer(String sdp) {

    }

    public String GenerateAnswer() {
        return "";
    }

    /**
     * Handle remote ICE Candidate.
     * Supports trickle ice candidates.
     */
    public boolean setRemoteIceCandidate(String candidateSdp) {
        return false;
    }

    void send(final String message,
              final short streamId) {

    }

    void send(final ByteBuffer message,
              final short streamId) {

    }
}
