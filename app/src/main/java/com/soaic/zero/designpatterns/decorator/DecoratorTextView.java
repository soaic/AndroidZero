package com.soaic.zero.designpatterns.decorator;

public class DecoratorTextView extends TextView{

    private TextView textView;

    public DecoratorTextView(TextView textView) {
        this.textView = textView;
    }

    public int getWidth() {
        return textView.width;
    }

    public void setWidth(int width) {
        textView.width = width;
    }

    public int getHeight() {
        return textView.height;
    }

    public void setHeight(int height) {
        textView.height = height;
    }
}
