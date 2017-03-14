package com.github.zubnix.rtcdcjava.nativ;

import org.freedesktop.jaccall.CType;
import org.freedesktop.jaccall.Field;
import org.freedesktop.jaccall.Struct;

@Struct(value = {
        @Field(name = "sin_family",
               type = CType.UNSIGNED_INT),
        @Field(name = "sin_port",
               type = CType.UNSIGNED_SHORT),
        @Field(name = "sin_addr",
               type = CType.UNSIGNED_INT)
})
public final class sockaddr_in extends sockaddr_in_Jaccall_StructType {}
