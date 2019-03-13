package controller;

import configuration.Credentials;
import model.Model;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class RenderScreenController {

    public static ModelAndView renderInicio(Request request, Response response) {
        System.out.println("rendering inicio....");
        HashMap<String, Object> mapa = new HashMap<>();

        if(Model.preference.getId() != null) {
            mapa.put("preferenceId", Model.preference.getId());
            mapa.put("preferenceInitPoint", Model.preference.getInitPoint());

            if(Model.preference.getPayer() != null) {
                mapa.put("payerName", Model.preference.getPayer().getName());
                mapa.put("payerSurname", Model.preference.getPayer().getSurname());
                mapa.put("payerEmail", Model.preference.getPayer().getEmail());
            }
            if(!Model.preference.getItems().isEmpty()) {
                mapa.put("itemTitle", Model.preference.getItems().get(0).getTitle());
                mapa.put("itemUnitPrice", Model.preference.getItems().get(0).getUnitPrice());
                mapa.put("itemQuantity", Model.preference.getItems().get(0).getQuantity());
            }
        }

        return new ModelAndView(mapa, "inicio");
    }

    public static ModelAndView renderPunto1(Request request, Response response) {
        System.out.println("rendering punto1....");
        HashMap<String, Object> mapa = new HashMap<>();

        mapa.put("publicKey", Credentials.PUBLIC_KEY);

        return new ModelAndView(mapa, "punto1");
    }

    public static ModelAndView renderPunto2(Request request, Response response) {
        System.out.println("rendering punto2....");
        HashMap<String, Object> mapa = new HashMap<>();

        if(Model.preference.getId() != null) {
            mapa.put("preferenceInitPoint", Model.preference.getInitPoint());
        }
        mapa.put("publicKey", Credentials.PUBLIC_KEY);

        return new ModelAndView(mapa, "punto2");
    }

    public static ModelAndView renderPunto3(Request request, Response response) {
        System.out.println("rendering punto3....");
        HashMap<String, Object> mapa = new HashMap<>();

        mapa.put("publicKey", Credentials.PUBLIC_KEY);

        return new ModelAndView(mapa, "punto3");
    }

    public static ModelAndView renderPunto4(Request request, Response response) {
        System.out.println("rendering punto4....");
        HashMap<String, Object> mapa = new HashMap<>();

        if(Model.preference.getId() != null) {
            Double transactionAmount = Model.preference.getItems().stream()
                    .mapToDouble(i -> (i.getQuantity() * i.getUnitPrice()))
                    .sum();

            mapa.put("transactionAmount", transactionAmount);
        }
        mapa.put("publicKey", Credentials.PUBLIC_KEY);

        return new ModelAndView(mapa, "punto4");
    }

    public static ModelAndView renderPunto5(Request request, Response response) {
        System.out.println("rendering punto5....");
        HashMap<String, Object> mapa = new HashMap<>();

        if(Model.preference.getId() != null) {
            mapa.put("preferenceId", Model.preference.getId());
        }
        mapa.put("publicKey", Credentials.PUBLIC_KEY);

        return new ModelAndView(mapa, "punto5");
    }

}
