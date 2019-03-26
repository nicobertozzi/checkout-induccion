package controllers;

import com.mercadopago.core.MPApiResponse;
import com.mercadopago.resources.Preference;
import controllers.handlers.CreatePreferenceRequestHandler;
import controllers.handlers.RequestHandlerFactory;
import errors.ErrorMessages;
import errors.ErrorResponse;
import org.apache.http.HttpStatus;
import services.PreferencesService;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class PreferenceController {

    private final PreferencesService preferencesService;
    private final RequestHandlerFactory requestHandlerFactory;

    public PreferenceController(PreferencesService preferencesService, RequestHandlerFactory requestHandlerFactory) {
        this.preferencesService = preferencesService;
        this.requestHandlerFactory = requestHandlerFactory;
    }

    /**
     *
     *
     * @param request Request
     * @param response Response
     * @return Map with response data or an ErrorResponse if the validation fails
     * @throws Exception
     */
    public Object createPreference(Request request, Response response) throws Exception {
        response.header("Content-Type", "application/json");

        CreatePreferenceRequestHandler requestHandler = requestHandlerFactory.getCreatePreferenceHandler(request);

        if(!requestHandler.isValid()) {
            response.status(HttpStatus.SC_BAD_REQUEST);
            return new ErrorResponse(HttpStatus.SC_BAD_REQUEST, ErrorMessages.BAD_REQUEST, ErrorMessages.INVALID_PREFERENCE_CREATION, requestHandler.getInvalidCause());
        }

        Preference locPreference = preferencesService.save(requestHandler.getPreferenceDTO());
        if(locPreference.getId() == null) {
            MPApiResponse mpa = locPreference.getLastApiResponse();
            response.status(mpa.getStatusCode());

            return mpa.getJsonElementResponse();
        }
        response.status(HttpStatus.SC_OK);
        //response.header setearle las cabeceras de JSON (ver en el repo)

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("init_point", locPreference.getInitPoint());

        return responseMap;
    }

}
