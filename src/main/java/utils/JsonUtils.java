package utils;

import com.google.gson.Gson;

public class JsonUtils {

    public static String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <R> R fromJson(String body, Class<R> returnClass) {
        Gson gson = new Gson();
        return gson.fromJson(body, returnClass);
    }

}
