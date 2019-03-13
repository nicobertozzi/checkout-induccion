package configuration;

import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPException;
import spark.Filter;
import spark.Spark;

/*
Clase para la configuracion
 */
public class Configuration {

    public static void configureSpark() {
        // Cambiamos a un puerto mas amigable...
        Spark.port(9999);

        // Al hacer el fetch desde React para obtener los datos, se bloqueaba el acceso por CORS policy (por eso se devuelve en la cabecera)
        Spark.after((Filter) (req, resp) -> {
            resp.header("Access-Control-Allow-Origin", "*");
            resp.header("Access-Control-Allow-Methods", "GET");
        });

        System.out.println(">> Spark's configuration done!");
    }

}
