package com.cjlabs.memory;

import com.cjlabs.memory.redis.FmkRedisProperties;
import com.cjlabs.web.json.FmkJacksonUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@ConditionalOnClass(RedisTemplate.class)
@ComponentScan(basePackages = "com.cjlabs.memory")
@EnableConfigurationProperties({FmkRedisProperties.class})
@ConditionalOnProperty(prefix = "fmk.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
public class FmkMemoryAutoConfig {

    public FmkMemoryAutoConfig() {
        log.info("FmkMemoryAutoConfig|初始化|Fmk memory 模块自动配置加载");
    }

    /**
     * 配置 RedisTemplate
     * 使用 Jackson2JsonRedisSerializer 进行序列化
     */
    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 获取 ObjectMapper
        ObjectMapper mapper = FmkJacksonUtil.getMapper();

        // 使用 Jackson2JsonRedisSerializer 来序列化和反序列化 redis 的 value 值
        // Spring Data Redis 3.x 新的构造方式
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(mapper, Object.class);

        // 使用 StringRedisSerializer 来序列化和反序列化 redis 的 key 值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key 采用 String 的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash 的 key 也采用 String 的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value 序列化方式采用 jackson
        template.setValueSerializer(serializer);
        // hash 的 value 序列化方式采用 jackson
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();

        log.info("RedisTemplate 配置完成");
        return template;
    }

    /**
     * 配置 StringRedisTemplate
     */
    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)  // 添加这个注解
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        log.info("StringRedisTemplate 配置完成");
        return template;
    }

}
