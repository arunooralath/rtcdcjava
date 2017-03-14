package com.github.zubnix.rtcdcjava.nativ;

import org.freedesktop.jaccall.ByVal;
import org.freedesktop.jaccall.Functor;
import org.freedesktop.jaccall.Ptr;

@FunctionalInterface
@Functor
public interface receive_cb {
    int $(@Ptr long sock,
          @ByVal(sctp_sockstore.class) long addr,
          @Ptr(Void.class) long data,
          long datalen,
          @ByVal(sctp_rcvinfo.class) long sctp_rcvinfo,
          int flags);
}
