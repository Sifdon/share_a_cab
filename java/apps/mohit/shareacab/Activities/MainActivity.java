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

import apps.mohit.shareacab.Activities.MapViews.UserMapsView;
import apps.mohit.shareacab.R;

/**
 * This is the launcher activity of the App. It is a login screen, that allows a user to
 * log in to access the App. Users have the option to sign up for an account, or ask for
 * their password to be reset if they forgot it
 *
 * @author Mohit Kewalramani
 * @version 1.0
 * @since 2017-05-11
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;      // The instance of the Firebase Authentication

    /**
     * This method is called when the Activity is intialized. The Firebase Authentication
     * is also initialized by getting the instance of the current user's authentication
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Called when the user clicks on the Login button. It checks the validity of the entered
     * username and password, and if valid, attempts to login using the Firebase Authentication
     * platform. If successful, the screen of the MapView with all posted shares is opened
     * @param view The view from which the method is called
     */
    public void logIn(View view){
        EditText emailEntry = (EditText) findViewById(R.id.userName);
        EditText password = (EditText) findViewById(R.id.password);

        String emailEntryText = emailEntry.getText().toString().trim();
        String passwordText = password.getText().toString();

        if (emailEntryText.equals("") || passwordText.equals("")){
            Toast.makeText(
                    getApplicationContext(),
                    "Please Enter A Valid Username/Password",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(emailEntryText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            showMaps();
                            // Update screen with current user
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                    getApplicationContext(),
                                    "The Userame/Password Is Incorrect",
                                    Toast.LENGTH_SHORT).show();
                            // Update screen with current user
                        }
                    }
                });
    }

    /**
     * The helper method that launches the MapView class when the sign in is successful
     */
    private void showMaps(){
        Intent intent = new Intent(this, UserMapsView.class);
        startActivity(intent);
        finish();
    }

    /**
     * Called when a user want's to sign up for an account. This takes the user
     * to a screen for signing up
     * @param view The view from which the method is called
     */
    public void showSignUpScreen(View view){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the forgot password button. The user is asked to enter
     * their email address following which a reset password link is sent to that email
     * @param view The view from which the method is called
     */
    public void forgotPassword(View view){
        EditText emailEntry = (EditText) findViewById(R.id.userName);
        String emailEntryText = emailEntry.getText().toString().trim();
        if (emailEntryText.equals("")){
            Toast.makeText(
                    getApplicationContext(),
                    "Please Enter Your Email",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.sendPasswordResetEmail(emailEntryText)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(
                                    getApplicationContext(),
                                    "An Email Has Been Sent For Password Reset",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
