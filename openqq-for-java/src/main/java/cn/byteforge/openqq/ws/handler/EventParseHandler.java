package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.util.Maps;
import cn.byteforge.openqq.ws.entity.enumerate.OpCode;
import cn.byteforge.openqq.ws.event.Event;
import cn.byteforge.openqq.ws.event.type.friend.*;
import cn.byteforge.openqq.ws.event.type.group.*;
import cn.byteforge.openqq.ws.event.type.interact.InteractionEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 反序列化事件处理
 * */
public class EventParseHandler extends ChainHandler {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(OpCode.class, new OpCodeTypeAdapter())
            .create();

    private static final Map<String, Class<? extends Event>> TYPE_EVENT_MAP;

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

    static {
        TYPE_EVENT_MAP = new HashMap<>();
        TYPE_EVENT_MAP.put("INTERACTION_CREATE", InteractionEvent.class);
        TYPE_EVENT_MAP.putAll(Maps.of(
                "C2C_MESSAGE_CREATE", C2CMessageEvent.class,
                "GROUP_AT_MESSAGE_CREATE", GroupAtMessageEvent.class
        ));
        TYPE_EVENT_MAP.putAll(Maps.of(
                "GROUP_ADD_ROBOT", GroupAddRobotEvent.class,
                "GROUP_DEL_ROBOT", GroupDelRobotEvent.class,
                "GROUP_MSG_RECEIVE", GroupMsgReceiveEvent.class,
                "GROUP_MSG_REJECT", GroupMsgRejectEvent.class
        ));
        TYPE_EVENT_MAP.putAll(Maps.of(
                "FRIEND_ADD", FriendAddEvent.class,
                "FRIEND_DEL", FriendDelEvent.class,
                "C2C_MSG_REJECT", C2CMsgRejectEvent.class,
                "C2C_MSG_RECEIVE", C2CMsgReceiveEvent.class
        ));
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
