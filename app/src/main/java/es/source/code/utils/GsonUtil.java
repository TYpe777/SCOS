package es.source.code.utils;

import com.google.gson.Gson;

import es.source.code.model.Food;

/**
 * @author taoye
 * @date 2018/11/4.
 * @classname GsonUtil.java
 * @description 将Json格式的字符串通过Gson反序列化为对应的Java对象
 */
public class GsonUtil {
    /**
     * 泛型。使用Gson反序列化
     * @param jsonData
     * @param type
     * @param <T>
     * @return
     */
    private static <T> T parseJsonWithGson(String jsonData, Class<T> type){
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }

    /**
     * 将Json格式的String反序列化为对应的Food对象
      * @param json
     * @return
     */
    public static Food getFoodFromJsonWithGSON(String json){
        return parseJsonWithGson(json, Food.class);
    }
}
