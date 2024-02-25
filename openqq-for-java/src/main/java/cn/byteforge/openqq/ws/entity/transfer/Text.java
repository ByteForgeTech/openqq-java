package cn.byteforge.openqq.ws.entity.transfer;

import lombok.Data;

/**
 * 文本消息
 * */
@Data
public class Text implements Transfer {

    private String content;

}
