package cn.byteforge.openqq.http.entity.entry;

import lombok.Data;

/**
 * 链接+文本列表每项输入
 * */
@Data
public class LinkedTextEntry {

    /**
     * 文本内容
     * */
    private final String desc;

    /**
     * 链接，需要提前报备，如果不填就显示为文本，如果填了就显示为链接
     * */
    private final String link;

}
