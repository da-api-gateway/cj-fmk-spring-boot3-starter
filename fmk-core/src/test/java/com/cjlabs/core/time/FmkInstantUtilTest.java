package com.cjlabs.core.time;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZoneId;

import static com.cjlabs.core.time.FmkTimeConstant.SINGAPORE_ZONE;
import static com.cjlabs.core.time.FmkTimeConstant.yy_MM_dd_HH_mm_ss_Formatter;

@Slf4j
public class FmkInstantUtilTest {


    public static void main(String[] args) {
        Instant instant1 = FmkInstantUtil.fromMillis(1764201600000L);
        String instant1Str = FmkInstantUtil.formatDateTime(instant1);
        log.info("FmkInstantUtilTest|main|instant1Str={}", instant1Str);

        Instant instant2 = FmkInstantUtil.fromMillis(1764287999000L);
        String instant2Str = FmkInstantUtil.formatDateTime(instant2);
        log.info("FmkInstantUtilTest|main|instant2Str={}", instant2Str);

        Instant dbInstant1 = FmkInstantUtil.fromMillis(1764181951000L);
        String dbInstant1Str = FmkInstantUtil.format(dbInstant1, yy_MM_dd_HH_mm_ss_Formatter, SINGAPORE_ZONE);
        log.info("FmkInstantUtilTest|main|dbInstant1Str={}", dbInstant1Str);
    }
}
