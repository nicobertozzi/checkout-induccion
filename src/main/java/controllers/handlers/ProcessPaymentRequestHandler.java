package controllers.handlers;

import errors.ErrorCause;
import model.dto.PaymentDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import spark.Request;
import utils.RequestUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProcessPaymentRequestHandler implements  RequestHandler {

    public PaymentDTO paymentDTO;

    public PaymentDTO getPaymentDTO() {
        return paymentDTO;
    }

    private List<ErrorCause> invalidCauses;
    private HashMap<String, Object> validValues;

    private final String INVALID_PAYMENT_CODE = "200";

    private final String INVALID_DESCRIPTION_CODE = "201";
    private final String INVALID_EMAIL_CODE = "202";
    private final String INVALID_AMOUNT_CODE = "203";
    private final String INVALID_INSTALLMENTS_CODE = "204";
    private final String INVALID_ISSUER_ID_CODE = "205";
    private final String INVALID_PAYMENT_METHOD_ID_CODE = "206";
    private final String INVALID_TOKEN_CODE = "207";

    public ProcessPaymentRequestHandler(Request request) throws IOException {
        this.invalidCauses = new ArrayList<>();

        paymentDTO = RequestUtil.getBodyAsObject(request, PaymentDTO.class);

        automaticValidation();
        //manualValidation();
    }

    /**
     * Automatic validations through annotations in the DTOs
     */
    private void automaticValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        for(ConstraintViolation violation : validator.validate(paymentDTO)) {
            this.invalidCauses.add(new ErrorCause(INVALID_PAYMENT_CODE, "Invalid payment processing: " + violation.getPropertyPath() + " " + violation.getMessage()));
        }
    }

    /**
     * Manual validations
     */
    private void manualValidation() {
        if(StringUtils.isBlank(paymentDTO.getDescription())) {
            this.invalidCauses.add(new ErrorCause(INVALID_DESCRIPTION_CODE, "Invalid payment processing. Description is null or empty"));
        }
        if(StringUtils.isBlank(paymentDTO.getEmail())) {
            this.invalidCauses.add(new ErrorCause(INVALID_EMAIL_CODE, "Invalid payment processing. Payer's email is null or empty"));
        } else if(!EmailValidator.getInstance().isValid(paymentDTO.getEmail())) {
            this.invalidCauses.add(new ErrorCause(INVALID_EMAIL_CODE, "Invalid payment processing. Payer's email is not a valid email"));
        }
        if(paymentDTO.getAmount() == null) {
            this.invalidCauses.add(new ErrorCause(INVALID_AMOUNT_CODE, "Invalid payment processing. Amount is null or empty"));
        } else if(paymentDTO.getAmount() < 0.01F) {
            this.invalidCauses.add(new ErrorCause(INVALID_AMOUNT_CODE, "Invalid payment processing. Amount is not a valid number"));
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

    @Override
    public boolean isValid() {
        return this.invalidCauses.isEmpty();
    }

    public <T> T getValidParam(String parameter) {
        return (T) this.validValues.get(parameter);
    }

    @Override
    public List<ErrorCause> getInvalidCause() {
        return invalidCauses;
    }

}
