package com.cjlabs.core.time;

import com.cjlabs.core.collection.FmkPairUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@DisplayName("FmkPairUtil Tests")
class FmkInstantUtilTest_V2 {

    @Nested
    @DisplayName("FmkInstantUtil ")
    class PairCreationTests {
        @Test
        @DisplayName("currentSeconds")
        void currentSeconds() {
            long currentSeconds = FmkInstantUtil.currentSeconds();

            Instant instant = FmkInstantUtil.fromSeconds(currentSeconds);

            DateTimeFormatter yyMmDdHhMmSsFormatter = FmkTimeConstant.yy_MM_dd_HH_mm_ss_Formatter;

            ZoneId utcZone = FmkTimeConstant.UTC_ZONE;
            String utcZoneStr = FmkInstantUtil.format(instant, yyMmDdHhMmSsFormatter, utcZone);
            log.info("PairCreationTests|ofShouldCreateImmutablePair|utcZoneStr={}", utcZoneStr);

            ZoneId utcPlus7Zone = FmkTimeConstant.UTC_PLUS_7_ZONE;
            String utcPlus7ZoneStr = FmkInstantUtil.format(instant, yyMmDdHhMmSsFormatter, utcPlus7Zone);
            log.info("PairCreationTests|ofShouldCreateImmutablePair|utcPlus7ZoneStr={}", utcPlus7ZoneStr);

            ZoneId utcPlus8Zone = FmkTimeConstant.UTC_PLUS_8_ZONE;
            String utcPlus8ZoneStr = FmkInstantUtil.format(instant, yyMmDdHhMmSsFormatter, utcPlus8Zone);
            log.info("PairCreationTests|ofShouldCreateImmutablePair|utcPlus8ZoneStr={}", utcPlus8ZoneStr);


        }

        @Test
        @DisplayName("currentMillis")
        void currentMillis() {
            long currentMillis = FmkInstantUtil.currentMillis();

            Instant instant = FmkInstantUtil.fromMillis(currentMillis);

            DateTimeFormatter yyMmDdHhMmSsFormatter = FmkTimeConstant.yy_MM_dd_HH_mm_ss_Formatter;

            ZoneId utcZone = FmkTimeConstant.UTC_ZONE;
            String utcZoneStr = FmkInstantUtil.format(instant, yyMmDdHhMmSsFormatter, utcZone);
            log.info("PairCreationTests|ofShouldCreateImmutablePair|utcZoneStr={}", utcZoneStr);

            ZoneId utcPlus7Zone = FmkTimeConstant.UTC_PLUS_7_ZONE;
            String utcPlus7ZoneStr = FmkInstantUtil.format(instant, yyMmDdHhMmSsFormatter, utcPlus7Zone);
            log.info("PairCreationTests|ofShouldCreateImmutablePair|utcPlus7ZoneStr={}", utcPlus7ZoneStr);

            ZoneId utcPlus8Zone = FmkTimeConstant.UTC_PLUS_8_ZONE;
            String utcPlus8ZoneStr = FmkInstantUtil.format(instant, yyMmDdHhMmSsFormatter, utcPlus8Zone);
            log.info("PairCreationTests|ofShouldCreateImmutablePair|utcPlus8ZoneStr={}", utcPlus8ZoneStr);


        }

        @Test
        @DisplayName("currentNanos")
        void currentNanos() {
            long currentNanos = FmkInstantUtil.currentNanos();

            log.info("PairCreationTests|ofShouldCreateImmutablePair|currentNanos={}", currentNanos);

            long currentSeconds = FmkInstantUtil.currentSeconds();

            Instant instant = FmkInstantUtil.fromSecondsAndNanos(currentSeconds, currentNanos);

            DateTimeFormatter yyMmDdHhMmSsFormatter = FmkTimeConstant.yy_MM_dd_HH_mm_ss_Formatter;

            ZoneId utcZone = FmkTimeConstant.UTC_ZONE;
            String utcZoneStr = FmkInstantUtil.format(instant, yyMmDdHhMmSsFormatter, utcZone);
            log.info("PairCreationTests|ofShouldCreateImmutablePair|utcZoneStr={}", utcZoneStr);

            ZoneId utcPlus7Zone = FmkTimeConstant.UTC_PLUS_7_ZONE;
            String utcPlus7ZoneStr = FmkInstantUtil.format(instant, yyMmDdHhMmSsFormatter, utcPlus7Zone);
            log.info("PairCreationTests|ofShouldCreateImmutablePair|utcPlus7ZoneStr={}", utcPlus7ZoneStr);

            ZoneId utcPlus8Zone = FmkTimeConstant.UTC_PLUS_8_ZONE;
            String utcPlus8ZoneStr = FmkInstantUtil.format(instant, yyMmDdHhMmSsFormatter, utcPlus8Zone);
            log.info("PairCreationTests|ofShouldCreateImmutablePair|utcPlus8ZoneStr={}", utcPlus8ZoneStr);

        }

        @Test
        @DisplayName("currentFullNanos")
        void currentFullNanos() {
            long currentFullNanos = FmkInstantUtil.currentFullNanos();

            Instant instant = FmkInstantUtil.fromFullNanos(currentFullNanos);

            DateTimeFormatter yyMmDdHhMmSsFormatter = FmkTimeConstant.yy_MM_dd_HH_mm_ss_Formatter;

            ZoneId utcZone = FmkTimeConstant.UTC_ZONE;
            String utcZoneStr = FmkInstantUtil.format(instant, yyMmDdHhMmSsFormatter, utcZone);
            log.info("PairCreationTests|ofShouldCreateImmutablePair|utcZoneStr={}", utcZoneStr);

            ZoneId utcPlus7Zone = FmkTimeConstant.UTC_PLUS_7_ZONE;
            String utcPlus7ZoneStr = FmkInstantUtil.format(instant, yyMmDdHhMmSsFormatter, utcPlus7Zone);
            log.info("PairCreationTests|ofShouldCreateImmutablePair|utcPlus7ZoneStr={}", utcPlus7ZoneStr);

            ZoneId utcPlus8Zone = FmkTimeConstant.UTC_PLUS_8_ZONE;
            String utcPlus8ZoneStr = FmkInstantUtil.format(instant, yyMmDdHhMmSsFormatter, utcPlus8Zone);
            log.info("PairCreationTests|ofShouldCreateImmutablePair|utcPlus8ZoneStr={}", utcPlus8ZoneStr);


        }

    }
}