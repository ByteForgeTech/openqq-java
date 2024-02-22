package cn.byteforge.openqq.ws.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 链接分片配置
 * */
@Data
@AllArgsConstructor
public class Shard {

    public static final Shard STANDALONE = new Shard(0, 1);

    /**
     * 分片数
     * */
    private Integer size;

    /**
     * 当前分片索引
     * [0, size-1]
     * */
    private Integer index;

    public int[] toArray() {
        return new int[] {index, size};
    }

}
