package com.magicare.mutils.http.loader;

import android.text.TextUtils;

import com.magicare.mutils.cache.HttpCacheEntity;
import com.magicare.mutils.common.IOUtil;
import com.magicare.mutils.http.RequestParams;
import com.magicare.mutils.http.request.UriRequest;

import java.io.InputStream;

/**
 * Author: justin
 * Time: 2014/05/30
 */
class StringLoader extends Loader<String> {

    private String charset = "UTF-8";
    private String resultStr = null;

    @Override
    public Loader<String> newInstance() {
        return new StringLoader();
    }

    @Override
    public void setParams(final RequestParams params) {
        if (params != null) {
            String charset = params.getCharset();
            if (!TextUtils.isEmpty(charset)) {
                this.charset = charset;
            }
        }
    }

    @Override
    public String load(final InputStream in) throws Throwable {
        resultStr = IOUtil.readStr(in, charset);
        return resultStr;
    }

    @Override
    public String load(final UriRequest request) throws Throwable {
        request.sendRequest();
        return this.load(request.getInputStream());
    }

    @Override
    public String loadFromCache(final HttpCacheEntity cacheEntity) throws Throwable {
        if (cacheEntity != null) {
            return cacheEntity.getContent();
        }

        return null;
    }

    @Override
    public void save2Cache(UriRequest request) {
        saveStringCache(request, resultStr);
    }
}
