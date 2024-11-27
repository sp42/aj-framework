package org.example.config;

import com.ajaxjs.api.security.referer.HttpReferer;
import com.ajaxjs.api.time_signature.TimeSignature;
import com.ajaxjs.api.time_signature.TimeSignatureVerify;
import com.ajaxjs.springboot.BaseWebMvcConfigure;
import com.ajaxjs.springboot.DiContextUtil;
import com.ajaxjs.springboot.GlobalControllerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class MyWebMvcConfigure extends BaseWebMvcConfigure {

    /**
     * 配置 RedisTemplate
     *
     * @param factory 链接配置
     * @return RedisTemplate
     */
    @Bean
    @Lazy
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());  // 设置键的序列化方式
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // 设置值的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer()); // 设置哈希键的序列化方式
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());        // 设置哈希值的序列化方式

        return template;
    }

    @Bean
    public TimeSignature TimeSignature() {
        TimeSignature timeSignature = new TimeSignature();
        timeSignature.setGlobalCheck(true);

        return timeSignature;
    }

    @Bean
    HttpReferer HttpReferer() {
        return new HttpReferer();
    }
}
