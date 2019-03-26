package model.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

public class PaymentDTO {

    @Pattern(regexp = "(\\p{Alnum}|\\s|'|-)*", message = "is not a valid alphanumeric characters")
    String description;

    @NotBlank(message = "is null or empty")
    @Email(message = "is not a valid email")
    String email;

    @NotNull(message = "is null")
    @Digits(integer = 6, fraction = 2, message = "is not a valid decimal")
    @Min(value = 0, message = "is not positive")
    Float amount;

    @NotNull(message = "is null")
    @Digits(integer = 2, fraction = 0, message = "is not a valid number")
    @Min(value = 1, message = "is not a valid value")
    Integer installments;

    @NotBlank(message = "is null or empty")
    @Digits(integer = 10, fraction = 0, message = "is not a valid number")
    String issuerId;

    @NotBlank(message = "is null or empty")
    @Pattern(regexp = "(\\p{Alpha})*", message = "is not a valid alphabetic characters")
    String paymentMethodId;

    @NotBlank(message = "is null or empty")
    @Pattern(regexp = "(\\p{Alnum})*", message = "is not a valid alphanumeric characters")
    @Length(min = 32, max = 32, message = "has to have 32 characters")
    String token;

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public Float getAmount() {
        return amount;
    }

    public Integer getInstallments() {
        return installments;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public String getToken() {
        return token;
    }

}
