package apps.mohit.shareacab.Activities.MapViews;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import apps.mohit.shareacab.LocationHolder;
import apps.mohit.shareacab.R;
import apps.mohit.shareacab.Types.ShareRequestEntry;

/**
 * An Activity that lets the user choose their cab's origin request for their request. This
 * pulls up an instance of google maps, and allows the user to long click a position and use
 * the marker as the origin point for the share
 *
 * @author Mohit Kewalramani
 * @version 1.0
 * @since 2017-05-11
 */
public class SelectOriginMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap; // The instance from which we access the Google Maps Display

    /**
     * Method called once the Activity is initialized
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_origin_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Method called once the map has loaded and is ready. Sets the global variable
     * as the updated google maps, and loads all markers currently on the database
     * for the user to see all the current share requests before making their own selection
     * @param googleMap The ready google maps view
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        loadAllMarkers();
        addUserSelectedMarkerToMap();
    }

    /**
     * Helper method to load all the markers to the current instance of the MapView.
     * Iterates through the database and loads them all onto the marker for the user to see
     */
    private void loadAllMarkers(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    ShareRequestEntry shareRequestEntry = postSnapshot.getValue(ShareRequestEntry.class);
                    googleMap.addMarker(
                            new MarkerOptions()
                                    .position(new LatLng(shareRequestEntry.getLatitude(),
                                            shareRequestEntry.getLongitude()))
                                    .title(shareRequestEntry.getDestination())
                                    .snippet(String.format("Date : %s   Time : %s",
                                            shareRequestEntry.getTravelDate(),
                                            shareRequestEntry.getTravelTime())));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Allows the user to long click the mapview in order to add their preferred location
     * for the share origin. Helper method is called once the MapView is ready
     */
    private void addUserSelectedMarkerToMap(){
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLng));
                LocationHolder.getInstance().selectedLocation = latLng;
                Button selectButton = (Button) findViewById(R.id.selectLocationFromMap);
                selectButton.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Closes the activity so the user can proceed with adding the share request. Relays
     * the selected location through a singleton class which has stored the data
     * @param view The view from which this method is called
     */
    public void relayLocationToRequest(View view){
        this.finish();
    }

}
