package com.github.zubnix.rtcdcjava;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class IcePortRange {

    public static IcePortRange create(int lower, int upper){
        return new AutoValue_IcePortRange(lower, upper);
    }

    public abstract int getLower();

    public abstract int getUpper();
}
