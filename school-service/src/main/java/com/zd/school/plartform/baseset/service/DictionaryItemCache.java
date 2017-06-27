/**
 * Project Name:school-service
 * File Name:DictionaryItemCache.java
 * Package Name:com.zd.school.plartform.baseset.service
 * Date:2016年7月20日上午9:23:53
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.plartform.baseset.service;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:DictionaryItemCache Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年7月20日 上午9:23:53
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public class DictionaryItemCache {
    /** 数据字典缓存 */
    private static Map<String, String> dicItems = new HashMap<String, String>();

    public static void push(String key, String value) {
        dicItems.put(key, value);
    }

    public static String get(String key) {
        return dicItems.get(key);
    }

    public static void clear(String key) {
        dicItems.remove(key);
    }

    public static void clearAll() {
        dicItems = new HashMap<String, String>();
    }
}
