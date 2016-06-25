package com.magicare.mutils.cache;

/**
 * @author justin on 2015/12/11 15:58
 *         justin@magicare.me
 * @version V1.0
 */
public class HttpCacheEntity {
    private String key;
    private long time;
    private long life;
    private String content;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getLife() {
        return life;
    }

    public void setLife(long life) {
        this.life = life;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
