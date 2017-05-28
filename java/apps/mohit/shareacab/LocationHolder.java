package apps.mohit.shareacab;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mohit Kewalramani on 20-04-2017.
 */

/**
 * A singleton class that makes use of the functionality to hold the selected location
 * when a user is creating a share request. The value at this request is reset at the start
 * of every share request addition
 *
 * @author Mohit Kewalramani
 * @version 1.0
 * @since 2017-05-11
 */
public class LocationHolder {

    private static final LocationHolder ourInstance = new LocationHolder();
    public LatLng selectedLocation;

    public static LocationHolder getInstance() {
        return ourInstance;
    }

    private LocationHolder() {
    }
}
