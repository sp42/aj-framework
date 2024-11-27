package org.example.service;

import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.springboot.DiContextUtil;
import com.ajaxjs.util.RedisUtils;
import org.example.JpaUtil;
import org.example.controller.FooController;
import org.example.model.Foo;
import org.example.po.MyEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.ajaxjs.springboot.annotation.JsonMessage;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

@Service
public class FooService implements FooController {
    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Override
    @JsonMessage("返回 Foo")
    public Foo getFoo() {
        RedisUtils.getInstance().set("bar", "888");
        redisTemplate.opsForValue().set("foo", 1);
//        EntityManager entityManager = JpaUtil.getEntityManager();
//        entityManager.getTransaction().begin();
//
//        MyEntity entity = new MyEntity();
//        entity.setName("Example");
//        entityManager.persist(entity);
//
//        entityManager.getTransaction().commit();
//        entityManager.close();

        Foo foo = new Foo();
        foo.setName("hi");
//        throw new BusinessException("业务异常");

        return foo;
    }
}
