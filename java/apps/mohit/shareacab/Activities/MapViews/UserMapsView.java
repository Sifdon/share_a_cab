package apps.mohit.shareacab.Activities.MapViews;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import apps.mohit.shareacab.Activities.MainActivity;
import apps.mohit.shareacab.Activities.ViewProfile;
import apps.mohit.shareacab.Activities.ViewShareRequests;
import apps.mohit.shareacab.R;
import apps.mohit.shareacab.Activities.RequestShare;
import apps.mohit.shareacab.Types.ShareRequestEntry;

import static apps.mohit.shareacab.R.id.map;


/**
 * The first activity the user sees after logging in. The activity shows the MapView that
 * contains all share requests currently in the database
 *
 * @author Mohit Kewalramani
 * @version 1.0
 * @since 2017-05-11
 */
public class UserMapsView extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap; // The instance from which we access the Google Maps Display

    /**
     * Method called when activity is initialized
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_maps_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Method called when MapView is ready and loaded. Sets up a click listener so the user
     * can view the poster's details when a marker is clicked
     * @param googleMap The ready google maps view
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        enableLocation();
        loadAllMarkers();
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                TextView shareUserEmail = (TextView) findViewById(R.id.shareUserEmail);
                shareUserEmail.setText(marker.getSnippet());
                return true;
            }
        });
    }

    /**
     * Method called when activity resumes from idle state. Markers are re-loaded from the
     * database to reflect the most up to date posts.
     */
    @Override
    protected void onResume() {
        loadAllMarkers();
        super.onResume();
    }

    /**
     * Method called when activity is resumed after being in another activity. Markers are
     * re-loaded from the database to reflect the most up to date posts.
     */
    @Override
    protected void onRestart() {
        loadAllMarkers();
        super.onRestart();
    }

    /**
     * Allows the user to let the app access their current location to allow map access to
     * their current location
     */
    private void enableLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    /**
     * Loads all markers from the Firebase Database into the MapView. Users can browse
     * through all currently posted markers and decide as to whether or not they want to
     * add a share request
     */
    public void loadAllMarkers(){
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
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
                                    .snippet(String.format("Date : %s   Time : %s  \nContact : %s",
                                            shareRequestEntry.getTravelDate(),
                                            shareRequestEntry.getTravelTime(),
                                            shareRequestEntry.getUser())));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * If the user wishes to add a share request, this method will be called once the
     * button is clicked
     * @param view The view from which the method is called
     */
    public void openAddRequest(View view){
        Intent intent = new Intent(this, RequestShare.class);
        startActivity(intent);
    }

    /**
     * A method that creates and inflates the menu options for the App
     * @param menu The menu interface for the App
     * @return boolean True if inflating the menu layout was successful
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.screen_menu, menu);
        return true;
    }

    /**
     * Carries out the designed action based on which menu item is selected
     * @param item The item on the menu clicked
     * @return boolean True if the actions of the clicked item have been executed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.viewMaps:
                return true;
            case R.id.viewShareRequests:
                Intent vsrIntent = new Intent(this, ViewShareRequests.class);
                startActivity(vsrIntent);
                return true;
            case R.id.viewProfile:
                Intent vpIntent = new Intent(this, ViewProfile.class);
                startActivity(vpIntent);
                return true;
            case R.id.logout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent signOut = new Intent(this, MainActivity.class);
                startActivity(signOut);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}