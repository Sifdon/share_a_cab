package apps.mohit.shareacab.Types;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mohit Kewalramani on 19-04-2017.
 */

/**
 * This class represents the type of a Share Request Entry. Data stored in this format
 * is what forms individual records on our Firebase Database
 *
 * @author Mohit Kewalramani
 * @version 1.0
 * @since 2017-05-11
 */
public class ShareRequestEntry {

    private String user;
    private Double latitude;
    private Double longitude;
    private String destination;
    private String travelDate;
    private String travelTime;
    private String postDate;


    public ShareRequestEntry(){

    }

    public ShareRequestEntry(String user, Double latitude, Double longitude, String destination,
                             String travelDate, String travelTime){
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
        this.destination = destination;
        this.travelDate = travelDate;
        this.travelTime = travelTime;
        this.postDate = getCurrentDate();
    }

    private String getCurrentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }

    public String getUser() {
        return user;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDestination() {
        return destination;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
