package com.soaic.zero.designpatterns.factory.factory2;

import com.soaic.zero.designpatterns.factory.ICache;

public interface ICacheFactory<T>{

    ICache<T> createCache();

}
