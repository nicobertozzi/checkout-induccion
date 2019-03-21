package services.providers;

import services.PaymentsService;
import services.PreferencesService;

/**
 * This class is responsible from creating services with their necessary dependencies.
 */
public class ServicesProvider {

    private ServicesProvider() {}

    public static PreferencesService getPreferencesService() {
        return new PreferencesService();
    }

    public static PaymentsService getPaymentsService() {
        return new PaymentsService();
    }

}
