package com.ace.easyteacher.Http;

import android.text.TextUtils;

import com.ace.easyteacher.Bean.ResultBean;
import com.alibaba.fastjson.JSON;
import com.magicare.mutils.M;
import com.magicare.mutils.MLog;
import com.magicare.mutils.common.Callback;
import com.magicare.mutils.http.HttpMethod;
import com.magicare.mutils.http.RequestParams;

import org.xutils.ex.DbException;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HttpUtils {

    public static <T> Callback.Cancelable post(String url, HttpValues parameter,
                                               HttpCallback<T> callback, Class<T> resultType) {
        return post(url, null, parameter, callback, resultType, -1);
    }

    public static <T> Callback.Cancelable post(String url, HttpValues parameter,
                                               HttpCallback<T> callback, Class<T> resultType, long life) {
        return post(url, null, parameter, callback, resultType, life);
    }

    public static <T> Callback.Cancelable post(String url, HttpValues head, HttpValues parameter,
                                               final HttpCallback<T> callback,
                                               final Class<T> resultType,
                                               final long life) {
        return request(HttpMethod.POST, url, head, parameter, callback, resultType, life);
    }

    public static <T> Callback.Cancelable get(String url, HttpValues parameter,
                                              HttpCallback<T> callback, Class<T> resultType) {
        return get(url, null, parameter, callback, resultType, -1);
    }

    public static <T> Callback.Cancelable get(String url, HttpValues parameter,
                                              HttpCallback<T> callback, Class<T> resultType, long life) {
        return get(url, null, parameter, callback, resultType, life);
    }

    public static <T> Callback.Cancelable get(String url, HttpValues head, HttpValues parameter,
                                              final HttpCallback<T> callback,
                                              final Class<T> resultType,
                                              final long life) {
        return request(HttpMethod.GET, url, head, parameter, callback, resultType, life);
    }

    public static <T> Callback.Cancelable put(String url, HttpValues parameter,
                                              HttpCallback<T> callback, Class<T> resultType) {
        return put(url, null, parameter, callback, resultType, -1);
    }

    public static <T> Callback.Cancelable put(String url, HttpValues parameter,
                                              HttpCallback<T> callback, Class<T> resultType, long life) {
        return put(url, null, parameter, callback, resultType, life);
    }

    public static <T> Callback.Cancelable put(String url, HttpValues head, HttpValues parameter,
                                              final HttpCallback<T> callback,
                                              final Class<T> resultType,
                                              final long life) {
        return request(HttpMethod.PUT, url, head, parameter, callback, resultType, life);
    }

    public static <T> Callback.Cancelable putFile(String url, HttpValues head, HttpValues parameter,
                                                  HttpValues files,
                                                  final HttpCallback<T> callback,
                                                  final Class<T> resultType) {
        return request(HttpMethod.PUT, url, head, parameter, files, callback, resultType, -1);
    }

    public static <T> Callback.Cancelable postFile(String url, HttpValues head, HttpValues parameter,
                                                   HttpValues files,
                                                   final HttpCallback<T> callback,
                                                   final Class<T> resultType) {
        return request(HttpMethod.POST, url, head, parameter, files, callback, resultType, -1);
    }


    public static Callback.Cancelable postDownload(String url, String path,
                                                   HttpUtils.DownloadCallback callback) {
        return postDownload(url, null, path, callback);
    }

    public static Callback.Cancelable postDownload(String url, HttpValues head, String path,
                                                   HttpUtils.DownloadCallback callback) {
        return download(HttpMethod.POST, url, head, path, callback);
    }


    public static Callback.Cancelable getDownload(String url, String path,
                                                  HttpUtils.DownloadCallback callback) {
        return getDownload(url, null, path, callback);
    }

    public static Callback.Cancelable getDownload(String url, HttpValues head, String path,
                                                  HttpUtils.DownloadCallback callback) {
        return download(HttpMethod.GET, url, head, path, callback);
    }

    public static Callback.Cancelable download(HttpMethod method, String url, HttpValues head, final String path,
                                               final HttpUtils.DownloadCallback callback) {
        RequestParams rp = new RequestParams(url);
        if (head != null) {
            head.buildHead(rp);
        }
        rp.setSaveFilePath(path);
        Callback.CommonCallback<File> commonCallback = new Callback.CacheCallback<File>() {
            @Override
            public boolean onCache(File result) {
                return false;
            }

            @Override
            public void onSuccess(File result) {
                if (callback != null) {
                    callback.onSuccess(path);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (callback != null) {
                    callback.onError(ex);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                if (callback != null) {
                    callback.onCancelled(cex);
                }
            }

            @Override
            public void onFinished() {
                if (callback != null) {
                    callback.onFinished();
                }
            }
        };
        return M.http().request(method, rp, commonCallback);
    }

    private static <T> Callback.Cancelable request(HttpMethod method, String url, HttpValues head, HttpValues parameter,
                                                   final HttpCallback<T> callback,
                                                   final Class<T> resultType,
                                                   final long life) {

        return request(method, url, head, parameter, null, callback, resultType, life);
    }

    private static <T> Callback.Cancelable request(HttpMethod method, final String url, HttpValues head,
                                                   HttpValues parameter,
                                                   HttpValues files,
                                                   final HttpCallback<T> callback,
                                                   final Class<T> resultType,
                                                   final long life) {

        RequestParams rp;

        if (parameter != null && files == null) {
            rp = parameter.buildRequsetParams(url);
        } else {
            rp = new RequestParams(url);
        }
        if (parameter != null && files != null) {
            Iterator iter = parameter.getValues().entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                rp.addQueryStringParameter(key, val);
            }
            files.buildFiles(rp);
        } else if (files != null) {
            rp.setMultipart(true);
            files.buildFiles(rp);
        }
        if (life > 0) {
            rp.setCacheMaxAge(life);
        }
        if (head != null) {
            head.buildHead(rp);
        }

        Callback.CacheCallback<String> cacheCallback = new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return life > 0 && parseResult(callback, resultType, result);
            }

            @Override
            public void onSuccess(String result) {
                MLog.v("HttpUtils", url + " onSuccess result:" + result);
                parseResult(callback, resultType, result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MLog.d("HttpUtils", url + " onError:" + ex);
                if (callback != null) {
                    callback.onError(ex, isOnCallback);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                if (callback != null) {
                    callback.onCancelled(cex);
                }
            }

            @Override
            public void onFinished() {
                if (callback != null) {
                    callback.onFinished();
                }
            }
        };

        return M.http().request(method, rp, cacheCallback);
    }

    private static <T> boolean parseResult(final HttpCallback<T> callback,
                                           final Class<T> resultType, String result) {
        if (callback != null) {
            try {
                char char1 = result.charAt(8);
                if(char1 == ','){
                    StringBuilder builder = new StringBuilder();
                    builder.append(result.substring(0,8));
                    builder.append("\"\"");
                    builder.append(result.substring(8));
                    result = builder.toString();
                }
                ResultBean resultBean = parseObject(result);
                if (resultBean.isSuccess()) {
                    String json = resultBean.getData();
                    if (resultType.equals(ResultBean.class)) {
                        callback.onSuccess((T) resultBean);
                        return true;
                    }
                    if (TextUtils.isEmpty(json)) {
                        callback.onSuccess((T)"");
                        return true;
                    }
                    if (resultType.equals(String.class)) {
                        callback.onSuccess((T) result);
                        return true;
                    }
                    if (json.startsWith("[")) {
                        callback.onSuccess(parseList(json, resultType));
                        return true;
                    }
                    callback.onSuccess(parseObject(json, resultType));
                    return true;
                } else if (resultBean.getStatus().equals("fail")) {
                    // TODO 登陆异常处理
                } else throw new ParseException(result);
            } catch (Exception ex) {
                callback.onError(ex, true);
            }
        }
        return false;
    }


    private static ResultBean parseObject(String result) {
        return JSON.parseObject(result, ResultBean.class);
    }

    public static <T> T parseObject(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }

    public static <T> List<T> parseList(String json, Class<T> cls) {
        return JSON.parseArray(json, cls);
    }


    public interface HttpCallback<T> {
        void onError(Throwable ex, boolean isOnCallback);

        void onSuccess(T result);

        void onSuccess(List<T> result) throws DbException;

        void onCancelled(Callback.CancelledException cex);

        void onFinished();
    }

    public interface DownloadCallback {
        void onError(Throwable ex);

        void onSuccess(String filepath);

        void onCancelled(Callback.CancelledException cex);

        void onFinished();
    }

    static class ParseException extends RuntimeException {
        public ParseException(String detailMessage) {
            super(detailMessage);
        }
    }
}
