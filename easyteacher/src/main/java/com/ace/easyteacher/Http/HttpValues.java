package com.ace.easyteacher.Http;

import com.magicare.mutils.http.RequestParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpValues {
    private HashMap<String, String> values;

    public HttpValues() {
        values = new HashMap<>();
    }

    public HttpValues(String key, String val) {
        this();
        add(key, val);
    }

    public void add(String key, String val) {
        values.put(key, val);
    }

    public HashMap<String, String> getValues() {
        return values;
    }

    public void buildHead(RequestParams rp) {
        if (rp != null) {
            Iterator iter = values.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                rp.addHeader(key, val);
            }
        }
    }


    public void buildParameter(RequestParams rp) {

        if (rp != null) {
            Iterator iter = values.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                rp.addParameter(key, val);
            }
        }
    }

    public void buildFiles(RequestParams rp) {
        if (rp != null) {
            rp.setMultipart(true);
            Iterator iter = values.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                File file = new File(val);
                try {
                    rp.addBodyParameter(key, new FileInputStream(file), "JPEG", file.getName());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public RequestParams buildRequsetParams(String url) {
        String[] cacheKey = new String[values.size()];
        Iterator iter = values.entrySet().iterator();
        int index = 0;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            cacheKey[index] = key;
            index++;
        }
        RequestParams rp;
        if (index > 0) {
            rp = new RequestParams(url, null, null, cacheKey);
        } else {
            rp = new RequestParams(url);
        }
        buildParameter(rp);
        return rp;
    }

    @Override
    public String toString() {
        return "HttpValues{" +
                "values=" + values +
                '}';
    }
}
