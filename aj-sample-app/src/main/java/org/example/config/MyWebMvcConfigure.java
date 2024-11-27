package org.example.config;

import com.ajaxjs.springboot.BaseWebMvcConfigure;
import com.ajaxjs.springboot.DiContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class MyWebMvcConfigure extends BaseWebMvcConfigure {
    /**
     * 配置 RedisTemplate
     *
     * @param factory 链接配置
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());  // 设置键的序列化方式
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // 设置值的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer()); // 设置哈希键的序列化方式
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());        // 设置哈希值的序列化方式

        return template;
    }
}
