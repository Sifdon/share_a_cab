package apps.mohit.shareacab.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import apps.mohit.shareacab.R;


/**
 * An Activity that lets the user sign up for an account with the App
 *
 * @author Mohit Kewalramani
 * @version 1.0
 * @since 2017-05-11
 */
public class SignUp extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;  // Instance of Firebase Authentication
    private FirebaseUser firebaseUser;  // Instance of Firebase User

    /**
     * Method called when activity is initialized. Retrieves the instance of the
     * Firebase Authentication and User
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    /**
     * Called when the user has entered their details and wishes to sign up for
     * an account. This method checks to see if all fields are filled in, and then uses the
     * Firebase Platform to sign up a user.
     * @param view The view from which the method is called
     */
    public void signUp(View view){
        EditText accountName = (EditText) findViewById(R.id.signUpFullName);
        EditText emailEntry = (EditText) findViewById(R.id.signUpEmail);
        EditText password = (EditText) findViewById(R.id.signUpPassword);
        EditText passwordConfirm = (EditText) findViewById(R.id.signUpPasswordConfirm);

        final String accountNameText = accountName.getText().toString().trim();
        String emailEntryText = emailEntry.getText().toString().trim();
        String passwordText = password.getText().toString();
        String passwordConfirmText = passwordConfirm.getText().toString();

        if (accountNameText.equals("") || emailEntryText.equals("") || passwordText.equals("") ||
                passwordConfirmText.equals("")){
            Toast.makeText(
                    getApplicationContext(),
                    "Please Don't Leave Any Fields Blank",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!passwordText.equals(passwordConfirmText)){
            Toast.makeText(
                    getApplicationContext(),
                    "The passwords don't match",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(emailEntryText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Sign Up Successful",
                                    Toast.LENGTH_SHORT).show();
                            updateDisplayName(accountNameText);
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * A method to update the display name as soon as the Account is created. Prints a
     * confirmation to the screen if the update is successful
     * @param accountNameText The account name as entered by the user
     */
    private void updateDisplayName(String accountNameText){
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(accountNameText)
                .build();
        firebaseUser.updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            sendEmailConfirmation();
                        }
                    }
                });
    }

    /**
     * A helper method that sends the user a confirmation email once the sign
     * up has completed
     */
    private void sendEmailConfirmation(){
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(
                                    getApplicationContext(),
                                    "A Confirmation Email Has Been Sent. Confirm Your Email, then Log In",
                                    Toast.LENGTH_SHORT).show();
                            returnHomeScreen();
                        }
                    }
                });
    }

    /**
     * Returns the user to the home screen. Used as a helper method to show the
     * home screen once the sign up has completed
     */
    private void returnHomeScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    /**
     * Cancels the current activity and returns the user to the previous activity
     * @param view The view from which the method is called
     */
    public void cancelActivity(View view){
        this.finish();
    }
}
