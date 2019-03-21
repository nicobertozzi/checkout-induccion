package controllers;

import controllers.handlers.CreatePreferenceRequestHandler;
import controllers.handlers.ProcessPaymentRequestHandler;
import controllers.handlers.RequestHandler;
import controllers.handlers.RequestHandlerFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import services.PaymentsService;
import services.PreferencesService;
import spark.Request;
import spark.Response;

public class EndpointControllerTest {

    private PreferencesService preferencesService;
    private CreatePreferenceRequestHandler reqHandCreatePref;

    private PaymentsService paymentsService;
    private ProcessPaymentRequestHandler reqHandProcPay;

    private EndpointController controller;

    @Before
    public void setup() {
        // Mock basic valid request case
        preferencesService = Mockito.mock(PreferencesService.class);
        paymentsService = Mockito.mock(PaymentsService.class);

        RequestHandlerFactory requestHandlerFactory = Mockito.mock(RequestHandlerFactory.class);

        reqHandCreatePref = Mockito.mock(CreatePreferenceRequestHandler.class);
        Mockito.when(requestHandlerFactory.getCreatePreferenceHandler(Mockito.any())).thenReturn(reqHandCreatePref);

        reqHandProcPay = Mockito.mock(ProcessPaymentRequestHandler.class);
        Mockito.when(requestHandlerFactory.getProcessPaymentHandler(Mockito.any())).thenReturn(reqHandProcPay);

        controller = new EndpointController(preferencesService, paymentsService, requestHandlerFactory);
    }

    @Test
    public void whenCreatePreferenceThenUseService() throws Exception {
        Mockito.when(reqHandCreatePref.isValid()).thenReturn(true);
        //Mockito.when(reqHandCreatePref.getValidParam(ID_PARAMETER)).thenReturn(Mockito.anyString());
        //Mockito.when(reqHandCreatePref.getValidParam(CHECKOUT_VERSION_PARAMETER)).thenReturn(VERSION_2);
        //Mockito.when(reqHandCreatePref.getValidParam(FORCE_PARAMETER)).thenReturn(false);

        controller.createPreference(Mockito.mock(Request.class), Mockito.mock(Response.class));

        Mockito.verify(preferencesService, Mockito.atLeastOnce()).save(Mockito.any());
    }

}
