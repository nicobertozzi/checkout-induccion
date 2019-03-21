package utils;

import spark.Request;

public class RequestUtil {

    private RequestUtil() {}

    public static String getBodyAsJSON(Request request) {
        try {
            return Json.INSTANCE.convertToJSON(request.body());
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getBodyAsObject(Request request, Class<T> tClass) {
        try {
            return Json.INSTANCE.convertToObject(request.body(), tClass);
        } catch (Exception e) {
            return null;
        }
    }

}