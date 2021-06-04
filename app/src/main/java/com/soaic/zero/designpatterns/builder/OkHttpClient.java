package com.soaic.zero.designpatterns.builder;

// builder 设计模式
public class OkHttpClient {
    private int connectTimeout;
    private int readTimeout;
    private int writeTimeout;

    public OkHttpClient(Builder builder) {
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;

        OkHttpClient client = OkHttpClient.newBuilder()
                .connectTimeout(10_000)
                .readTimeout(10_000)
                .writeTimeout(10_000)
                .build();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private static class Builder {
        private int connectTimeout;
        private int readTimeout;
        private int writeTimeout;

        private Builder() {
            // 初始化
            connectTimeout = 10_000;
            readTimeout = 10_000;
            writeTimeout = 10_000;
        }


        public Builder connectTimeout(int timeout) {
            this.connectTimeout = timeout;
            return this;
        }

        public Builder readTimeout(int timeout) {
            this.readTimeout = timeout;
            return this;
        }

        public Builder writeTimeout(int timeout) {
            this.writeTimeout = timeout;
            return this;
        }

        public OkHttpClient build() {
            return new OkHttpClient(this);
        }

    }

}
