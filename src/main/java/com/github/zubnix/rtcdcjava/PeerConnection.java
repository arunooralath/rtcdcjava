package com.github.zubnix.rtcdcjava;


import com.github.zubnix.rtcdcjava.nativ.Usrsctp;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PeerConnection {

    private final DTLSTransportFactory dtlsTransportFactory = new DTLSTransportFactory();

    private final Usrsctp          usrsctp;
    private final RTCConfiguration rtcConfiguration;

    @Nonnull
    private Optional<OnIceCandidate> onIceCandidate = Optional.empty();
    @Nonnull
    private Optional<OnDataChannel>  onDataChannel  = Optional.empty();

    public static PeerConnection create() {
        //create default config
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
        String         iceUfrag       = "";
        String         icePassword    = "";
        RTCCertificate rtcCertificate = RTCCertificate.generate("rtcdcjava");

        final RTCConfiguration rtcConfiguration = RTCConfiguration.create(rtcTurnServers,
                                                                          icePortRange,
                                                                          iceUfrag,
                                                                          icePassword,
                                                                          rtcCertificate);

        return create(rtcConfiguration);
    }

    public static PeerConnection create(RTCConfiguration rtcConfiguration) {

        final Usrsctp usrsctp = new Usrsctp();
        new Usrsctp_Symbols().link();


        return new PeerConnection(usrsctp,
                                  rtcConfiguration);
    }

    private PeerConnection(final Usrsctp usrsctp,
                           final RTCConfiguration rtcConfiguration) {
        this.usrsctp = usrsctp;
        this.rtcConfiguration = rtcConfiguration;
    }

    public RTCConfiguration getRtcConfiguration() {
        return rtcConfiguration;
    }

    public Optional<OnDataChannel> getOnDataChannel() {
        return onDataChannel;
    }

    public Optional<OnIceCandidate> getOnIceCandidate() {
        return onIceCandidate;
    }

    public void set(@Nonnull OnDataChannel onDataChannel) {
        this.onDataChannel = Optional.of(onDataChannel);
    }

    public void set(@Nonnull OnIceCandidate onIceCandidate) {
        this.onIceCandidate = Optional.of(onIceCandidate);
    }

    public void removeOnDataChannel() {
        this.onDataChannel = Optional.empty();
    }

    public void removeOnIceCandidate() {
        this.onIceCandidate = Optional.empty();
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
    public boolean addRemoteIceCandidate(String candidateSdp) {
        return false;
    }

    public DataChannel createDataChannel() {






        return null;
    }
}
