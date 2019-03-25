package controllers;

import com.mercadopago.resources.Payment;
import controllers.handlers.ProcessPaymentRequestHandler;
import controllers.handlers.RequestHandler;
import controllers.handlers.RequestHandlerFactory;
import errors.ErrorMessages;
import errors.ErrorResponse;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import services.PaymentsService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class PaymentControllerTest {

    private PaymentsService service;
    private RequestHandler requestHandler;
    private PaymentController controller;

    @Before
    public void setup() throws Exception {
        // Simulamos el Service con Mockito porque hay dependencias...
        service = Mockito.mock(PaymentsService.class);

        RequestHandlerFactory requestHandlerFactory = Mockito.mock(RequestHandlerFactory.class);

        requestHandler = Mockito.mock(ProcessPaymentRequestHandler.class);
        Mockito.when(requestHandlerFactory.getProcessPaymentHandler(Mockito.any())).thenReturn((ProcessPaymentRequestHandler) requestHandler);

        controller = new PaymentController(service, requestHandlerFactory);
    }

    @Test
    public void whenProcessPaymentThenUseService() throws Exception {
        Mockito.when(requestHandler.isValid()).thenReturn(true);

        Payment pay = Mockito.mock(Payment.class);
        Mockito.when(service.save(Mockito.any())).thenReturn(pay);
        Mockito.when(pay.getId()).thenReturn("0");
        Mockito.when(pay.getStatus()).thenReturn(Payment.Status.approved);

        Map responseMap = (Map) controller.processPayment(Mockito.mock(Request.class), Mockito.mock(Response.class));

        Assert.assertEquals(responseMap.get("payment_status"), Payment.Status.approved);
    }

    @Test
    public void whenInvalidProcessPaymentThenRespondBadRequest() throws Exception {
        Mockito.when(requestHandler.isValid()).thenReturn(false);

        ErrorResponse errorResponse = (ErrorResponse) controller.processPayment(Mockito.mock(Request.class), Mockito.mock(Response.class));

        Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, errorResponse.getHttpStatusCode());
        Assert.assertEquals(ErrorMessages.BAD_REQUEST, errorResponse.getError());
    }

}
