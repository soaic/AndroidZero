package com.soaic.zero.designpatterns.strategy;

public abstract class CacheStrategy {

    public abstract boolean isDataCacheable(DataSource dataSource);

    public abstract boolean isResourceCacheable(
            boolean isFromAlternateCacheKey, DataSource dataSource, EncodeStrategy encodeStrategy);

    /** 是否缓存解码的资源数据 */
    public abstract boolean decodeCachedResource();

    /** 是否缓存解码的源数据 */
    public abstract boolean decodeCachedData();

    public enum DataSource {
        LOCAL,
        REMOTE,
        DATA_DISK_CACHE,
        RESOURCE_DISK_CACHE,
        MEMORY_CACHE,
    }

    public enum EncodeStrategy {
        SOURCE,
        TRANSFORMED,
        NONE,
    }


    public static final CacheStrategy NONE = new CacheStrategy() {
        @Override
        public boolean isDataCacheable(DataSource dataSource) {
            return false;
        }

        @Override
        public boolean isResourceCacheable(boolean isFromAlternateCacheKey, DataSource dataSource, EncodeStrategy encodeStrategy) {
            return false;
        }

        @Override
        public boolean decodeCachedResource() {
            return false;
        }

        @Override
        public boolean decodeCachedData() {
            return false;
        }
    };

    public static final CacheStrategy ALL = new CacheStrategy() {
        @Override
        public boolean isDataCacheable(DataSource dataSource) {
            return dataSource == DataSource.REMOTE;
        }

        @Override
        public boolean isResourceCacheable(boolean isFromAlternateCacheKey, DataSource dataSource, EncodeStrategy encodeStrategy) {
            return dataSource != DataSource.RESOURCE_DISK_CACHE
                    && dataSource != DataSource.MEMORY_CACHE;
        }

        @Override
        public boolean decodeCachedResource() {
            return true;
        }

        @Override
        public boolean decodeCachedData() {
            return true;
        }
    };

    public static class Glide {
        CacheStrategy diskCacheStrategy;
        private String getNextStage(int current) {
            switch (current) {
                case 0:
                    return diskCacheStrategy.decodeCachedResource()
                            ? "Stage.RESOURCE_CACHE"
                            : getNextStage(1);
                case 1:
                    return diskCacheStrategy.decodeCachedData()
                            ? "Stage.DATA_CACHE"
                            : getNextStage(2);
                case 2:
                    return "Stage.FINISHED";
                default:
                    throw new IllegalArgumentException("Unrecognized stage: " + current);
            }
        }

        public void cacheStrategy(CacheStrategy cacheStrategy) {
            this.diskCacheStrategy = cacheStrategy;
        }

        public void start() {
            String stage = getNextStage(0);
            System.out.println("stage="+stage);
        }
    }

    public static void main(String[] args) {
        Glide glide = new Glide();
        glide.cacheStrategy(CacheStrategy.ALL);
        glide.start();
    }
}
