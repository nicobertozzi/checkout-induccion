package model.dto;

import javax.validation.constraints.*;

public class ItemDTO {

    @NotBlank(message = "is null or empty")
    @Pattern(regexp = "(\\p{Alnum}|\\s|'|-)*", message = "is not a valid alphanumeric characters")
    private String title;

    @NotNull(message = "is null")
    @Min(value = 0, message = "is not positive")
    @Digits(integer = 6, fraction = 2, message = "is not a valid decimal")
    private Float unitPrice;

    @NotNull(message = "is null")
    @Min(value = 1, message = "is not a valid value")
    private Integer quantity;

    public String getTitle() {
        return title;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
