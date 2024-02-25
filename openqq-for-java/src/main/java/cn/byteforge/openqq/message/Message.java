package cn.byteforge.openqq.message;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Message {

    /**
     * 消息数据
     * */
    @Expose
    private final Map<String, Object> data;


    @Override
    public String toString() {
        return new Gson().toJson(data);
    }

}
