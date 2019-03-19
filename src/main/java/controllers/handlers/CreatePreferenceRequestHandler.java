package controllers.handlers;

import errors.ErrorCause;
import model.dto.ItemDTO;
import model.dto.PreferenceDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import spark.Request;
import utils.JsonUtils;
import utils.RequestUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePreferenceRequestHandler implements  RequestHandler {

    private PreferenceDTO preferenceDTO;

    public PreferenceDTO getPreferenceDTO() {
        return preferenceDTO;
    }

    private List<ErrorCause> invalidCauses;

    private final String INVALID_PAYER_CODE = "100";
    private final String INVALID_NAME_PAYER_CODE = "101";
    private final String INVALID_SURNAME_PAYER_CODE = "102";
    private final String INVALID_EMAIL_PAYER_CODE = "103";
    private final String INVALID_DOCUMENT_PAYER_CODE = "104";

    private final String INVALID_ITEMS_CODE = "110";
    private final String INVALID_TITLE_ITEMS_CODE = "111";
    private final String INVALID_UNITPRICE_ITEMS_CODE = "112";
    private final String INVALID_QUANTITY_ITEMS_CODE = "113";

    public CreatePreferenceRequestHandler(Request request) {
        this.invalidCauses = new ArrayList<>();

        System.out.println(request.body());
        preferenceDTO = JsonUtils.fromJson(RequestUtil.getBodyAsJSON(request), PreferenceDTO.class);

        if(preferenceDTO.getPayer() != null) {
            if(StringUtils.isBlank(preferenceDTO.getPayer().getName())) {
                this.invalidCauses.add(new ErrorCause(INVALID_NAME_PAYER_CODE, "Invalid Preference configuration. Payer's name is null or empty"));
            } else {
                if(!StringUtils.isAlphaSpace(preferenceDTO.getPayer().getName())) {
                    this.invalidCauses.add(new ErrorCause(INVALID_NAME_PAYER_CODE, "Invalid Preference configuration. Payer's name does not contain only Unicode letters"));
                }
            }
            if(StringUtils.isBlank(preferenceDTO.getPayer().getSurname())) {
                this.invalidCauses.add(new ErrorCause(INVALID_SURNAME_PAYER_CODE, "Invalid Preference configuration. Payer's surname is null or empty"));
            } else {
                if(!StringUtils.isAlphaSpace(preferenceDTO.getPayer().getSurname())) {
                    this.invalidCauses.add(new ErrorCause(INVALID_SURNAME_PAYER_CODE, "Invalid Preference configuration. Payer's surname does not contain only letters"));
                }
            }
            if(StringUtils.isBlank(preferenceDTO.getPayer().getEmail())) {
                this.invalidCauses.add(new ErrorCause(INVALID_EMAIL_PAYER_CODE, "Invalid Preference configuration. Payer's email is null or empty"));
            } else {
                if(!EmailValidator.getInstance().isValid(preferenceDTO.getPayer().getEmail())) {
                    this.invalidCauses.add(new ErrorCause(INVALID_EMAIL_PAYER_CODE, "Invalid Preference configuration. Payer's email is not a valid email"));
                }
            }
            if(StringUtils.isBlank(preferenceDTO.getPayer().getDocumentType())) {
                this.invalidCauses.add(new ErrorCause(INVALID_DOCUMENT_PAYER_CODE, "Invalid Preference configuration. Document type is null or empty"));
            } else {
                if(!StringUtils.isAlphaSpace(preferenceDTO.getPayer().getDocumentType())) {
                    this.invalidCauses.add(new ErrorCause(INVALID_DOCUMENT_PAYER_CODE, "Invalid Preference configuration. Document type does not contain only letters"));
                }
            }
            if(StringUtils.isBlank(preferenceDTO.getPayer().getDocumentNumber())) {
                this.invalidCauses.add(new ErrorCause(INVALID_DOCUMENT_PAYER_CODE, "Invalid Preference configuration. Document number is null or empty"));
            } else {
                if(!StringUtils.isNumeric(preferenceDTO.getPayer().getDocumentNumber())) {
                    this.invalidCauses.add(new ErrorCause(INVALID_DOCUMENT_PAYER_CODE, "Invalid Preference configuration. Document number does not contain only numbers"));
                }
            }
        } else {
            this.invalidCauses.add(new ErrorCause(INVALID_PAYER_CODE, "Invalid Preference configuration. Payer is null"));
        }
        if(preferenceDTO.getItems() != null && !preferenceDTO.getItems().isEmpty()) {
            boolean someInvalid = false;
            for(ItemDTO item : preferenceDTO.getItems()) {
                if(StringUtils.isBlank(item.getTitle())) {
                    this.invalidCauses.add(new ErrorCause(INVALID_TITLE_ITEMS_CODE, "Invalid Preference configuration. Title is null or empty"));
                    someInvalid = true;
                }
                try {
                    Double.parseDouble(item.getUnitPrice());
                } catch(NumberFormatException e) {
                    someInvalid = true;
                    this.invalidCauses.add(new ErrorCause(INVALID_UNITPRICE_ITEMS_CODE, "Invalid Preference configuration. Unit price is not a valid number"));
                }
                try {
                    Double.parseDouble(item.getQuantity());
                } catch(NumberFormatException e) {
                    someInvalid = true;
                    this.invalidCauses.add(new ErrorCause(INVALID_QUANTITY_ITEMS_CODE, "Invalid Preference configuration. Quantity is not a valid number"));
                }
                // si validamos que alguno ya es invalido, cortamos la iteracion...
                if(someInvalid) break;
            }
        } else {
            this.invalidCauses.add(new ErrorCause(INVALID_ITEMS_CODE, "Invalid Preference configuration. Items is null or empty"));
        }

        /*if (this.invalidCauses.isEmpty()) {
            this.validValues.put(CHECKOUT_VERSION_PARAMETER, checkoutVersion);
            this.validValues.put(FORCE_PARAMETER, force);
            this.validValues.put(ID_PARAMETER, id);
        }*/
    }

    @Override
    public <T> T getValidParam(String parameter) {
        return null;
    }

    @Override
    public boolean isValid() {
        return this.invalidCauses.isEmpty();
    }

    /*public <T> T getValidParam(String parameter) {
        return (T) this.validValues.get(parameter);
    }*/

    @Override
    public List<ErrorCause> getInvalidCause() {
        return invalidCauses;
    }

}
