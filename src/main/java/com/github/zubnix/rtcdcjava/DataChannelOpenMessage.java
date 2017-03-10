package com.github.zubnix.rtcdcjava;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DataChannelOpenMessage {

    public static DataChannelOpenMessage create(byte msgType,
                                                byte chanType,
                                                short priority,
                                                int reliability,
                                                short labelLen,
                                                short protocolLan,
                                                String label,
                                                String protocol) {
        return new AutoValue_DataChannelOpenMessage(msgType,
                                                    chanType,
                                                    priority,
                                                    reliability,
                                                    labelLen,
                                                    protocolLan,
                                                    label,
                                                    protocol);
    }

    public abstract byte getMsgType();

    public abstract byte getchanType();

    public abstract short getPriority();

    public abstract int getReliability();

    public abstract short getLabelLen();

    public abstract short getProtocolLen();

    public abstract String getLabel();

    public abstract String getProtocol();
}
