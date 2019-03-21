package model.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ItemDTO {

    @NotBlank(message = "is null or empty")
    @Pattern(regexp = "(\\p{Alnum}|\\s|'|-)*", message = "is not a valid alphanumeric characters")
    private String title;

    @NotBlank(message = "is null or empty")
    @Min(value = 0, message = "is not positive")
    @Digits(integer = 6, fraction = 2, message = "is not a valid decimal")
    private String unitPrice;

    @NotBlank(message = "is null or empty")
    @Min(value = 0, message = "is not positive")
    @Digits(integer = 4, fraction = 0, message = "is not a valid number")
    private String quantity;

    public String getTitle() {
        return title;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public String getQuantity() {
        return quantity;
    }

}
