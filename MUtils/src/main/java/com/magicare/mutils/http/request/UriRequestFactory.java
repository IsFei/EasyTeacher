package com.magicare.mutils.http.request;


import com.magicare.mutils.http.RequestParams;

import java.lang.reflect.Type;

/**
 * Created by justin on 15/11/4.
 * Uri请求创建工厂
 */
public final class UriRequestFactory {


    private UriRequestFactory() {
    }

    public static UriRequest getUriRequest(RequestParams params, Type loadType) throws Throwable {
        String uri = params.getUri();
        if (uri.startsWith("http")) {
            return new HttpRequest(params, loadType);
        } else {
            throw new IllegalArgumentException("The url not be support: " + uri);
        }
    }

}
