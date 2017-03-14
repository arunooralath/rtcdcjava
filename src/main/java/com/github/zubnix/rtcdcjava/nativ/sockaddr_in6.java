package com.github.zubnix.rtcdcjava.nativ;

import org.freedesktop.jaccall.CType;
import org.freedesktop.jaccall.Field;
import org.freedesktop.jaccall.Struct;

@Struct(value = {
        @Field(name = "sin6_family",
               type = CType.UNSIGNED_INT),
        @Field(name = "sin6_port",
               type = CType.UNSIGNED_SHORT),
        @Field(name = "sin6_flowinfo",
               type = CType.UNSIGNED_INT),
        @Field(name = "sin6_addr",
               type = CType.UNSIGNED_CHAR,
               cardinality = 16),
        @Field(name = "sin6_scope_id",
               type = CType.UNSIGNED_INT),
})
public final class sockaddr_in6 extends sockaddr_in6_Jaccall_StructType {}
