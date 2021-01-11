package py.com.personal.mimundo;

import android.app.Application;

import py.com.personal.mimundo.activities.R;

/**
 * Created by Konecta on 22/12/2014.
 */
public class MyApplication extends Application {

    //private static final String PROPERTY_ID = "UA-57905956-1";
    //private static final String PROPERTY_ID = "UA-44014532-1"; //Mi mundo anterior
    private static final String PROPERTY_ID ="UA-43824803-20";
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }
}
