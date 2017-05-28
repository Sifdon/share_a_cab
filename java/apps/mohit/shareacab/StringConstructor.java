package apps.mohit.shareacab;

/**
 * Created by Mohit Kewalramani on 19-04-2017.
 */

/**
 * This class contains helper methods to help process the string manipulation required for storage
 * and display throughout the App
 *
 * @author Mohit Kewalramani
 * @version 1.0
 * @since 2017-05-11
 */
public class StringConstructor {

    /**
     * Returns the string name of the month based on the selected
     * integer value from the DatePicker Fragment
     * @param month The integer value of the month selected
     * @return string The string value of the month selected
     */
    public String getMonthString(int month){
        switch (month){
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return "Null";
    }

    /**
     * Ensures that the date values used matches the 'YYYY-mm-dd" format. This
     * method helps bring single digit values into the format. Adds 0 onto the start of the
     * string if it's a value less than 10
     * @param value Integer value of the selected month or date
     * @string string The formatted value of the given integer in the form of a string
     */
    public String doubleDigit(int value){
        if (value <= 9){
            return "0" + String.valueOf(value);
        }
        return String.valueOf(value);
    }

    /**
     * Returns the extra detail of the selected time being AM or PM based on the hour value
     * provided, so it can be added to the display of the share request's detail
     * @param hourValue The integer value of the selected hour on the TimePicker Fragment
     * @return string The string value of whether the given hour is AM or PM
     */
    public String denoteTimeOfDay(int hourValue){
        if (hourValue <= 11){
            return "AM";
        }
        return "PM";
    }

}
