package com.soaic.zero.designpatterns.factory.factory2;


import com.soaic.zero.designpatterns.factory.ICache;
import com.soaic.zero.designpatterns.factory.MemoryCache;

public class MemoryCacheFactory<T> implements ICacheFactory<T> {

    @Override
    public ICache<T> createCache() {
         return new MemoryCache<>();
    }
}
