package constants;

import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPException;

/*
Clase para mantener las credenciales...
 */
public class Credentials {

    // Web-Checkout
    private static final String CLIENT_ID = "545545474996086";
    private static final String CLIENT_SECRET = "GaQGhYj58qsRWhp9CSQgsuyV2Mk3ONVg";

    // API o Checkout personalizado
    private static final String ACCESS_TOKEN_SANDBOX = "TEST-545545474996086-010315-04b0f100ed5aeb57304e2926cebde76a__LB_LC__-239656545";
    public static final String PUBLIC_KEY_SANDBOX = "TEST-b46c6d4a-56c4-4439-8ebd-4ca2cfa4cd1e";
    private static final String ACCESS_TOKEN_PROD = "APP_USR-545545474996086-010315-d45aa2d8f8bb1ad9a75cec582842e235__LA_LB__-239656545";
    public static final String PUBLIC_KEY_PROD = "APP_USR-e27b6c01-4af2-4850-baa9-c11d8587d365";

    /*public enum Environment {
        SANDBOX, PRODUCCION
    }*/

    public static void configureCredentials() throws MPException {
        System.out.println(" >> Configuring user's credentials...");
        MercadoPago.SDK.setClientId(CLIENT_ID);
        MercadoPago.SDK.setClientSecret(CLIENT_SECRET);

        MercadoPago.SDK.setAccessToken(ACCESS_TOKEN_SANDBOX);
        System.out.println(" >> User's credentials configured!");
    }

    /*public static void configureModeAccessToken(Environment pModo) throws MPException {
        System.out.println(" >> Configuring '" + pModo.name() + "' access token...");
        switch (pModo) {
            case SANDBOX: {
                MercadoPago.SDK.setAccessToken(ACCESS_TOKEN_SANDBOX);
                break;
            }
            case PRODUCCION: {
                MercadoPago.SDK.setAccessToken(ACCESS_TOKEN_PROD);
                break;
            }
        }
        System.out.println(" >> Access token configured!");
    }*/

}
