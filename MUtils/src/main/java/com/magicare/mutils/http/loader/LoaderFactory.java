package com.magicare.mutils.http.loader;


import com.magicare.mutils.http.RequestParams;
import com.magicare.mutils.http.app.RequestTracker;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Author: justin
 * Time: 2014/05/26
 */
public final class LoaderFactory {

    private LoaderFactory() {
    }

    private static RequestTracker defaultTracker;

    /**
     * key: loadType
     */
    private static final HashMap<Type, RequestTracker> trackerHashMap = new HashMap<>();

    /**
     * key: loadType
     */
    private static final HashMap<Type, Loader> converterHashMap = new HashMap<Type, Loader>();

    static {
        converterHashMap.put(String.class, new StringLoader());
        converterHashMap.put(File.class, new FileLoader());
    }

    public static Loader<?> getLoader(Type type, RequestParams params) {
        Loader<?> result = converterHashMap.get(type);
        if (result == null) {
            result = converterHashMap.get(String.class);
        } else {
            result = result.newInstance();
        }
        result.setParams(params);
        RequestTracker tracker = trackerHashMap.get(type);
        result.setResponseTracker(tracker == null ? defaultTracker : tracker);
        return result;
    }

    public static <T> void registerLoader(Type type, Loader<T> loader) {
        converterHashMap.put(type, loader);
    }

    public static void registerDefaultTracker(RequestTracker tracker) {
        defaultTracker = tracker;
    }

    public static void registerTracker(Type type, RequestTracker tracker) {
        trackerHashMap.put(type, tracker);
    }
}
