package services;

import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.Identification;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;
import model.PreferenceModel;
import model.dto.ItemDTO;
import model.dto.PreferenceDTO;

public class PreferencesService {

    public static Preference save(PreferenceDTO preferenceDTO) throws MPException {
        Preference preference = new Preference();

        preference.setPayer(new Payer()
                .setName(preferenceDTO.getPayer().getName())
                .setSurname(preferenceDTO.getPayer().getSurname())
                .setEmail(preferenceDTO.getPayer().getEmail())
                .setIdentification(new Identification()
                        .setType(preferenceDTO.getPayer().getDocumentType())
                        .setNumber(preferenceDTO.getPayer().getDocumentType())));

        for(ItemDTO itemDTO : preferenceDTO.getItems()) {
            preference.appendItem(new Item()
                    .setTitle(itemDTO.getTitle())
                    .setQuantity(itemDTO.getQuantity())
                    .setCurrencyId("ARS")
                    .setUnitPrice(itemDTO.getUnitPrice()));
        }

        // lo mantengo en memoria...
        PreferenceModel.preference = preference.save();

        return preference;
    }

}
