package com.example.demo.service;


import com.example.demo.config.properties.ConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ServiceSchedule {

    private final ConfigData configData;

    public ServiceSchedule(ConfigData configData) {
        this.configData = configData;
    }

    //    @Scheduled(fixedRateString = "${app.schedulers.scheduler1.interval}")
    public void printData() {
        ConfigData.SchedulerConfig scheduler1 = configData.getSchedulers().getScheduler1();
        log.info("ServiceSchedule: id={}, duration={}", scheduler1.getId(), scheduler1.getInterval());
    }
}
