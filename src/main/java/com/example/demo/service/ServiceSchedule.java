package com.example.demo.service;


import com.example.demo.annotation.SchedulerTask;
import com.example.demo.config.properties.ConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SchedulerTask(schedulerId = "scheduler1")
public class ServiceSchedule implements ScheduledTasks {

    private final ConfigData configData;

    public ServiceSchedule(ConfigData configData) {
        this.configData = configData;
    }

    //    @Scheduled(fixedRateString = "${app.schedulers.scheduler1.interval}")
    public void execute() {
        ConfigData.SchedulerConfig scheduler1 = configData.getSchedulers().get("scheduler1");
        log.info("ServiceSchedule: id={}, timeUnit={}, duration={}", scheduler1.getId(), scheduler1.getTimeUnit(), scheduler1.getInterval());
    }
}
