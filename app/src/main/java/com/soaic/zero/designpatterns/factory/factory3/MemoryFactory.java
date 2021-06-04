package com.soaic.zero.designpatterns.factory.factory3;

import com.soaic.zero.designpatterns.factory.factory2.ICacheFactory;
import com.soaic.zero.designpatterns.factory.factory2.MemoryCacheFactory;

public class MemoryFactory<T> implements IAbstractFactory<T>{

    @Override
    public ICacheFactory<T> getCacheFactory() {
        return new MemoryCacheFactory<>();
    }

    @Override
    public IOtherFactory<T> getOtherFactory() {
        return null;
    }

}
