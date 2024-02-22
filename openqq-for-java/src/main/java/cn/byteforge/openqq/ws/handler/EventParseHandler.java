package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.ws.entity.OpCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import cn.byteforge.openqq.ws.event.Event;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 反序列化事件处理
 * */
public class EventParseHandler extends ChainHandler {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(OpCode.class, new OpCodeTypeAdapter())
            .create();

    // JsonObject -> Event
    @Override
    protected Event doHandle(Object object) {
        return GSON.fromJson((JsonObject) object, Event.class);
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
