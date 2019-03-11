import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPException;
import spark.Filter;
import spark.Spark;

/*
Clase para la configuracion de Spark
 */
class Configuration {

    static void configureSpark() {
        try {
            // Cambiamos a un puerto mas amigable...
            Spark.port(9999);

            // Al hacer el fetch desde React para obtener los datos, se bloqueaba el acceso por CORS policy (por eso se devuelve en la cabecera)
            Spark.after((Filter) (req, resp) -> {
                resp.header("Access-Control-Allow-Origin", "*");
                resp.header("Access-Control-Allow-Methods", "GET");
            });

            // Configuracion de las credenciales de un usuario
            MercadoPago.SDK.setClientId(Credentials.SELLER_CLIENT_ID);
            MercadoPago.SDK.setClientSecret(Credentials.SELLER_CLIENT_SECRET);

            System.out.println(">> Configuration done!");
        } catch (MPException mpe) {
            mpe.printStackTrace();
        }
    }

}
