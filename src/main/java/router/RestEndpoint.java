package router;

import com.mercadopago.exceptions.MPException;
import constants.Configuration;
import constants.Credentials;
import controllers.ExceptionHandler;
import spark.Spark;
import spark.servlet.SparkApplication;

public class RestEndpoint implements SparkApplication {

    @Override
    public void init() {
        try {
            Configuration.configureSpark();
            Credentials.configureCredentials();
            //Credentials.configureModeAccessToken(Credentials.Environment.SANDBOX);

            // Routes definition
            Spark.path("", new RouteGroupImpl());

            // Exceptions definition
            Spark.exception(Exception.class, ExceptionHandler::exceptionHandler);
            Spark.exception(MPException.class, ExceptionHandler::mpExceptionHandler);
        } catch (MPException e) {
            e.printStackTrace();
        }
    }

}
