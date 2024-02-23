package cn.byteforge.openqq.exception;

import cn.byteforge.openqq.ws.entity.Shard;
import cn.hutool.core.lang.Pair;

import java.util.Arrays;
import java.util.UUID;

/**
 * 分片异常
 * */
public class InvalidShardException extends RuntimeException {

    public InvalidShardException(Shard shard, Pair<UUID, Shard>[] shardsConfigured) {
        super(String.format("Invalid shard[%d, %d] in %s", shard.getIndex(), shard.getSize(), Arrays.toString(shardsConfigured)));
    }

}
