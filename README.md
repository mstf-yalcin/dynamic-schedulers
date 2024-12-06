# Dynamic Scheduler with Spring Boot Embedded Config Server

## Update scheduler configuration at runtime
```
curl -X POST http://localhost:8080/actuator/refresh
```

## Configuration Example
```
  schedulers:
    scheduler1:
      id: scheduler1
      interval: 3
      time-unit: seconds
      enabled: true
    scheduler2:
      id: scheduler2
      interval: 5000
      time-unit: milliseconds
      enabled: true
```
