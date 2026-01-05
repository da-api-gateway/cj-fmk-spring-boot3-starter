
```

# Spring Redis 配置
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: 
      database: 0
      timeout: 3000ms
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
          max-wait: -1ms

# FMK Redis 配置
fmk:
  redis:
    enabled: true
    default-expire-seconds: 3600
    lock-timeout-seconds: 30
    lock-wait-seconds: 3
    key-prefix: "fmk:"

```