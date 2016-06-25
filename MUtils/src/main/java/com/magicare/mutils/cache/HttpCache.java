package com.magicare.mutils.cache;

/**
 * @author justin on 2015/12/11 16:36
 *         justin@magicare.me
 * @version V1.0
 */
public class HttpCache {
    private static LruCache<String, HttpCacheEntity> cache;
    private static final int MAXMEMONRY = (int) (Runtime.getRuntime().maxMemory() / 16);

    public static HttpCacheEntity getCache(String key) {
        if (cache == null) {
            cache = new LruCache<String, HttpCacheEntity>(MAXMEMONRY) {
                @Override
                protected int sizeOf(String key, HttpCacheEntity value) {
                    return value.getContent().length();
                }
            };
        }
        HttpCacheEntity cacheEntity = cache.get(key);
        if (cacheEntity == null) {
            return null;
        }
        if (cacheEntity.getLife() > System.currentTimeMillis()) {
            return cacheEntity;
        } else {
            cache.remove(key);
        }
        return null;
    }

    public static void update(HttpCacheEntity entity) {
        if (cache == null) {
            cache = new LruCache<String, HttpCacheEntity>(MAXMEMONRY) {
                @Override
                protected int sizeOf(String key, HttpCacheEntity value) {
                    return value.getContent().length();
                }
            };
        }
        cache.put(entity.getKey(), entity);
    }

}
