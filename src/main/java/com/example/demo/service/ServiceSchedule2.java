package com.example.demo.service;

import com.example.demo.config.properties.ConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ServiceSchedule2 {
    private final ConfigData configData;

    public ServiceSchedule2(ConfigData configData) {
        this.configData = configData;
    }

    public void printData() {
        ConfigData.SchedulerConfig scheduler2 = configData.getSchedulers().getScheduler2();
        log.info("ServiceSchedule2: id={}, duration={}", scheduler2.getId(), scheduler2.getInterval());
    }
}
