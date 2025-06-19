package com.yabobaozb.ecom.common.infra.util;

import java.util.Calendar;

/**
 * ID生成器
 */
public final class IDGenerator {
    private static volatile long lastTimestamp = -1L;
    private static final long TIMESTAMP_SHIFT_BITS = 22L;
    private static final long DATACENTER_SHIFT_BITS = 17L;
    private static final long WORKERID_SHIFT_BITS = 12L;
    private static final long SEQUENCE_BITS = 12L;

    // 2025-01-01 00:00:00
    private static final long INIT_EPOCH = 1735660800898L;
    private static final long datacenterId = 1L;
    private static final long workerId = 1L;


    private static volatile long sequence = 0L;
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    public static synchronized long generateId() {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("时钟回拨");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;
        return (timestamp - INIT_EPOCH) << TIMESTAMP_SHIFT_BITS
                | datacenterId << DATACENTER_SHIFT_BITS
                | workerId << WORKERID_SHIFT_BITS
                | sequence;
    }

    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public static void main(String[] args) {
        System.out.println( System.currentTimeMillis() );
        // 1759305600000L
        // 1750237884023
        // 1750237914027

        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, 0, 1, 0, 0, 0);
        System.out.println( calendar.getTimeInMillis() );
    }

}
