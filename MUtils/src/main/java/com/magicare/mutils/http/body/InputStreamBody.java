package com.magicare.mutils.http.body;

import android.text.TextUtils;

import com.magicare.mutils.common.Callback;
import com.magicare.mutils.common.IOUtil;
import com.magicare.mutils.http.ProgressHandler;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Author: justin
 * Time: 2014/05/30
 */
public class InputStreamBody implements ProgressBody {

    private final long total;
    private InputStream content;
    private String contentType;
    private long current = 0;

    private ProgressHandler callBackHandler;

    public InputStreamBody(InputStream inputStream) {
        this(inputStream, null);
    }

    public InputStreamBody(InputStream inputStream, String contentType) {
        this.content = inputStream;
        this.contentType = contentType;
        this.total = getInputStreamLength(inputStream);
    }

    public static long getInputStreamLength(InputStream inputStream) {
        try {
            if (inputStream instanceof FileInputStream ||
                    inputStream instanceof ByteArrayInputStream) {
                return inputStream.available();
            }
        } catch (Throwable ignored) {
        }
        return -1L;
    }

    @Override
    public void setProgressHandler(ProgressHandler progressHandler) {
        this.callBackHandler = progressHandler;
    }

    @Override
    public long getContentLength() {
        return total;
    }

    @Override
    public String getContentType() {
        return TextUtils.isEmpty(contentType) ? "application/octet-stream" : contentType;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        if (callBackHandler != null && !callBackHandler.updateProgress(total, current, true)) {
            throw new Callback.CancelledException("upload stopped!");
        }

        byte[] buffer = new byte[1024];
        try {
            int len = 0;
            while ((len = content.read(buffer)) != -1) {
                out.write(buffer, 0, len);
                current += len;
                if (callBackHandler != null && !callBackHandler.updateProgress(total, current, false)) {
                    throw new Callback.CancelledException("upload stopped!");
                }
            }
            out.flush();

            if (callBackHandler != null) {
                callBackHandler.updateProgress(total, total, true);
            }
        } finally {
            IOUtil.closeQuietly(content);
        }
    }
}