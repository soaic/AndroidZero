package com.soaic.zero.designpatterns.factory.factory3;

import com.soaic.zero.designpatterns.factory.ICache;

public interface IOtherFactory<T>{

    ICache<T> createOtherCache();

}
