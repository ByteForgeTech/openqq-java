package com.illtamer.openqq.ws.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.illtamer.openqq.ws.event.Event;

/**
 * 反序列化事件处理
 * */
public class EventParseHandler extends ChainHandler {

    public static final Gson GSON = new GsonBuilder().create();

    // JsonObject -> Event
    @Override
    protected Event doHandle(Object object) {
        return GSON.fromJson((JsonObject) object, Event.class);
    }

}
