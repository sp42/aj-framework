package com.ajaxjs.util;

import com.ajaxjs.springboot.DiContextUtil;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 * <p>
 * <a href="https://blog.csdn.net/weixin_40461281/article/details/103416876">...</a>
 * <a href="https://www.cnblogs.com/huanruke/p/16078529.html">...</a>
 * <a href="https://redisson.org/docs/integration-with-spring">...</a>
 */
@Data
public class RedisUtils {
    private RedisTemplate<String, Object> redisTemplate;

    private RedisUtils() {
    }

    private RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static class SingletonHolder {
        private static final RedisUtils INSTANCE = new RedisUtils();
    }

    /**
     * Redis 工具类实例
     *
     * @return Redis 工具类实例
     */
    public static RedisUtils getInstance() {
        if (SingletonHolder.INSTANCE.getRedisTemplate() == null)
            SingletonHolder.INSTANCE.setRedisTemplate((RedisTemplate) DiContextUtil.getBean("redisTemplate"));

        return SingletonHolder.INSTANCE;
    }

    /**
     * 是否存在 key
     *
     * @param key 键
     * @return 是否存在 key
     */
    public boolean hasKey(String key) {
        return b(redisTemplate.hasKey(key));
    }

    /**
     * 删除缓存
     *
     * @param keys 键集合
     */
    public void del(String... keys) {
        redisTemplate.delete(Arrays.asList(keys));
    }

    /**
     * 获取缓存 TTL
     *
     * @param key 键
     * @return java.lang.Long 时长（s）
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存 TTL
     *
     * @param key  键
     * @param time 时长（s） 单位：秒
     */
    public boolean expire(String key, long time) {
        return expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存 TTL
     *
     * @param key      键
     * @param time     时长（s） 单位：秒
     * @param timeUnit 单位
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        return b(redisTemplate.expire(key, time, timeUnit));
    }

    /**
     * 移除给定 key 的过期时间，使得 key 永不过期。
     *
     * @param key 键
     * @return 是否成功
     */

    public boolean persist(String key) {
        return b(redisTemplate.boundValueOps(key).persist());
    }

    /**
     * 检查给定的元素是否在变量中
     *
     * @param key 键
     * @param obj 元素对象
     * @return 给定的元素是否在变量中
     */

    public boolean isMember(String key, Object obj) {
        return b(redisTemplate.opsForSet().isMember(key, obj));
    }

    /**
     * 转移变量的元素值到目的变量
     *
     * @param key     键
     * @param value   元素对象
     * @param destKey 元素对象
     * @return 是否成功
     */
    public boolean move(String key, String value, String destKey) {
        return b(redisTemplate.opsForSet().move(key, value, destKey));
    }

    /* --------------------------- 添加缓存  -------------------------------------*/

    /**
     * 添加缓存 - string
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 添加缓存
     *
     * @param key   键
     * @param value 值
     * @param time  ttl (s)
     */
    public void set(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return java.lang.Object
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取缓存 - string
     *
     * @param key 键
     * @return str
     */
    public String getStr(String key) {
        return get(key).toString();
    }

    /* --------------------------- 存储 Hash 操作  -------------------------------------*/

    /**
     * 确定哈希 hashKey 是否存在
     *
     * @param key  键
     * @param hKey hash键
     * @return true=存在；false=不存在
     */
    public boolean hasHashKey(String key, String hKey) {
        return b(redisTemplate.opsForHash().hasKey(key, hKey));
    }

    /**
     * 往 Hash 中存入数据
     *
     * @param key   Redis 键
     * @param hKey  Hash 键
     * @param value 值
     */
    public void hashPut(String key, String hKey, Object value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 往 Hash 中存入多个数据
     *
     * @param key    Redis 键
     * @param values Hash 键值对
     */
    public void hashPutAll(String key, Map<String, Object> values) {
        redisTemplate.opsForHash().putAll(key, values);
    }

    /**
     * 获取 Hash 中的数据
     *
     * @param key  Redis 键
     * @param hKey Hash 键
     * @return Hash 中的对象
     */
    public Object hashGet(String key, String hKey) {
        return redisTemplate.opsForHash().get(key, hKey);
    }

    /**
     * 获取 Hash 中的数据
     *
     * @param key Redis 键
     * @return Hash 对象
     */
    public Map<Object, Object> hashGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取多个 Hash 中的数据
     *
     * @param key   Redis 键
     * @param hKeys Hash 键集合
     * @return Hash 对象集合
     */
    public List<Object> hashMultiGet(String key, Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash 键集合
     * @return Hash 对象集合
     */
    public long hashDeleteKeys(String key, Collection<Object> hKeys) {
        return redisTemplate.opsForHash().delete(key, hKeys);
    }

    private static boolean b(Boolean B) {
        return B != null && B;
    }
}
