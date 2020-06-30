package com.jerry.netty.pojo;

import java.util.Date;

/**
 * @author <a href="jianwei@outlook.com">Jerry Luo</a>
 * @since 2020/6/30 23:51
 */
public class UnixTime {

    private final long value;

    public UnixTime() {
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public UnixTime(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return new Date((value() - 2208988800L) * 1000L).toString() + " >>>";
    }
}
