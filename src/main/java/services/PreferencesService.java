package services;

import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Identification;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;
import model.PreferenceModel;
import model.dto.ItemDTO;
import model.dto.PreferenceDTO;
import org.apache.log4j.Logger;

import java.util.Map;

public class PreferencesService {

    private static Logger log = Logger.getLogger(PreferencesService.class);

    /*
    Creamos una Preference a partir de un Payer e Items...
     */
    public static Preference save(PreferenceDTO preferenceDTO) throws MPException {
        log.info("Save Preference");

        Preference preference = new Preference();

        // Creamos un Payer de Test...
        preference.setPayer(new Payer()
                .setName(preferenceDTO.getPayer().getName())
                .setSurname(preferenceDTO.getPayer().getSurname())
                .setEmail(preferenceDTO.getPayer().getEmail())
                .setIdentification(new Identification()
                        .setType(preferenceDTO.getPayer().getDocumentType())
                        .setNumber(preferenceDTO.getPayer().getDocumentType())));

        // Creamos un item para la compra y lo añadimos...
        for(ItemDTO itemDTO : preferenceDTO.getItems()) {
            preference.appendItem(new Item()
                    .setTitle(itemDTO.getTitle())
                    .setQuantity(Integer.parseInt(itemDTO.getQuantity()))
                    .setCurrencyId("ARS")
                    .setUnitPrice(Float.parseFloat(itemDTO.getUnitPrice())));
        }

        preference.setBackUrls(new BackUrls(
                "localhost:9999/punto1",
                "localhost:9999/",
                "localhost:9999/punto5"));

        preference.save();

        // Si esta bien, recien ahi reemplazamos...
        PreferenceModel.preference = preference;

        return preference;
    }

}
