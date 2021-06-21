package com.soaic.zero.designpatterns.proxy;

public class ProxyPngImage implements Image{
    private final Image image;
    public ProxyPngImage(Image pngImage) {
        this.image = pngImage;
    }

    @Override
    public void display() {
        image.display();
    }
}
