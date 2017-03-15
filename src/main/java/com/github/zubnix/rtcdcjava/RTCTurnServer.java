package com.github.zubnix.rtcdcjava;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RTCTurnServer {
    public static RTCTurnServer create(String hostname,
                                       int port) {
        return new AutoValue_RTCIceServer(hostname,
                                          port);
    }

    public abstract String getHostname();

    public abstract int getPort();
}
