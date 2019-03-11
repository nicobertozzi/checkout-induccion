import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPException;

/*
Clase para mantener las credenciales...
 */
class Credentials {

    // Checkout basico
    static final String SELLER_CLIENT_ID = "2799634616083392";
    static final String SELLER_CLIENT_SECRET = "ZWNgyLUjf3F1Z0lH3yawRNYvSD73p6OH";
    // Checkout personalizado
    private static final String SELLER_ACCESS_TOKEN = "TEST-2799634616083392-030613-37d6c9f41b71f2b5dc2170a70cf3d211-401515673";

    // PAYER
    static final String PAYER_NICK = "TESTMKRSPLCN";
    static final String PAYER_EMAIL = "test_user_19562067@testuser.com";

    /*
    Configuracion para el Access Token...
     */
    static void configureAccessToken() throws MPException {
        MercadoPago.SDK.setAccessToken(SELLER_ACCESS_TOKEN);
    }

}
