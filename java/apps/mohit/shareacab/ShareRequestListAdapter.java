package apps.mohit.shareacab;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.HashMap;

import apps.mohit.shareacab.Types.ShareRequestEntry;

/**
 * Created by Mohit Kewalramani on 30-04-2017.
 */

/**
 * This class is a helper to populate the ListView on the user's view profile when they are
 * viewing all the posts they have made
 *
 * @author Mohit Kewalramani
 * @version 1.0
 * @since 2017-05-11
 */
public class ShareRequestListAdapter extends BaseAdapter {

    // List of Entries that will be shown on the ListView (User's personal posts)
    public HashMap<String, ShareRequestEntry> userEntries = new HashMap<>();

    /**
     * Returns the size of the list that contains the user's posted requests
     * @return int The size of the list
     */
    @Override
    public int getCount() {
        return userEntries.size();
    }

    /**
     * Returns the ShareRequest item in the List at the index of position
     * @param position The index to look for
     * @return ShareRequestEntry The item at the position of the list
     */
    @Override
    public Object getItem(int position) {
        return userEntries.get(position);
    }

    /**
     * Returns the index we are currently searching for
     * @param position The index we are currently searching for
     * @return int The index we are currently searching for
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Populates the respective TextView's each segment on the ListView as the method iterates
     * through each item in the List who's data we are populating
     * @param position The index of the current item we are populating
     * @param convertView The view onto which we are populating the data
     * @param parent The parent of the view onto which we are populating data
     * @return View The view with the populated data on it
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.share_list_view, parent, false);
        }
        ShareRequestEntry shareRequestEntry =
                (ShareRequestEntry) userEntries.values().toArray()[position];

        TextView listViewDestination = (TextView) convertView.findViewById(R.id.list_view_destination);
        listViewDestination.setText(String.format("Destination : %s",
                shareRequestEntry.getDestination()));

        TextView listViewShareDetails = (TextView) convertView.findViewById(R.id.list_view_time_date);
        listViewShareDetails.setText(String.format("Date : %s   Time : %s",
                shareRequestEntry.getTravelDate(), shareRequestEntry.getTravelTime()));

        TextView listViewDatePosted = (TextView) convertView.findViewById(R.id.list_view_date_posted);
        listViewDatePosted.setText(String.format("Date Posted : %s",
                shareRequestEntry.getPostDate()));

        return convertView;
    }
}
