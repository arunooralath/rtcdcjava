package com.github.zubnix.rtcdcjava.nativ;

import org.freedesktop.jaccall.Lib;
import org.freedesktop.jaccall.Ptr;
import org.freedesktop.jaccall.Unsigned;

@Lib("usrsctp")
public class Usrsctp {

    public static native void usrsctp_init(@Unsigned short udp_port);

    public static native int usrsctp_finish();

    @Ptr
    public static native long usrsctp_socket(int domain,
                                             int type,
                                             int protocol,
                                             @Ptr(receive_cb.class) long cb,
                                             @Unsigned int sb_threshold);

    public static native void usrsctp_close(@Ptr long so);

    public static native long usrsctp_sendv(@Ptr long so,
                                            @Ptr(Void.class) long data,
                                            long len,
                                            @Ptr(sockaddr.class) long addrs,
                                            int addrcnt,
                                            @Ptr(Void.class) long info,
                                            int infolen,
                                            @Unsigned int infotype,
                                            int flags);

    public static native int usrsctp_setsockopt(@Ptr long so,
                                                int level,
                                                int optname,
                                                @Ptr(Void.class) long optval,
                                                @Unsigned int optlen);

    public static native int usrsctp_bind(@Ptr long so,
                                          @Ptr(sockaddr.class) long addr,
                                          @Unsigned int addrlen);

    public static native int usrsctp_connect(@Ptr long so,
                                             @Ptr(sockaddr.class) long name,
                                             @Unsigned int addrlen);
}
