package apps.mohit.shareacab.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import apps.mohit.shareacab.LocationHolder;
import apps.mohit.shareacab.Activities.MapViews.SelectOriginMaps;
import apps.mohit.shareacab.R;
import apps.mohit.shareacab.Types.ShareRequestEntry;
import apps.mohit.shareacab.StringConstructor;

/**
 * A method to add a new share request to the Firebase Database in order to put their request
 * on the database, and hence on the Google Maps View
 *
 * @author Mohit Kewalramani
 * @version 1.0
 * @since 2017-05-11
 */
public class RequestShare extends AppCompatActivity {

    private static StringConstructor sc;    // Class that helps construct strings for readability
    private static String departureDate;    // Chosen date for departure
    private static String departureTime;    // Chosen time for departure

    private static int chosen_year, chosen_month, chosen_day; // Chosen year, month, and date for travel
    private static int chosen_hour, chosen_minute;            // Chosen time for travel

    private FirebaseAuth firebaseAuth;

    /**
     * Method is called when this Activity is initialized. Resets the fields required
     * to make a Share Request and gets the Authentication of the current user
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_share);
        sc = new StringConstructor();
        LocationHolder.getInstance().selectedLocation = null;
        departureDate = null;
        departureTime = null;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * This method allows the user to view a separate instance of the Google Maps View
     * to select a location to choose the starting origin of the share
     * @param view The view from which the method is called
     */
    public void chooseOriginLocation(View view){
        Intent intent = new Intent(this, SelectOriginMaps.class);
        startActivity(intent);
    }

    /**
     * Displays a fragment that allows the user to choose the departure date of the
     * share
     * @param view The view from which the method is called
     */
    public void chooseDepartureDate(View view){
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), "datePicker");
    }

    /**
     * Displays a fragment that allows the user to choose the departure time of the
     * share
     * @param view The view from which the method is called
     */
    public void chooseDepartureTime(View view){
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getFragmentManager(), "timePicker");
    }

    /**
     * Adds the constructed request to the Firebase Database once the user has
     * confirmed the post. Checks if all fields are filled in as well
     * @param view The view from which the method is called
     */
    public void addRequest(View view){
        if (!checkAllFilled()){
            return;
        }
        EditText destination = (EditText) findViewById(R.id.shareDestination);
        ShareRequestEntry shareRequestEntry = new ShareRequestEntry(
                firebaseAuth.getCurrentUser().getEmail(),
                LocationHolder.getInstance().selectedLocation.latitude,
                LocationHolder.getInstance().selectedLocation.longitude,
                destination.getText().toString(),
                departureDate,
                departureTime
        );
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.push().setValue(shareRequestEntry);
        Toast.makeText(getApplicationContext(), "Request Submitted", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    /**
     * Helper method to check if all fields are filled in. If not, it relays a
     * message to the user to fill all the fields in
     * @return boolean True if all fields filled in
     */
    private Boolean checkAllFilled(){
        if (LocationHolder.getInstance().selectedLocation == null){
            Toast.makeText(
                    getApplicationContext(),
                    "Please Don't Leave Any Fields Blank",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        EditText destination = (EditText) findViewById(R.id.shareDestination);
        if (destination.getText().toString().equals("")){
            Toast.makeText(
                    getApplicationContext(),
                    "Please Don't Leave Any Fields Blank",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (departureDate == null){
            Toast.makeText(
                    getApplicationContext(),
                    "Please Don't Leave Any Fields Blank",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (departureTime == null){
            Toast.makeText(
                    getApplicationContext(),
                    "Please Don't Leave Any Fields Blank",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isDatetimeValid()){
            Toast.makeText(
                    getApplicationContext(),
                    "Please Select a Valid Time and Date",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Checks if the entered date is valid, that is a date later than or equal to today
     * @return boolean True if date is valid
     */
    private Boolean isDatetimeValid(){
        Calendar calendar = Calendar.getInstance();
        Calendar chosenDateTime = Calendar.getInstance();
        chosenDateTime.set(chosen_year, chosen_month, chosen_day, chosen_hour, chosen_minute);
        return !chosenDateTime.before(calendar);
    }

    /**
     * Cancels the current activity, and returns user to the previous activity
     * @param view The view from which the method is called
     */
    public void cancelRequest(View view){
        this.finish();
    }

    /**
     * A private method that shows a DatePicker fragment so the User can pick the date
     * for the requested share
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        /**
         * Method that is called once the Fragment is initiated.
         * It sets the chosen date on the fragment to today's date
         * @param savedInstanceState The saved instance state
         * @return DatePickerDialog The date picker dialog on which to select a date
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        /**
         * Method called when the date is selected and confirmed by the user
         * @param view The view from which the method is called
         * @param year The year selected
         * @param monthOfYear The month selected
         * @param dayOfMonth The day selected
         */
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Button chooseDate = (Button) getActivity().findViewById(R.id.chooseDepartureDate);
            departureDate = dayOfMonth + "-" + sc.getMonthString(monthOfYear) + "-" + year;
            chooseDate.setText(departureDate);

            chosen_year = year;
            chosen_month = monthOfYear;
            chosen_day = dayOfMonth;
        }
    }

    /**
     * A private method that shows a TimePicker fragment so the User can pick the time
     * for the requested share
     */
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener{

        /**
         * Method called once Fragment is initiated. Allows the user to select a
         * time for the share. Set's date display for user to see.
         * @param savedInstanceState The saved instance state
         * @return TimePickerDialog The time picker dialog to select a time from
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, android.text.format.DateFormat
                    .is24HourFormat(getActivity()));
        }

        /**
         * Method called once user selects a time and confirms the selection
         * @param view The view from which the method is called. Set's time display for
         *             user to see
         * @param hourOfDay The hour selected
         * @param minute The minute selected
         */
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Button chooseTime = (Button) getActivity().findViewById(R.id.chooseDepartureTime);
            departureTime = sc.doubleDigit(hourOfDay) + ":" + sc.doubleDigit(minute) + " " +
                    sc.denoteTimeOfDay(hourOfDay);
            chooseTime.setText(departureTime);

            chosen_hour = hourOfDay;
            chosen_minute = minute;
        }
    }

}

