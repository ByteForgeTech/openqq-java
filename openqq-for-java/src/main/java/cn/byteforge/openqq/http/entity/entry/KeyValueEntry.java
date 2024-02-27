package cn.byteforge.openqq.http.entity.entry;

import lombok.Data;

import java.util.Map;

/**
 * Entry to generate {"key": key, "value": value}
 * */
@Data
public class KeyValueEntry implements Map.Entry<String, Object> {

    private final String key;

    private final Object value;

    @Override
    public Object setValue(Object value) {
        throw new UnsupportedOperationException();
    }

}
