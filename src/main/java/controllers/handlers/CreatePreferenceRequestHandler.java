package controllers.handlers;

import errors.ErrorCause;
import model.dto.ItemDTO;
import model.dto.PreferenceDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import spark.Request;
import utils.RequestUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreatePreferenceRequestHandler implements  RequestHandler {

    private PreferenceDTO preferenceDTO;

    public PreferenceDTO getPreferenceDTO() {
        return preferenceDTO;
    }

    private List<ErrorCause> invalidCauses;
    private HashMap<String, Object> validValues;

    private final String INVALID_PREFERENCE_CODE = "100";

    private final String INVALID_PAYER_CODE = "110";
    private final String INVALID_NAME_PAYER_CODE = "111";
    private final String INVALID_SURNAME_PAYER_CODE = "112";
    private final String INVALID_EMAIL_PAYER_CODE = "113";
    private final String INVALID_DOCUMENT_PAYER_CODE = "114";

    private final String INVALID_ITEMS_CODE = "120";
    private final String INVALID_TITLE_ITEMS_CODE = "121";
    private final String INVALID_UNITPRICE_ITEMS_CODE = "122";
    private final String INVALID_QUANTITY_ITEMS_CODE = "123";

    public CreatePreferenceRequestHandler(Request request) {
        this.invalidCauses = new ArrayList<>();

        preferenceDTO = RequestUtil.getBodyAsObject(request, PreferenceDTO.class);

        automaticValidation();
        //manualValidation();
    }

    private void automaticValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        for(ConstraintViolation violation : validator.validate(preferenceDTO)) {
            this.invalidCauses.add(new ErrorCause(INVALID_PREFERENCE_CODE, "Invalid preference configuration: " + violation.getPropertyPath() + " " + violation.getMessage()));
        }
    }

    private void manualValidation() {
        if(preferenceDTO.getPayer() != null) {
            if(StringUtils.isBlank(preferenceDTO.getPayer().getName())) {
                this.invalidCauses.add(new ErrorCause(INVALID_NAME_PAYER_CODE, "Invalid preference configuration. Payer's name is null or empty"));
            } else {
                if(!StringUtils.isAlphaSpace(preferenceDTO.getPayer().getName())) {
                    this.invalidCauses.add(new ErrorCause(INVALID_NAME_PAYER_CODE, "Invalid preference configuration. Payer's name does not contain only Unicode letters"));
                }
            }
            if(StringUtils.isBlank(preferenceDTO.getPayer().getSurname())) {
                this.invalidCauses.add(new ErrorCause(INVALID_SURNAME_PAYER_CODE, "Invalid preference configuration. Payer's surname is null or empty"));
            } else {
                if(!StringUtils.isAlphaSpace(preferenceDTO.getPayer().getSurname())) {
                    this.invalidCauses.add(new ErrorCause(INVALID_SURNAME_PAYER_CODE, "Invalid preference configuration. Payer's surname does not contain only letters"));
                }
            }
            if(StringUtils.isBlank(preferenceDTO.getPayer().getEmail())) {
                this.invalidCauses.add(new ErrorCause(INVALID_EMAIL_PAYER_CODE, "Invalid preference configuration. Payer's email is null or empty"));
            } else {
                if(!EmailValidator.getInstance().isValid(preferenceDTO.getPayer().getEmail())) {
                    this.invalidCauses.add(new ErrorCause(INVALID_EMAIL_PAYER_CODE, "Invalid preference configuration. Payer's email is not a valid email"));
                }
            }
            if(StringUtils.isBlank(preferenceDTO.getPayer().getDocumentType())) {
                this.invalidCauses.add(new ErrorCause(INVALID_DOCUMENT_PAYER_CODE, "Invalid preference configuration. Document type is null or empty"));
            } else {
                if(!StringUtils.isAlphaSpace(preferenceDTO.getPayer().getDocumentType())) {
                    this.invalidCauses.add(new ErrorCause(INVALID_DOCUMENT_PAYER_CODE, "Invalid preference configuration. Document type does not contain only letters"));
                }
            }
            if(StringUtils.isBlank(preferenceDTO.getPayer().getDocumentNumber())) {
                this.invalidCauses.add(new ErrorCause(INVALID_DOCUMENT_PAYER_CODE, "Invalid preference configuration. Document number is null or empty"));
            } else {
                if(!StringUtils.isNumeric(preferenceDTO.getPayer().getDocumentNumber())) {
                    this.invalidCauses.add(new ErrorCause(INVALID_DOCUMENT_PAYER_CODE, "Invalid preference configuration. Document number does not contain only numbers"));
                }
            }
        } else {
            this.invalidCauses.add(new ErrorCause(INVALID_PAYER_CODE, "Invalid preference configuration. Payer is null"));
        }
        if(preferenceDTO.getItems() != null && !preferenceDTO.getItems().isEmpty()) {
            boolean someInvalid = false;
            for(ItemDTO item : preferenceDTO.getItems()) {
                if(StringUtils.isBlank(item.getTitle())) {
                    this.invalidCauses.add(new ErrorCause(INVALID_TITLE_ITEMS_CODE, "Invalid preference configuration. Title is null or empty"));
                    someInvalid = true;
                }
                try {
                    Double.parseDouble(item.getUnitPrice());
                } catch(NumberFormatException e) {
                    someInvalid = true;
                    this.invalidCauses.add(new ErrorCause(INVALID_UNITPRICE_ITEMS_CODE, "Invalid preference configuration. Unit price is not a valid number"));
                }
                try {
                    Double.parseDouble(item.getQuantity());
                } catch(NumberFormatException e) {
                    someInvalid = true;
                    this.invalidCauses.add(new ErrorCause(INVALID_QUANTITY_ITEMS_CODE, "Invalid preference configuration. Quantity is not a valid number"));
                }
                // si validamos que alguno ya es invalido, cortamos la iteracion...
                if(someInvalid) break;
            }
        } else {
            this.invalidCauses.add(new ErrorCause(INVALID_ITEMS_CODE, "Invalid preference configuration. Items are null or empty"));
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
