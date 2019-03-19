package model.dto;

import java.util.List;

public class PreferenceDTO {

    private PayerDTO payer;
    private List<ItemDTO> items;

    public PayerDTO getPayer() {
        return payer;
    }

    public void setPayer(PayerDTO payer) {
        this.payer = payer;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

}
