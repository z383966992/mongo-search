/**
 * Copyright(c) 2002-2013, 360buy.com  All Rights Reserved
 */
package service;

import java.util.List;

/**
 * @author wangx
 * @date 2013-10-15
 */
public interface CommonCache {
    
    String get(String key);

//    String setex(String key, String value, Integer timeOut);
    void setex(String key, String value, Integer timeOut);
    
//    String set(String key, String value);
    void set(String key, String value);

    void remove(String key);
    
//    Long incr(String key);
    
    Object getObject(String key);
    
    void setObject(String key, int seconds, Object value);
}
