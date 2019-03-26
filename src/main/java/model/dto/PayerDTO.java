package model.dto;

import com.mercadopago.core.annotations.validation.Numeric;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

public class PayerDTO {

    @Pattern(regexp = "(\\p{Alpha}|\\s)*", message = "is not a valid alphabetic characters")
    private String name;

    @Pattern(regexp = "(\\p{Alpha}|\\s)*", message = "is not a valid alphabetic characters")
    private String surname;

    @Email(message = "is not a valid email")
    private String email;

    @Pattern(regexp = "(\\p{Alpha})*", message = "is not a valid alphabetic characters")
    private String documentType;

    @Numeric
    private String documentNumber;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

}
