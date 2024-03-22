package cn.byteforge.openqq.http.entity.entry;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

/**
 * Entry to generate {"key": key, "values": values}
 * */
@Data
public class KeyValuesEntry implements Map.Entry<String, Object[]> {

    private final String key;

    @SerializedName("values")
    private final Object[] value;

    public KeyValuesEntry(String key, Object ...values) {
        this.key = key;
        this.value = values;
    }


    @Override
    public Object[] setValue(Object[] value) {
        throw new UnsupportedOperationException();
    }

}
