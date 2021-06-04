package com.soaic.zero.designpatterns.factory;

public interface ICache<T> {

    void save(String key, T value);

    T get(String key);

}
