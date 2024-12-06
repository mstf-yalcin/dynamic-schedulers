package com.example.demo.config.properties;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties("app")
public class ConfigData {
    private String property1;
    private String property2;
    private Schedulers schedulers;

    @Data
    public static class Properties {
        private String property1;
        private String property2;
    }

    @Data
    public static class Schedulers {
        private SchedulerConfig scheduler1;
        private SchedulerConfig scheduler2;
    }

    @Data
    public static class SchedulerConfig {
        private String id;
        private long interval;
        private String timeUnit;
        private boolean enabled;
        private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;
        private static final long DEFAULT_MIN_INTERVAL_MS = 3000;

        public TimeUnit getTimeUnitAsEnum() {
            try {
                return TimeUnit.valueOf(timeUnit.toUpperCase());
            } catch (IllegalArgumentException e) {
                this.interval = DEFAULT_MIN_INTERVAL_MS;
                this.timeUnit = DEFAULT_TIME_UNIT.name().toLowerCase();

                log.error("Invalid time unit: '{}' for scheduler {}. Valid values are: {}. Defaulting to {}",
                        timeUnit, id, Arrays.toString(TimeUnit.values()), timeUnit);

                return DEFAULT_TIME_UNIT;
            }
        }
    }


}
