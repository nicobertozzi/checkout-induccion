package model.dto;

import com.mercadopago.core.annotations.validation.Numeric;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class PayerDTO {

    @NotBlank(message = "is null or empty")
    @Pattern(regexp = "(\\p{Alpha}|\\s)*", message = "is not a valid alphabetic characters")
    private String name;

    @NotBlank(message = "is null or empty")
    @Pattern(regexp = "(\\p{Alpha}|\\s)*", message = "is not a valid alphabetic characters")
    private String surname;

    @NotBlank(message = "is null or empty")
    @Email(message = "is not a valid email")
    private String email;

    @NotBlank(message = "is null or empty")
    @Pattern(regexp = "(\\p{Alpha})*", message = "is not a valid alphabetic characters")
    private String documentType;

    @NotBlank(message = "is null or empty")
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
