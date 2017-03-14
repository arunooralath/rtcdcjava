package com.github.zubnix.rtcdcjava.nativ;

import org.freedesktop.jaccall.CType;
import org.freedesktop.jaccall.Field;
import org.freedesktop.jaccall.Struct;

@Struct(value = {
        @Field(name = "sa_family",
               type = CType.UNSIGNED_SHORT),
        @Field(name = "sa_data",
               type = CType.CHAR,
               cardinality = 14)
})
public final class sockaddr extends sockaddr_Jaccall_StructType{
}
