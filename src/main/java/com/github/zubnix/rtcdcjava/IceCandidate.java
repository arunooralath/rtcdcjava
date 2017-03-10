package com.github.zubnix.rtcdcjava;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class IceCandidate {

    public abstract String getCandidate();

    public abstract String getSdpMid();

    public abstract int getSdpMLineIndex();
}
