package com.example.demo.config;

import com.example.demo.annotation.SchedulerTask;
import com.example.demo.config.properties.ConfigData;
import com.example.demo.service.ScheduledTasks;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;

@Slf4j
@Configuration
@EnableScheduling
class SchedulerConfig {
    private final ConfigData configData;
    private final DynamicSchedulerRegistry schedulerRegistry;
    private final ApplicationContext applicationContext;


    public SchedulerConfig(ConfigData configData,
                           DynamicSchedulerRegistry schedulerRegistry, ApplicationContext applicationContext) {
        this.configData = configData;
        this.schedulerRegistry = schedulerRegistry;
        this.applicationContext = applicationContext;
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
        Map<String, ScheduledTasks> scheduledTasks = applicationContext.getBeansOfType(ScheduledTasks.class);
        Map<String, ConfigData.SchedulerConfig> schedulers = configData.getSchedulers();

        scheduledTasks.forEach((beanName, task) -> {

            SchedulerTask annotation = task.getClass().getAnnotation(SchedulerTask.class);

            if (annotation == null) {
                log.warn("Bean {} implements ScheduledTasks but missing @SchedulerTask annotation", beanName);
                return;
            }

            String schedulerId = annotation.schedulerId();
            ConfigData.SchedulerConfig schedulerConfig = schedulers.get(schedulerId);

            if (schedulerConfig == null) {
                log.error("No configuration found for scheduler ID: {}", schedulerConfig.getId());
                return;
            }

            schedulerRegistry.registerScheduler(
                    schedulerConfig.getId(),
                    task::execute,
                    schedulerConfig.getTimeUnitAsEnum(),
                    schedulerConfig.getInterval(),
                    schedulerConfig.isEnabled()
            );

        });
    }
}