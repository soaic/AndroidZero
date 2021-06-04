package com.soaic.zero.designpatterns.factory.factory3;

import com.soaic.zero.designpatterns.factory.factory2.DiskCacheFactory;
import com.soaic.zero.designpatterns.factory.factory2.ICacheFactory;

public class DiskFactory<T> implements IAbstractFactory<T>{

    @Override
    public ICacheFactory<T> getCacheFactory() {
        return new DiskCacheFactory<>();
    }

    @Override
    public IOtherFactory<T> getOtherFactory() {
        return null;
    }

}
