package com.github.zubnix.rtcdcjava.nativ;

import org.freedesktop.jaccall.CType;
import org.freedesktop.jaccall.Field;
import org.freedesktop.jaccall.Struct;

@Struct(union = true,
        value = {
                @Field(name = "sin",
                       type = CType.STRUCT,
                       dataType = sockaddr_in.class),
                @Field(name = "sin6",
                       type = CType.STRUCT,
                       dataType = sockaddr_in6.class),
                @Field(name = "scon",
                       type = CType.STRUCT,
                       dataType = sockaddr_conn.class),
                @Field(name = "sa",
                       type = CType.STRUCT,
                       dataType = sockaddr.class)
        })
public final class sctp_sockstore extends sctp_sockstore_Jaccall_StructType {}
