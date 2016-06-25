package com.magicare.mutils.http.loader;


import android.text.TextUtils;

import com.magicare.mutils.cache.HttpCache;
import com.magicare.mutils.cache.HttpCacheEntity;
import com.magicare.mutils.http.ProgressHandler;
import com.magicare.mutils.http.RequestParams;
import com.magicare.mutils.http.app.RequestTracker;
import com.magicare.mutils.http.request.UriRequest;

import java.io.InputStream;

/**
 * Author: justin
 * Time: 2014/05/26
 */
public abstract class Loader<T> {

    protected RequestParams params;
    protected RequestTracker tracker;
    protected ProgressHandler progressHandler;

    public void setParams(final RequestParams params) {
        this.params = params;
    }

    public void setProgressHandler(final ProgressHandler callbackHandler) {
        this.progressHandler = callbackHandler;
    }

    public RequestTracker getResponseTracker() {
        return this.tracker;
    }

    public void setResponseTracker(RequestTracker tracker) {
        this.tracker = tracker;
    }

    protected void saveStringCache(UriRequest request, String resultStr) {
        if (!TextUtils.isEmpty(resultStr)) {
            HttpCacheEntity entity = new HttpCacheEntity();

            entity.setKey(request.getCacheKey());
            entity.setTime(System.currentTimeMillis());
            entity.setLife(request.getExpiration());
            entity.setContent(resultStr);
            HttpCache.update(entity);
        }
    }

    public abstract Loader<T> newInstance();

    public abstract T load(final InputStream in) throws Throwable;

    public abstract T load(final UriRequest request) throws Throwable;

    public abstract T loadFromCache(final HttpCacheEntity cacheEntity) throws Throwable;

    public abstract void save2Cache(final UriRequest request);
}
