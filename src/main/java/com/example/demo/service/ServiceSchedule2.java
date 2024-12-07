package com.example.demo.service;

import com.example.demo.annotation.SchedulerTask;
import com.example.demo.config.properties.ConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SchedulerTask(schedulerId = "scheduler2")
public class ServiceSchedule2 implements ScheduledTasks {
    private final ConfigData configData;

    public ServiceSchedule2(ConfigData configData) {
        this.configData = configData;
    }

    public void execute() {
        ConfigData.SchedulerConfig scheduler2 = configData.getSchedulers().get("scheduler2");
        log.info("ServiceSchedule2: id={}, timeUnit={}, duration={}", scheduler2.getId(), scheduler2.getTimeUnit(), scheduler2.getInterval());
    }
}
