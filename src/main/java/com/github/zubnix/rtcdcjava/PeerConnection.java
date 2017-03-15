package com.github.zubnix.rtcdcjava;


import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PeerConnection {


    private final RTCConfiguration rtcConfiguration;
    private final OnIceCandidate   onIceCandidate;
    private final OnDataChannel    onDataChannel;

    public static PeerConnection create(OnIceCandidate onIceCandidate,
                                        OnDataChannel onDataChannel) {
        //createClientTransport default config
        List<RTCTurnServer> rtcTurnServers = Arrays.asList(RTCTurnServer.create("stun.l.google.com",
                                                                                19302),
                                                           RTCTurnServer.create("stun1.l.google.com",
                                                                                19302),
                                                           RTCTurnServer.create("stun2.l.google.com",
                                                                                19302),
                                                           RTCTurnServer.create("stun3.l.google.com",
                                                                                19302),
                                                           RTCTurnServer.create("stun4.l.google.com",
                                                                                19302));
        IcePortRange icePortRange = IcePortRange.create(1025,
                                                        65535);
        String               iceUfrag        = "";
        String               icePassword     = "";
        List<RTCCertificate> rtcCertificates = Collections.singletonList(RTCCertificate.generate("rtcdcjava"));

        final RTCConfiguration rtcConfiguration = RTCConfiguration.create(rtcTurnServers,
                                                                          icePortRange,
                                                                          iceUfrag,
                                                                          icePassword,
                                                                          rtcCertificates);

        return create(rtcConfiguration,
                      onIceCandidate,
                      onDataChannel);
    }

    public static PeerConnection create(RTCConfiguration rtcConfiguration,
                                        OnIceCandidate onIceCandidate,
                                        OnDataChannel onDataChannel) {

        return new PeerConnection(rtcConfiguration,
                                  onIceCandidate,
                                  onDataChannel);
    }

    private PeerConnection(final RTCConfiguration rtcConfiguration,
                           final OnIceCandidate onIceCandidate,
                           final OnDataChannel onDataChannel) {

        this.rtcConfiguration = rtcConfiguration;
        this.onIceCandidate = onIceCandidate;
        this.onDataChannel = onDataChannel;
    }

    public RTCConfiguration getRtcConfiguration() {
        return rtcConfiguration;
    }

    public OnDataChannel getOnDataChannel() {
        return onDataChannel;
    }

    public OnIceCandidate getOnIceCandidate() {
        return onIceCandidate;
    }

    /**
     * Parse Offer SDP
     */
    public void parseOffer(String sdp) {

    }

    /**
     * generate answer SDP
     *
     * @return SDP string
     */
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
