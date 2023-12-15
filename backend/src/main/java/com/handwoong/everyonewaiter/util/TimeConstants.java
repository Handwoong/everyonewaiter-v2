package com.handwoong.everyonewaiter.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeConstants {
    private static final long SECOND_TO_MILLISECOND = 1000L;
    private static final long MINUTE_TO_SECOND = 60L;

    public static final long TEN_MINUTE = SECOND_TO_MILLISECOND * MINUTE_TO_SECOND * 10L;
}
