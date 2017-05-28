package apps.mohit.shareacab.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import apps.mohit.shareacab.Activities.MapViews.UserMapsView;
import apps.mohit.shareacab.R;
import apps.mohit.shareacab.ShareRequestListAdapter;
import apps.mohit.shareacab.Types.ShareRequestEntry;


/**
 * An Activity that allows the user to view and delete the share requests that they (from their
 * account) have posted.
 *
 * @author Mohit Kewalramani
 * @version 1.0
 * @since 2017-05-11
 */
public class ViewShareRequests extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;                          // Instance of Firebase Authentication
    private ListView userPostsListView;                         // ListView where user's requests are listed
    private ShareRequestListAdapter shareRequestListAdapter;    // ListAdapter to populate ListView
    private FirebaseDatabase firebaseDatabase;                  // Instance of Firebase Database to retrieve posts
    private DatabaseReference databaseReference;                // Reference to the Firebase Database

    /**
     * Method called once the activity is initialized. Instances are received, and the list
     * view containing the user's posts is set up with data from the Firebase database
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_share_requests);
        firebaseAuth = FirebaseAuth.getInstance();
        userPostsListView = (ListView) findViewById(R.id.requestedSharesList);
        shareRequestListAdapter = new ShareRequestListAdapter();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Toast.makeText(getApplicationContext(), "Long Click To Delete Request", Toast.LENGTH_SHORT)
                .show();
        populateListView();
        userPostsListView.setAdapter(shareRequestListAdapter);
        allowPostDeletions();
    }

    /**
     * Helper method to populate the listview on the screen, by iterating through the database
     * and selecting the records that the user has posted
     */
    private void populateListView(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    ShareRequestEntry shareRequestEntry = postSnapshot.getValue(ShareRequestEntry.class);
                    if (shareRequestEntry.getUser().equals(firebaseAuth.getCurrentUser().getEmail())){
                        shareRequestListAdapter.userEntries.put(postSnapshot.getKey(), shareRequestEntry);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Allows the listview to be long-clickable which gives the user the functionality
     * to delete any requests that were initially posted
     */
    private void allowPostDeletions(){
        userPostsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                constructAlertDialog(position);
                return true;
            }
        });
    }

    /**
     * Constructs an alert dialog for confirmation with the user, in order to proceed
     * with the deletion. If the user clicks the positive button, the post is deleted
     * from the database
     * @param position The position (index) of the list where the long click was done
     */
    private void constructAlertDialog(final int position){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Delete Request");
        alertDialog.setMessage("Are you sure you want to delete this Request?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String itemDeleteString = (String) shareRequestListAdapter.userEntries.keySet()
                        .toArray()[position];
                deleteItem(itemDeleteString);
                shareRequestListAdapter.userEntries.clear();
                populateListView();
                finish();
                startActivity(getIntent());
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.create();
        alertDialog.show();
    }

    /**
     * Helper method to delete the record from the Firebase database. The database
     * uses the given key to find the share request record and then proceeds to delete it
     * @param itemId The id (primary key) of the record in the database to delete
     */
    private void deleteItem(final String itemId){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    if (postSnapshot.getKey().equals(itemId)){
                        postSnapshot.getRef().removeValue();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                Intent mapsIntent = new Intent(this, UserMapsView.class);
                startActivity(mapsIntent);
                return true;
            case R.id.viewShareRequests:
                return true;
            case R.id.viewProfile:
                Intent vpIntent = new Intent(this, ViewProfile.class);
                startActivity(vpIntent);
                return true;
            case R.id.logout:
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
