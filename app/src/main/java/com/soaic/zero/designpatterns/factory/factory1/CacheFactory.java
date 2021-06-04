package com.soaic.zero.designpatterns.factory.factory1;


import com.soaic.zero.designpatterns.factory.DiskCache;
import com.soaic.zero.designpatterns.factory.ICache;
import com.soaic.zero.designpatterns.factory.MemoryCache;

public class CacheFactory {

    public static <T> ICache<T> createCache(int type) {
        switch(type) {
            case 1:
                return new MemoryCache<>();
            case 2:
                return new DiskCache<>();
            default:
                return null;
        }
    }

    public static <E, T extends ICache<E>> T createCache2(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
