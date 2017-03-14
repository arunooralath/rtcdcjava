package com.github.zubnix.rtcdcjava.nativ;

import org.freedesktop.jaccall.CType;
import org.freedesktop.jaccall.Field;
import org.freedesktop.jaccall.Struct;

@Struct(value = {
        @Field(name = "sconn_family",
               type = CType.UNSIGNED_SHORT),
        @Field(name = "sconn_port",
               type = CType.UNSIGNED_SHORT),
        @Field(name = "sconn_addr",
               type = CType.POINTER,
               dataType = Void.class)
})
public final class sockaddr_conn extends sockaddr_conn_Jaccall_StructType {}
