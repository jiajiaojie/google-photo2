package com.example.jiaojiejia.googlephoto.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created by jiaojie.jia on 2017/8/24.
 */
@Entity
public class PhotoModule {

    @Id(autoincrement = true)
    private Long id;

//    @Index(unique = true)
    private int imageId;

    @Index(unique = true)
    private String origin;

    private String compress;

    private String remote;

    @Generated(hash = 734822693)
    public PhotoModule() {
    }

    @Generated(hash = 337026826)
    public PhotoModule(Long id, int imageId, String origin, String compress, String remote) {
        this.id = id;
        this.imageId = imageId;
        this.origin = origin;
        this.compress = compress;
        this.remote = remote;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCompress() {
        return this.compress;
    }

    public void setCompress(String compress) {
        this.compress = compress;
    }

    public String getRemote() {
        return this.remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }
}
