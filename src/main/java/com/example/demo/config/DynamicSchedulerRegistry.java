package com.example.demo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Component
public class DynamicSchedulerRegistry {
    private final ThreadPoolTaskScheduler taskScheduler;
    private final Map<String, SchedulerInfo> schedulers = new ConcurrentHashMap<>();

    public DynamicSchedulerRegistry() {
        this.taskScheduler = new ThreadPoolTaskScheduler();
        this.taskScheduler.setPoolSize(10);
        this.taskScheduler.initialize();
    }

    public void registerScheduler(String schedulerId, Runnable task, long interval, boolean enabled) {
        SchedulerInfo existingScheduler = schedulers.get(schedulerId);

        if (existingScheduler != null) {
            boolean intervalChanged = existingScheduler.getInterval() != interval;
            boolean enabledStateChanged = existingScheduler.isEnabled() != enabled;

            if (!intervalChanged && !enabledStateChanged) {
                log.debug("Scheduler {} settings unchanged, skipping restart", schedulerId);
                return;
            }

            if (intervalChanged) {
                log.debug("Scheduler {} interval changed from {} to {}",
                        schedulerId, existingScheduler.getInterval(), interval);
            }
            if (enabledStateChanged) {
                log.debug("Scheduler {} enabled state changed from {} to {}",
                        schedulerId, existingScheduler.isEnabled(), enabled);
            }

            if ((intervalChanged && existingScheduler.isEnabled() && enabled) ||
                (enabledStateChanged && existingScheduler.isEnabled() && !enabled)) {
                stopScheduler(schedulerId);
            }
        }

        if (enabled) {
            log.info("Starting scheduler {} with interval: {} ms", schedulerId, interval);
            ScheduledFuture<?> future = taskScheduler.scheduleAtFixedRate(task, Duration.ofMillis(interval));
            schedulers.put(schedulerId, new SchedulerInfo(task, interval, future, true));
        } else {
            log.info("Registering scheduler {} in disabled state", schedulerId);
            schedulers.put(schedulerId, new SchedulerInfo(task, interval, null, false));
        }
    }

    private void stopScheduler(String schedulerId) {
        SchedulerInfo scheduler = schedulers.get(schedulerId);
        if (scheduler != null && scheduler.getFuture() != null) {
            log.info("Stopping scheduler {}", schedulerId);
            scheduler.getFuture().cancel(false);
            scheduler.setFuture(null);
        }
    }

    public boolean isEnabled(String schedulerId) {
        SchedulerInfo scheduler = schedulers.get(schedulerId);
        return scheduler != null && scheduler.isEnabled();
    }

    public void enableScheduler(String schedulerId) {
        SchedulerInfo scheduler = schedulers.get(schedulerId);
        if (scheduler != null && !scheduler.isEnabled()) {
            log.info("Enabling scheduler {}", schedulerId);
            scheduler.setEnabled(true);
            scheduler.setFuture(taskScheduler.scheduleAtFixedRate(scheduler.getTask(), Duration.ofMillis(scheduler.getInterval())));
        }
    }

    public void disableScheduler(String schedulerId) {
        SchedulerInfo scheduler = schedulers.get(schedulerId);
        if (scheduler != null && scheduler.isEnabled()) {
            log.info("Disabling scheduler {}", schedulerId);
            stopScheduler(schedulerId);
            scheduler.setEnabled(false);
        }
    }

    public void stopAll() {
        schedulers.keySet().forEach(this::stopScheduler);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class SchedulerInfo {
        private Runnable task;
        private long interval;
        private ScheduledFuture<?> future;
        private boolean enabled;
    }
}