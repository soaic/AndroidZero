package com.soaic.zero.designpatterns.iterator;

public class DemoIterator implements Iterator<String> {

    private String[] names = {"Tom","Jack","Soaic"};

    int index = 0;

    @Override
    public boolean hasNext() {
        return index < names.length;
    }

    @Override
    public String next() {
        if (index >= names.length)
            return null;
        return names[index++];
    }
}
