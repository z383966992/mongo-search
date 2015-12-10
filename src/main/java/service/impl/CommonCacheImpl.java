/**
 * Copyright(c) 2002-2013, 360buy.com  All Rights Reserved
 */
package service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.Resources;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import service.CommonCache;
import utils.Constants;
import utils.SerializableUtil;
import cache.ICacheManager;

@Component
public class CommonCacheImpl implements CommonCache {

	private static final Logger logger = Logger.getLogger(CommonCacheImpl.class);

	@Resource
	private ICacheManager cacheManager;

//    public static final int TIMEOUT = Constants.SESSION_TIMEOUT;
    //域名唯一标识_模块唯一标识_key唯一标识
	public final static String KEY = "%s_%s_%s";

    @Override
    public String get(String key) {
		try {
			String value = cacheManager.get(key);
			logger.debug("get--->key:" + key + "; values:" + value);
			return value;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

    @Override
    public void setex(String key, String value, Integer timeOut) {
		logger.debug("set--->key:" + key + "; value=" + value);
		try {
			cacheManager.set(key, value);
			cacheManager.expire(key, timeOut);
//			return redis.setex(key, timeOut, value);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}
    
    @Override
    public void set(String key, String value) {
        logger.debug("set--->key:" + key + "; value=" + value);
        try {
        	cacheManager.set(key, value);
//            return redis.set(key, value);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

	@Override
    public void remove(String key) {
		logger.debug("remove--->key:" + key);
		try {
			cacheManager.del(key);
//			redis.del(key);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

//    @Override
//    public Long incr(String key) {
//        logger.debug("incr--->key:" + key);
//        try{
//            return redis.incr(key);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage(), e.getCause());
//        }
//    }

    @Override
    public Object getObject(String key) {
        logger.debug("getObject--->key:" + key);
        try{
        	
        	return SerializableUtil.deSerialize(cacheManager.get(SerializableUtil.serialize(key)));
//            return redis.getObject(key);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void setObject(String key, int seconds, Object value) {
        logger.debug("setObject--->key:" + key);
        try{
        	cacheManager.set(SerializableUtil.serialize(key), SerializableUtil.serialize(value));
//            return redis.setObject(key, seconds, value);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }
}
