package com.soaic.zero.designpatterns.factory;

import com.soaic.zero.designpatterns.factory.factory1.CacheFactory;
import com.soaic.zero.designpatterns.factory.factory2.MemoryCacheFactory;
import com.soaic.zero.designpatterns.factory.factory3.IAbstractFactory;
import com.soaic.zero.designpatterns.factory.factory3.MemoryFactory;

public class Test {


    public static void main(String[] args) {

        // 简单工厂
        ICache<String> cache = CacheFactory.createCache(1);
        ICache<String> cache0 = CacheFactory.createCache2(MemoryCache.class);
        cache.save("key","value");

        // 工厂方法
        com.soaic.zero.designpatterns.factory.factory2.ICacheFactory<String> factory1 = new MemoryCacheFactory<>();
        ICache<String> cache1 = factory1.createCache();
        cache1.save("key","value");

        // 抽象工厂
        IAbstractFactory<String> iAbstractFactory = new MemoryFactory<>();
        ICache<String> cache2 = iAbstractFactory.getCacheFactory().createCache();
        ICache<String> cache3 = iAbstractFactory.getOtherFactory().createOtherCache();
        cache2.save("key","value");

    }

}
