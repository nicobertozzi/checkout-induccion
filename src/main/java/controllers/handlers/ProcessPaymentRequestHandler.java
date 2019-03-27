package controllers.handlers;

import errors.ErrorCause;
import model.dto.PaymentDTO;

import java.util.List;

public abstract class ProcessPaymentRequestHandler implements RequestHandler {

    protected PaymentDTO paymentDTO;

    public PaymentDTO getPaymentDTO() {
        return paymentDTO;
    }

    protected List<ErrorCause> invalidCauses;

    protected final String INVALID_PAYMENT_CODE = "200";

    protected final String INVALID_DESCRIPTION_CODE = "201";
    protected final String INVALID_EMAIL_CODE = "202";
    protected final String INVALID_AMOUNT_CODE = "203";
    protected final String INVALID_INSTALLMENTS_CODE = "204";
    protected final String INVALID_ISSUER_ID_CODE = "205";
    protected final String INVALID_PAYMENT_METHOD_ID_CODE = "206";
    protected final String INVALID_TOKEN_CODE = "207";

    @Override
    public boolean isValid() {
        return this.invalidCauses.isEmpty();
    }

    @Override
    public List<ErrorCause> getInvalidCause() {
        return invalidCauses;
    }

}
