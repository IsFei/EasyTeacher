package com.magicare.mutils.http.app;


import com.magicare.mutils.MLog;
import com.magicare.mutils.common.Callback;
import com.magicare.mutils.ex.HttpException;
import com.magicare.mutils.http.HttpMethod;
import com.magicare.mutils.http.request.UriRequest;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashSet;

/**
 * Author: justin
 * Time: 2014/05/30
 */
public final class HttpRetryHandler {

    private static HashSet<Class<?>> blackList = new HashSet<Class<?>>();

    static {
        blackList.add(HttpException.class);
        blackList.add(Callback.CancelledException.class);
        blackList.add(MalformedURLException.class);
        blackList.add(URISyntaxException.class);
        blackList.add(NoRouteToHostException.class);
        blackList.add(PortUnreachableException.class);
        blackList.add(ProtocolException.class);
        blackList.add(NullPointerException.class);
        blackList.add(FileNotFoundException.class);
        blackList.add(JSONException.class);
        blackList.add(UnknownHostException.class);
        blackList.add(IllegalArgumentException.class);
    }

    protected int maxRetryCount = 2;

    public HttpRetryHandler() {
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public boolean retryRequest(Throwable ex, int count, UriRequest request) {

        MLog.v(ex.getMessage(), ex);

        if (count > maxRetryCount || request == null) {
            MLog.w(HttpRetryHandler.class, "The Max Retry times has been reached!");
            return false;
        }

        if (!HttpMethod.permitsRetry(request.getParams().getMethod())) {
            MLog.w(HttpRetryHandler.class, "The Request Method can not be retried.");
            return false;
        }

        if (blackList.contains(ex.getClass())) {
            MLog.w(HttpRetryHandler.class, "The Exception can not be retried.");
            return false;
        }

        return true;
    }
}
