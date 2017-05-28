package apps.mohit.shareacab.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import apps.mohit.shareacab.Activities.MapViews.UserMapsView;
import apps.mohit.shareacab.R;

/**
 * An Activity that allows the user to view their profile and make any changes to their
 * name or email address
 *
 * @author Mohit Kewalramani
 * @version 1.0
 * @since 2017-05-11
 */
public class ViewProfile extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;  // Instance of Firebase Authentication

    /**
     * Method called when the Activity is initiated. The current instance of the activity
     * is retrieved and the display fields of the user's details are all set for viewing
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        setDisplayFields();
    }

    /**
     * Helper method to set the display fields of the user's account details
     */
    private void setDisplayFields(){
        TextView headerName = (TextView) findViewById(R.id.headerNameView);
        headerName.setText("Hello " + firebaseAuth.getCurrentUser().getDisplayName() + " !");

        TextView userName = (TextView) findViewById(R.id.userProfileName);
        userName.setText(firebaseAuth.getCurrentUser().getDisplayName());

        TextView userEmail = (TextView) findViewById(R.id.userProfileEmail);
        userEmail.setText(firebaseAuth.getCurrentUser().getEmail());
    }

    /**
     * Button clicked when the user wishes to change the name associated with the
     * account. New Activity is launched to start that process
     * @param view The view from which the method is called
     */
    public void changeAccountName(View view){
        Intent intent = new Intent(this, EditAccountDetail.class);
        intent.putExtra("type", "Name");
        startActivity(intent);
    }

    /**
     * Button clicked when the user wishes to change the email associated with the
     * account. New Activity is launched to start that process
     * @param view The view from which the method is called
     */
    public void changeAccountEmail(View view){
        Intent intent = new Intent(this, EditAccountDetail.class);
        intent.putExtra("type", "Email");
        startActivity(intent);
    }

    /**
     * If the user wishes to reset their password, that request is handled separately, due
     * to the checks that need to be done. A new activity is launched to help the user
     * with that process
     * @param view The view from which the method is called
     */
    public void resetPassword(View view){
        Intent intent = new Intent(this, ResetPassword.class);
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
                Intent mapsIntent = new Intent(this, UserMapsView.class);
                startActivity(mapsIntent);
                return true;
            case R.id.viewShareRequests:
                Intent vsrIntent = new Intent(this, ViewShareRequests.class);
                startActivity(vsrIntent);
                return true;
            case R.id.viewProfile:
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
