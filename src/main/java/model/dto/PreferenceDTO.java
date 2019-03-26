package model.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class PreferenceDTO {

    @Valid
    private PayerDTO payer;

    @Valid
    @NotNull(message = "is null")
    @Size(min = 1, message = "is empty")
    private List<ItemDTO> items;

    public PayerDTO getPayer() {
        return payer;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

}
