package configuration;

import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPException;

/*
Clase para mantener las credenciales...
 */
public class Credentials {

    // Checkout basico
    private static final String CLIENT_ID = "2799634616083392";
    private static final String CLIENT_SECRET = "ZWNgyLUjf3F1Z0lH3yawRNYvSD73p6OH";
    // Checkout personalizado
    private static final String ACCESS_TOKEN = "TEST-2799634616083392-031315-f88bce92de37560f864b0cc1a14289a5-401515673";

    public static final String PUBLIC_KEY = "TEST-a6b4a9b1-fbc1-4677-b4f7-7f6d6b4e9800";

    public static void configureCredentials() throws MPException {
        // Configuracion de las credenciales de un usuario
        MercadoPago.SDK.setClientId(CLIENT_ID);
        MercadoPago.SDK.setClientSecret(CLIENT_SECRET);

        System.out.println(">> User's credentials configured!");
    }

    public static void configureAccessToken() throws MPException {
        MercadoPago.SDK.setAccessToken(ACCESS_TOKEN);

        System.out.println("Access token configured!");
    }

}
