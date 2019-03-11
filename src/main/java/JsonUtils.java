
import com.google.gson.Gson;

import spark.ResponseTransformer;

/*
Clase para que transforme la respuesta en un JSON y sea mas amigable la visualizacion
 */
public class JsonUtils {

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json() {
        return JsonUtils::toJson;
    }

}
