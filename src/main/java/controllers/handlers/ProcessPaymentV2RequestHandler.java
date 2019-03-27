package controllers.handlers;

import errors.ErrorCause;
import model.dto.PaymentDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import spark.Request;
import utils.RequestUtil;
import java.io.IOException;
import java.util.ArrayList;

public class ProcessPaymentV2RequestHandler extends ProcessPaymentRequestHandler {

    public ProcessPaymentV2RequestHandler(Request request) throws IOException {
        this.invalidCauses = new ArrayList<>();

        paymentDTO = RequestUtil.getBodyAsObject(request, PaymentDTO.class);

        manualValidation();
    }

    /**
     * Manual validations
     */
    private void manualValidation() {
        if(paymentDTO.getEmail() != null
            && !EmailValidator.getInstance().isValid(paymentDTO.getEmail())) {
            this.invalidCauses.add(new ErrorCause(INVALID_EMAIL_CODE, "Invalid payment processing. Payer's email is not a valid email"));
        }
        if(paymentDTO.getInstallments() == null) {
            this.invalidCauses.add(new ErrorCause(INVALID_INSTALLMENTS_CODE, "Invalid payment processing. Installments are null or empty"));
        } else if(paymentDTO.getInstallments() < 1) {
            this.invalidCauses.add(new ErrorCause(INVALID_INSTALLMENTS_CODE, "Invalid payment processing. Installments is not a valid number"));
        }
        if(StringUtils.isBlank(paymentDTO.getIssuerId())) {
            this.invalidCauses.add(new ErrorCause(INVALID_ISSUER_ID_CODE, "Invalid payment processing. Issuer ID is null or empty"));
        }
        if(StringUtils.isBlank(paymentDTO.getPaymentMethodId())) {
            this.invalidCauses.add(new ErrorCause(INVALID_PAYMENT_METHOD_ID_CODE, "Invalid payment processing. Payment method ID is null or empty"));
        } else if(!StringUtils.isAlpha(paymentDTO.getPaymentMethodId())) {
            this.invalidCauses.add(new ErrorCause(INVALID_PAYMENT_METHOD_ID_CODE, "Invalid payment processing. Payment method ID does not contain only letters"));
        }
        if(StringUtils.isBlank(paymentDTO.getToken())) {
            this.invalidCauses.add(new ErrorCause(INVALID_TOKEN_CODE, "Invalid payment processing. Token is null or empty"));
        }
    }

}
