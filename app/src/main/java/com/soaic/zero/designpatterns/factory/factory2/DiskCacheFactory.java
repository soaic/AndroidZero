package com.soaic.zero.designpatterns.factory.factory2;


import com.soaic.zero.designpatterns.factory.DiskCache;
import com.soaic.zero.designpatterns.factory.ICache;

public class DiskCacheFactory<T> implements ICacheFactory<T> {

    @Override
    public ICache<T> createCache() {
        return new DiskCache<>();
    }
}
