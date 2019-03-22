package controllers;

import constants.Credentials;
import model.PreferenceModel;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class RenderScreenController {

    public static ModelAndView renderIndex(Request request, Response response) {
        System.out.println("Rendering Index....");
        HashMap<String, Object> mapa = new HashMap<>();

        if(PreferenceModel.preference.getId() != null) {
            mapa.put("preferenceId", PreferenceModel.preference.getId());
            mapa.put("preferenceInitPoint", PreferenceModel.preference.getInitPoint());

            if(PreferenceModel.preference.getPayer() != null) {
                mapa.put("payerName", PreferenceModel.preference.getPayer().getName());
                mapa.put("payerSurname", PreferenceModel.preference.getPayer().getSurname());
                mapa.put("payerEmail", PreferenceModel.preference.getPayer().getEmail());
            }
            if(!PreferenceModel.preference.getItems().isEmpty()) {
                mapa.put("items", PreferenceModel.preference.getItems());
            }
        }
        mapa.put("preferenceCreated", PreferenceModel.preference.getId() != null);

        return new ModelAndView(mapa, "index");
    }

    public static ModelAndView renderPage1(Request request, Response response) {
        System.out.println("Rendering Page 1....");
        HashMap<String, Object> mapa = new HashMap<>();

        mapa.put("preferenceCreated", PreferenceModel.preference.getId() != null);

        return new ModelAndView(mapa, "page1");
    }

    public static ModelAndView renderPage2(Request request, Response response) {
        System.out.println("Rendering Page 2....");
        HashMap<String, Object> mapa = new HashMap<>();

        if(PreferenceModel.preference.getId() != null) {
            mapa.put("preferenceInitPoint", PreferenceModel.preference.getInitPoint());
        }
        mapa.put("preferenceCreated", PreferenceModel.preference.getId() != null);

        return new ModelAndView(mapa, "page2");
    }

    public static ModelAndView renderPage3(Request request, Response response) {
        System.out.println("Rendering Page 3....");
        HashMap<String, Object> mapa = new HashMap<>();

        mapa.put("preferenceCreated", PreferenceModel.preference.getId() != null);

        addPublicKey(mapa);

        return new ModelAndView(mapa, "page3");
    }

    public static ModelAndView renderPage4(Request request, Response response) {
        System.out.println("Rendering Page 4....");
        HashMap<String, Object> mapa = new HashMap<>();

        if(PreferenceModel.preference.getId() != null) {
            Double transactionAmount = PreferenceModel.preference.getItems().stream()
                    .mapToDouble(i -> (i.getQuantity() * i.getUnitPrice()))
                    .sum();

            mapa.put("transactionAmount", transactionAmount);
        }
        mapa.put("preferenceCreated", PreferenceModel.preference.getId() != null);

        addPublicKey(mapa);

        return new ModelAndView(mapa, "page4");
    }

    public static ModelAndView renderPage5(Request request, Response response) {
        System.out.println("Rendering Page 5....");
        HashMap<String, Object> mapa = new HashMap<>();

        if(PreferenceModel.preference.getId() != null) {
            mapa.put("preferenceId", PreferenceModel.preference.getId());
        }
        mapa.put("preferenceCreated", PreferenceModel.preference.getId() != null);

        addPublicKey(mapa);

        return new ModelAndView(mapa, "page5");
    }

    private static void addPublicKey(HashMap<String, Object> params) {
        params.put("publicKey", Credentials.PUBLIC_KEY_SANDBOX);
        params.put("publicKeyProd", Credentials.PUBLIC_KEY_PROD);
    }

}
