package com.cjlabs.web.anno;

import java.lang.annotation.*;

/**
 * 免登录注解
 * 标记在 Controller 类或方法上，表示该接口不需要登录即可访问
 * <p>
 * 使用示例:
 * 1. 标记在类上：整个 Controller 的所有方法都免登录
 *
 * @NoLogin
 * @RestController public class PublicController { ... }
 * <p>
 * 2. 标记在方法上：只有该方法免登录
 * @NoLogin
 * @GetMapping("/public/data") public Result getData() { ... }
 */
@Target({ElementType.TYPE})
//@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoLogin {

    /**
     * 描述信息（可选）
     */
    String value() default "";

    /**
     * 是否记录日志（可选，默认 true）
     */
    boolean log() default true;
}