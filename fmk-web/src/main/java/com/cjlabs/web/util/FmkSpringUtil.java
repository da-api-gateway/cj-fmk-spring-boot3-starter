package com.cjlabs.web.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Utility class to access Spring-managed beans from the application context.
 *
 * 通过监听 ContextRefreshedEvent 来初始化 ApplicationContext，
 * 确保在所有 Bean 初始化完成后才可用。
 */
@Slf4j
public class FmkSpringUtil implements ApplicationListener<ContextRefreshedEvent> {

    private static final AtomicReference<ApplicationContext> APPLICATION_CONTEXT = new AtomicReference<>();

    /**
     * Get the Spring application context.
     *
     * @return the ApplicationContext
     * @throws IllegalStateException if the context is not initialized
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return APPLICATION_CONTEXT.get();
    }

    /**
     * Get a bean by its name.
     *
     * @param name the name of the bean
     * @return the bean instance
     * @throws IllegalStateException if the context is not initialized
     */
    public static Object getBean(String name) {
        checkApplicationContext();
        return APPLICATION_CONTEXT.get().getBean(name);
    }

    /**
     * Get a bean by its type.
     *
     * @param <T>   the type of the bean
     * @param clazz the class of the bean
     * @return the bean instance
     * @throws IllegalStateException if the context is not initialized
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return APPLICATION_CONTEXT.get().getBean(clazz);
    }

    /**
     * Get a bean by its name and type.
     *
     * @param <T>   the type of the bean
     * @param name  the name of the bean
     * @param clazz the class of the bean
     * @return the bean instance
     * @throws IllegalStateException if the context is not initialized
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        checkApplicationContext();
        return APPLICATION_CONTEXT.get().getBean(name, clazz);
    }

    /**
     * Get a bean by its type.
     *
     * @param <T>   the type of the bean
     * @param clazz the class of the bean
     * @return the bean instance
     * @throws IllegalStateException if the context is not initialized
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        checkApplicationContext();
        return APPLICATION_CONTEXT.get().getBeansOfType(clazz);
    }

    /**
     * Check if the application context is initialized.
     *
     * @throws IllegalStateException if the context is not initialized
     */
    private static void checkApplicationContext() {
        if (APPLICATION_CONTEXT.get() == null) {
            throw new IllegalStateException("ApplicationContext is not initialized. Ensure SpringUtil is registered as a Spring bean.");
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 只处理根容器的事件（避免父子容器重复触发）
        if (event.getApplicationContext().getParent() == null) {
            if (APPLICATION_CONTEXT.compareAndSet(null, event.getApplicationContext())) {
                log.info("FmkSpringUtil|onApplicationEvent|ApplicationContext initialized successfully");
            }
        }
    }
}