package Utils;

import model.dto.PreferenceDTO;
import org.junit.Test;
import utils.Json;

import java.io.IOException;

public class JsonTest {

    @Test
    public void convertToJSON() throws IOException {
        String a1 = "payer.name=Nico&payer.surname=Bertozzi&payer.email=test_user_19562067@testuser.com&payer.document_type=DNI&payer.document_number=32978330&items[0].title=Algo codiciado&items[0].unit_price=2000&items[0].quantity=8&items[1].title=algi&items[1].unit_price=3342&items[1].quantity=3\n";

        PreferenceDTO s = Json.INSTANCE.convertToObject(a1, PreferenceDTO.class);
        System.out.println(s);
    }

}
