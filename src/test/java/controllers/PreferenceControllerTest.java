package controllers;

import com.google.gson.JsonObject;
import com.mercadopago.core.MPApiResponse;
import com.mercadopago.resources.Preference;
import controllers.handlers.CreatePreferenceRequestHandler;
import controllers.handlers.RequestHandler;
import controllers.handlers.RequestHandlerFactory;
import errors.ErrorMessages;
import errors.ErrorResponse;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import services.PreferencesService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class PreferenceControllerTest {

    private PreferencesService service;
    private RequestHandler requestHandler;
    private PreferenceController controller;

    @Before
    public void setup() throws Exception {
        // Simulamos el Service con Mockito porque hay dependencias...
        service = Mockito.mock(PreferencesService.class);

        RequestHandlerFactory requestHandlerFactory = Mockito.mock(RequestHandlerFactory.class);

        requestHandler = Mockito.mock(CreatePreferenceRequestHandler.class);
        Mockito.when(requestHandlerFactory.getCreatePreferenceHandler(Mockito.any())).thenReturn((CreatePreferenceRequestHandler) requestHandler);

        controller = new PreferenceController(service, requestHandlerFactory);
    }

    @Test
    public void whenCreatePreferenceThenUseService() throws Exception {
        Mockito.when(requestHandler.isValid()).thenReturn(true);

        Preference pref = Mockito.mock(Preference.class);
        Mockito.when(service.save(Mockito.any())).thenReturn(pref);
        Mockito.when(pref.getId()).thenReturn("0");
        Mockito.when(pref.getInitPoint()).thenReturn("0000-0000-0000-0000");

        Map responseMap = (Map) controller.createPreference(Mockito.mock(Request.class), Mockito.mock(Response.class));

        Assert.assertEquals(responseMap.get("init_point"), "0000-0000-0000-0000");
    }

    @Test
    public void whenInvalidCreatePreferenceThenRespondBadRequest() throws Exception {
        Mockito.when(requestHandler.isValid()).thenReturn(false);

        ErrorResponse errorResponse = (ErrorResponse) controller.createPreference(Mockito.mock(Request.class), Mockito.mock(Response.class));

        Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, errorResponse.getHttpStatusCode());
        Assert.assertEquals(ErrorMessages.BAD_REQUEST, errorResponse.getError());
    }

}
