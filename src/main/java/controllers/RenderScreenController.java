package controllers;

import constants.Credentials;
import model.PreferenceModel;
import model.dto.ItemDTO;
import model.dto.PayerDTO;
import model.dto.PreferenceDTO;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;

public class RenderScreenController {

    public static ModelAndView renderInicio(Request request, Response response) {
        System.out.println("rendering inicio....");
        HashMap<String, Object> mapa = new HashMap<>();

        if(PreferenceModel.preference.getId() != null) {
            mapa.put("preferenceId", PreferenceModel.preference.getId());
            mapa.put("preferenceInitPoint", PreferenceModel.preference.getInitPoint());
            //mapa.put("preferenceSandboxInitPoint", PreferenceModel.preference.getSandboxInitPoint());

            if(PreferenceModel.preference.getPayer() != null) {
                mapa.put("payerName", PreferenceModel.preference.getPayer().getName());
                mapa.put("payerSurname", PreferenceModel.preference.getPayer().getSurname());
                mapa.put("payerEmail", PreferenceModel.preference.getPayer().getEmail());
            }
            if(!PreferenceModel.preference.getItems().isEmpty()) {
                mapa.put("itemTitle", PreferenceModel.preference.getItems().get(0).getTitle());
                mapa.put("itemUnitPrice", PreferenceModel.preference.getItems().get(0).getUnitPrice());
                mapa.put("itemQuantity", PreferenceModel.preference.getItems().get(0).getQuantity());
            }
        }
        mapa.put("preferenceCreated", PreferenceModel.preference.getId() != null);

        return new ModelAndView(mapa, "inicio");
    }

    public static ModelAndView renderPunto1(Request request, Response response) {
        System.out.println("rendering punto1....");
        HashMap<String, Object> mapa = new HashMap<>();

        mapa.put("preferenceCreated", PreferenceModel.preference.getId() != null);

        return new ModelAndView(mapa, "punto1");
    }

    public static ModelAndView renderPunto2(Request request, Response response) {
        System.out.println("rendering punto2....");
        HashMap<String, Object> mapa = new HashMap<>();

        if(PreferenceModel.preference.getId() != null) {
            mapa.put("preferenceInitPoint", PreferenceModel.preference.getInitPoint());
            mapa.put("preferenceSandboxInitPoint", PreferenceModel.preference.getSandboxInitPoint());
        }
        mapa.put("preferenceCreated", PreferenceModel.preference.getId() != null);

        return new ModelAndView(mapa, "punto2");
    }

    public static ModelAndView renderPunto3(Request request, Response response) {
        System.out.println("rendering punto3....");
        HashMap<String, Object> mapa = new HashMap<>();

        mapa.put("preferenceCreated", PreferenceModel.preference.getId() != null);

        addPublicKey(mapa);

        return new ModelAndView(mapa, "punto3");
    }

    public static ModelAndView renderPunto4(Request request, Response response) {
        System.out.println("rendering punto4....");
        HashMap<String, Object> mapa = new HashMap<>();

        if(PreferenceModel.preference.getId() != null) {
            Double transactionAmount = PreferenceModel.preference.getItems().stream()
                    .mapToDouble(i -> (i.getQuantity() * i.getUnitPrice()))
                    .sum();

            mapa.put("transactionAmount", transactionAmount);
        }
        mapa.put("preferenceCreated", PreferenceModel.preference.getId() != null);

        addPublicKey(mapa);

        return new ModelAndView(mapa, "punto4");
    }

    public static ModelAndView renderPunto5(Request request, Response response) {
        System.out.println("rendering punto5....");
        HashMap<String, Object> mapa = new HashMap<>();

        if(PreferenceModel.preference.getId() != null) {
            mapa.put("preferenceId", PreferenceModel.preference.getId());
        }
        mapa.put("preferenceCreated", PreferenceModel.preference.getId() != null);

        addPublicKey(mapa);

        return new ModelAndView(mapa, "punto5");
    }

    private static void addPublicKey(HashMap<String, Object> params) {
        params.put("publicKey", Credentials.PUBLIC_KEY_SANDBOX);
        //params.put("publicKeyProd", Credentials.PUBLIC_KEY_PROD);
    }

}
