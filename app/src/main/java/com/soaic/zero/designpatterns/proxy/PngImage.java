package com.soaic.zero.designpatterns.proxy;

public class PngImage implements Image{
    private final String imageName;
    public PngImage(String imageName) {
        this.imageName = imageName;
    }
    @Override
    public void display() {
        System.out.println(imageName + " display");
    }
}
