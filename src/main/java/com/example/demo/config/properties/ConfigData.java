package com.example.demo.config.properties;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

    private Map<String, SchedulerConfig> schedulers = new HashMap<>();
    private final static DefaultSettings defaultSettings = new DefaultSettings();

    @Data
    public static class DefaultSettings {
        private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        private long minInterval = 1000;
    }

    @Data
    public static class SchedulerConfig {
        private String id;
        private long interval;
        private String timeUnit;
        private boolean enabled;

        public TimeUnit getTimeUnitAsEnum() {
            try {
                return TimeUnit.valueOf(timeUnit.toUpperCase());
            } catch (IllegalArgumentException e) {

                log.error("Invalid time unit: '{}' for scheduler {}. Valid values are: {}. Defaulting to {}",
                        timeUnit, id, Arrays.toString(TimeUnit.values()), defaultSettings.getTimeUnit());

                this.interval = defaultSettings.getMinInterval();
                this.timeUnit = defaultSettings.getTimeUnit().name().toLowerCase();

                return defaultSettings.timeUnit;
            }
        }
    }


}
