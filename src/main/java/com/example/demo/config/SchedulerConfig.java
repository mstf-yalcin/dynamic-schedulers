package com.example.demo.config;

import com.example.demo.config.properties.ConfigData;
import com.example.demo.service.ServiceSchedule;
import com.example.demo.service.ServiceSchedule2;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@Configuration
@EnableScheduling
public class SchedulerConfig {
    private final ConfigData configData;
    private final DynamicSchedulerRegistry schedulerRegistry;
    private final ServiceSchedule serviceSchedule1;
    private final ServiceSchedule2 serviceSchedule2;

    public SchedulerConfig(ConfigData configData,
                           DynamicSchedulerRegistry schedulerRegistry,
                           ServiceSchedule serviceSchedule1,
                           ServiceSchedule2 serviceSchedule2) {
        this.configData = configData;
        this.schedulerRegistry = schedulerRegistry;
        this.serviceSchedule1 = serviceSchedule1;
        this.serviceSchedule2 = serviceSchedule2;
    }

    @PostConstruct
    public void init() {
        startAllSchedulers();
    }

    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh() {
        log.info("Refresh event received. Updating schedulers...");
        startAllSchedulers();
    }

    private void startAllSchedulers() {
        var schedulers = configData.getSchedulers();
        //foreach
        schedulerRegistry.registerScheduler(
                schedulers.getScheduler1().getId(),
                serviceSchedule1::printData,
                schedulers.getScheduler1().getInterval(),
                schedulers.getScheduler1().isEnabled()
        );

        schedulerRegistry.registerScheduler(
                schedulers.getScheduler2().getId(),
                serviceSchedule2::printData,
                schedulers.getScheduler2().getInterval(),
                schedulers.getScheduler2().isEnabled()
        );
    }
}