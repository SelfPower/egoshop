package com.ego.redis.dao.impl;

import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
@Repository
public class JedisDaoImpl implements JedisDao {
    @Autowired
    private JedisCluster jedisCluster;
    @Override
    public Boolean exist(String key) {
        return jedisCluster.exists(key);
    }

    @Override
    public Long del(String key) {
        return jedisCluster.del(key);
    }

    @Override
    public String set(String key, String value) {
        return jedisCluster.set(key, value);
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public Long expire(String key, int seconds) {
        //redis到expire属性，用于设置key的过期时间，过期不可用
        return jedisCluster.expire(key,seconds);
    }
}
