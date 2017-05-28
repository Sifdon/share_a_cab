package apps.mohit.shareacab.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import apps.mohit.shareacab.Activities.MapViews.UserMapsView;
import apps.mohit.shareacab.R;
import apps.mohit.shareacab.Types.ShareRequestEntry;

/**
 * This class provides the functionality for Editing the personal details associated with the
 * users' account
 *
 * @author Mohit Kewalramani
 * @version 1.0
 * @since 2017-05-11
 */
public class EditAccountDetail extends AppCompatActivity {

    private String detail;              // The account detail the user wishes to change
    private FirebaseAuth firebaseAuth;  // The instance of the Firebase Authentication

    /**
     * This method is called when the activity is initialized. The user's current
     * details are set on the Activity
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account_detail);
        firebaseAuth = FirebaseAuth.getInstance();
        TextView title = (TextView) findViewById(R.id.editAccountTitle);
        detail = getIntent().getStringExtra("type");
        title.setText(String.format("Change %s", detail));
        setAllStrings();
    }

    /**
     * A helper method to help set the strings to the Activity when initialized
     * This method sets the User's name and email to the respective TextViews
     */
    private void setAllStrings(){
        TextView currentDetailTitle = (TextView) findViewById(R.id.currentDetailTitle);
        currentDetailTitle.setText(String.format("Your %s", detail));

        TextView currentDetailString = (TextView) findViewById(R.id.currentDetailString);
        if (detail.equals("Name")){
            currentDetailString.setText(firebaseAuth.getCurrentUser().getDisplayName());
        }
        else{
            currentDetailString.setText(firebaseAuth.getCurrentUser().getEmail());
        }

        TextView newDetailTitle = (TextView) findViewById(R.id.newDetailTitle);
        newDetailTitle.setText(String.format("New %s", detail));
    }

    /**
     * This method updates the user's details onto his profile on the Firebase Platform
     * once the submit button is clicked
     * @param view The view from which this method is called
     */
    public void submitChanges(View view){
        EditText newDetail = (EditText) findViewById(R.id.newDetailString);
        String newDetailString = newDetail.getText().toString().trim();

        if (newDetailString.equals("")){
            Toast.makeText(
                    getApplicationContext(),
                    "Please Enter A Valid Detail",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (detail.equals("Name")){
            updateUserName(newDetailString);
        }
        else if (detail.equals("Email")) {
            updateEmailAcrossDatabase(firebaseAuth.getCurrentUser().getEmail(), newDetailString);
            updateUserEmail(newDetailString);
        }
    }

    /**
     * This is a helper method to update the user's name if changed. This method
     * calls the Firebase Update Profile method and shows a success method if the update
     * is complete
     * @param newName The name to be updated on the Firebase profile
     */
    private void updateUserName(String newName){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();
        firebaseUser.updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Your Name Has Been Updated",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            showHomeScreen();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Please Try Again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * This method updates all sharing posts on the Firebase Database that contains the
     * user's old email to match the new updated email that has been provided
     *
     * @param oldEmail The user's old email
     * @param newEmail The user's new email
     */
    private void updateEmailAcrossDatabase(final String oldEmail, final String newEmail){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    ShareRequestEntry shareRequestEntry =
                            postSnapshot.getValue(ShareRequestEntry.class);
                    if (shareRequestEntry.getUser().equals(oldEmail)){
                        shareRequestEntry.setUser(newEmail);
                        databaseReference.child(postSnapshot.getKey()).setValue(shareRequestEntry);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Update's the user's email on the Firebase authentication table to match the new
     * provided email
     * @param newEmail The user's new email
     */
    private void updateUserEmail(String newEmail){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        firebaseUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Your Email Has Been Updated",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    showHomeScreen();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please Try Again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This method is called once the user's email has been successfully updated.
     * It return's the user back to the home screen
     */
    private void showHomeScreen(){
        Intent intent = new Intent(this, UserMapsView.class);
        startActivity(intent);
    }

    /**
     * This method cancels the current activity and returns the view to the previous
     * Activity
     * @param view The view from which the method is called
     */
    public void cancelChanges(View view){
        this.finish();
    }
}
