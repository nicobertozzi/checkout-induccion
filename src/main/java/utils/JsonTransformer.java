package utils;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/*
Clase para que transforme la respuesta en un JSON
 */
public class JsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

}
