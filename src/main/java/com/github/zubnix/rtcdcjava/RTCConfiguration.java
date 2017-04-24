package com.github.zubnix.rtcdcjava;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class RTCConfiguration {

    public static RTCConfiguration create(List<RTCTurnServer> rtcTurnServers,
                                          IcePortRange icePortRange,
                                          String iceUfrag,
                                          String icePwd,
                                          RTCCertificate rtcCertificate) {
        return new AutoValue_RTCConfiguration(rtcTurnServers,
                                              icePortRange,
                                              iceUfrag,
                                              icePwd,
                                              rtcCertificate);
    }

    public abstract List<RTCTurnServer> getTurnServers();

    public abstract IcePortRange getIcePortRange();

    public abstract String getIceUfrag();

    public abstract String getIcePassword();

    public abstract RTCCertificate getRtcCertificate();
}
