package com.zd.core.cache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;

/**
 * 
 * ClassName: CacheFactory 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 缓存管理工厂定义.
 * date: 2016年3月13日 上午11:47:19 
 *
 * @author luoyibo
 * @version 
 * @since JDK 1.8
 */
public class CacheFactory {

    public static CacheManager cacheManager;

    static {
        try {
            cacheManager = CacheManager.create(CacheFactory.class.getResource("/ehcache.xml"));
        } catch (CacheException e) {
            e.printStackTrace();
        }
    }

    private CacheFactory() {
    }

    public static CacheManager getCacheManager() {
        return cacheManager;
    }

}
