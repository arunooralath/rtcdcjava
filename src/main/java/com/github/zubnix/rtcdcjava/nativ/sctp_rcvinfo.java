package com.github.zubnix.rtcdcjava.nativ;

import org.freedesktop.jaccall.CType;
import org.freedesktop.jaccall.Field;
import org.freedesktop.jaccall.Struct;

@Struct(value = {
        @Field(name = "rcv_sid",
               type = CType.UNSIGNED_SHORT),
        @Field(name = "rcv_ssn",
               type = CType.UNSIGNED_SHORT),
        @Field(name = "rcv_flags",
               type = CType.UNSIGNED_SHORT),
        @Field(name = "rcv_ppid",
               type = CType.UNSIGNED_INT),
        @Field(name = "rcv_tsn",
               type = CType.UNSIGNED_INT),
        @Field(name = "rcv_cumtsn",
               type = CType.UNSIGNED_INT),
        @Field(name = "rcv_context",
               type = CType.UNSIGNED_INT),
        @Field(name = "rcv_assoc_id",
               type = CType.UNSIGNED_INT),
})
public final class sctp_rcvinfo extends sctp_rcvinfo_Jaccall_StructType {}
