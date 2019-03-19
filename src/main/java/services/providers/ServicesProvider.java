package services.providers;

import services.PreferencesService;

/**
 * This class is responsible from creating services with their necessary dependencies.
 */
public class ServicesProvider {

    private ServicesProvider() {}

    public static PreferencesService getPreferencesService() {
        return new PreferencesService();
    }

}
