package com.github.zubnix.rtcdcjava;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RTCTurnServer {
    public static RTCTurnServer create(final String hostname,
                                       final int port) {
        return new AutoValue_RTCTurnServer(hostname,
                                           port);
    }

    public abstract String getHostname();

    public abstract int getPort();
}
