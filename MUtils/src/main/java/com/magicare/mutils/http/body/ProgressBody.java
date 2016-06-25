package com.magicare.mutils.http.body;


import com.magicare.mutils.http.ProgressHandler;

/**
 * Created by justin on 15/8/13.
 */
public interface ProgressBody extends RequestBody {
    void setProgressHandler(ProgressHandler progressHandler);
}
