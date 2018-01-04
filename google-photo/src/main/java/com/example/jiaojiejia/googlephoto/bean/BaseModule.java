package com.example.jiaojiejia.googlephoto.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created by jiaojie.jia on 2017/7/10.
 */
@Entity
public class BaseModule {

    @Id(autoincrement = true)
    private Long id;

    @Index(unique = true)
    private String key;

    private String json;

    @Generated(hash = 516328994)
    public BaseModule(Long id, String key, String json) {
        this.id = id;
        this.key = key;
        this.json = json;
    }

    @Generated(hash = 684342511)
    public BaseModule() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
