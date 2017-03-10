package com.github.zubnix.rtcdcjava;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class RTCConfiguration {

    public static RTCConfiguration create(List<RTCIceServer> rtcIceServers,
                                          IcePortRange icePortRange,
                                          String iceUfrag,
                                          String icePwd,
                                          List<RTCCertificate> rtcCertificates) {
        return new AutoValue_RTCConfiguration(rtcIceServers,
                                              icePortRange,
                                              iceUfrag,
                                              icePwd,
                                              rtcCertificates);
    }

    public abstract List<RTCIceServer> getIceServers();

    public abstract IcePortRange getIcePortRange();

    public abstract String iceUfrag();

    public abstract String icePwd();

    public abstract List<RTCCertificate> getRtcCertificates();
}
