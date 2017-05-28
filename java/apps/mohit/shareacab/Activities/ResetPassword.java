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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import apps.mohit.shareacab.Activities.MapViews.UserMapsView;
import apps.mohit.shareacab.R;

/**
 * An Activity that provides the functionality to the user to reset their password
 *
 * @author Mohit Kewalramani
 * @version 1.0
 * @since 2017-05-11
 */
public class ResetPassword extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;  // Instance of Firebase Authentication

    /**
     * Method called once activity is initialized. Retrieves the instance of the
     * current Firebase Authentication
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Executes the password change as confirmed by the user. The 2 password entries
     * are confirmed to be the same, following which the password is changed in the Firebase
     * Authentication Platform
     * @param view The view from which the method is called
     */
    public void confirmPasswordReset(View view){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        EditText newPassword = (EditText) findViewById(R.id.newPassword);
        String newPasswordString = newPassword.getText().toString();

        EditText newPasswordConfirm = (EditText) findViewById(R.id.newPasswordConfirm);
        String newPasswordConfirmString = newPasswordConfirm.getText().toString();

        if (newPasswordString.equals(newPasswordConfirmString)){
            firebaseUser.updatePassword(newPasswordString)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Password Updated",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                showHomeScreen();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(), "The Passwords Don't Match", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * This method takes the user back to the home screen. It's a helper method
     * that is called so the user can now login with their new password
     */
    private void showHomeScreen(){
        Intent intent = new Intent(this, UserMapsView.class);
        startActivity(intent);
    }

    /**
     * A method that cancels the current activity and returns the User back to the
     * previous activity
     * @param view The view from which the method is called
     */
    public void cancelPasswordReset(View view){
        this.finish();
    }
}
