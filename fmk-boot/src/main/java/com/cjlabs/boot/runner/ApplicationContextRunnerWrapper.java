package com.cjlabs.boot.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class ApplicationContextRunnerWrapper {

    /**
     * 启动 Spring Boot 应用，捕获异常并处理
     *
     * @param applicationClass 启动类
     * @param args             启动参数
     * @return Spring 上下文
     */
    public static ConfigurableApplicationContext run(Class<?> applicationClass, String[] args) {
        try {
            log.info("ApplicationRunnerWrapper|run|name={}", applicationClass.getSimpleName());

            SpringApplication app = new SpringApplication(applicationClass);
            app.setBannerMode(Banner.Mode.OFF);
            return app.run(args);
        } catch (Exception e) {
            log.info("ApplicationRunnerWrapper|run|failed={}", e.getMessage(), e);
            // TODO: 可以在这里做补救操作，如通知、释放资源、记录状态等
            throw e;
            // 可选择继续抛出异常或终止应用
        }
    }
}
