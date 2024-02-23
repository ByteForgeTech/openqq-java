package cn.byteforge.openqq.ws.entity;

import cn.hutool.core.lang.Assert;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 链接分片配置
 * */
@Data
public class Shard {

    public static final Shard STANDALONE = new Shard(0, 1);

    /**
     * 当前分片索引
     * [0, size-1]
     * */
    private final Integer index;

    /**
     * 分片数
     * */
    private final Integer size;

    private Shard(Integer index, Integer size) {
        this.index = index;
        this.size = size;
        Assert.isTrue(0 <= index && index < size);
    }

    public int[] toArray() {
        return new int[] {index, size};
    }

    /**
     * 生成分片组
     * @param size 分片数
     * */
    public static Shard[] generate(int size) {
        Shard[] shards = new Shard[size];
        for (int i = 0; i < size; ++ i) {
            shards[i] = new Shard(i, size);
        }
        return shards;
    }

    /**
     * 生成分片
     * @param index 当前分片索引
     * @param size 分片数
     * */
    public static Shard generateOne(int index, int size) {
        return new Shard(index, size);
    }

}
