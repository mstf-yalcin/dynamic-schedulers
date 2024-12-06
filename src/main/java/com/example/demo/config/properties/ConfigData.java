package com.example.demo.config.properties;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
        private boolean enabled;
    }
}
