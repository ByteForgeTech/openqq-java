package cn.byteforge.openqq.http.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Date;

/**
 * 上传文件信息
 * */
@Data
public class FileInfo {

    /**
     * 文件创建时间
     * */
    @Expose
    private Date createTime;

    /**
     * 文件 ID
     * */
    @SerializedName("file_uuid")
    private String fileUuid;

    /**
     * 文件信息，用于发消息接口的 media 字段使用
     * */
    @SerializedName("file_info")
    private String fileInfo;

    /**
     * 有效期，表示剩余多少秒到期，到期后 file_info 失效，当等于 0 时，表示可长期使用
     * */
    private Integer ttl;

    /**
     * 检查文件是否过期
     * */
    public boolean expired() {
        return new Date().after(new Date(createTime.getTime() + ttl * 1000L - 100));
    }

}
