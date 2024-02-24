package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.util.Maps;
import cn.byteforge.openqq.ws.entity.OpCode;
import cn.byteforge.openqq.ws.event.Event;
import cn.byteforge.openqq.ws.event.type.C2CMessageEvent;
import cn.byteforge.openqq.ws.event.type.GroupAtMessageEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;

/**
 * 反序列化事件处理
 * */
public class EventParseHandler extends ChainHandler {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(OpCode.class, new OpCodeTypeAdapter())
            .create();

    private static final Map<String, Class<? extends Event>> TYPE_EVENT_MAP = Maps.of(
            "C2C_MESSAGE_CREATE", C2CMessageEvent.class,
            "GROUP_AT_MESSAGE_CREATE", GroupAtMessageEvent.class
    );

    // JsonObject -> Event
    @Override
    protected Event doHandle(Object object) {
        JsonObject json = (JsonObject) object;
        if (json.has("t")) {
            Class<? extends Event> type = TYPE_EVENT_MAP.get(json.get("t").getAsString());
            // 不对事件类型进行感知
            type = type == null ? Event.class : type;
            return GSON.fromJson(json, type);
        }
        return GSON.fromJson(json, Event.class);
    }

    private static class OpCodeTypeAdapter extends TypeAdapter<OpCode> {

        @Override
        public void write(JsonWriter out, OpCode value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.value(value.getCode());
        }

        @Override
        public OpCode read(JsonReader in) throws IOException {
            if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return OpCode.parse(in.nextInt());
        }

    }

}
