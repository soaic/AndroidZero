package com.soaic.zero.designpatterns.factory.factory3;

import com.soaic.zero.designpatterns.factory.factory2.ICacheFactory;

public interface IAbstractFactory<T> {

    ICacheFactory<T> getCacheFactory();

    IOtherFactory<T> getOtherFactory();

}
