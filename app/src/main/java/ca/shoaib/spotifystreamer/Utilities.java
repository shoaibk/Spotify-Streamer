package ca.shoaib.spotifystreamer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by khan on 2015-06-21.
 */
public class Utilities {

    public static void showToastOffline(Context context) {
        Toast.makeText(context, R.string.no_internet_toast, Toast.LENGTH_LONG).show();
    }

    public static boolean isOnline(Context context) {
        boolean isOnline = true;
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (!(networkInfo != null && networkInfo.isConnected())) {
            isOnline = false;
        }
        return isOnline;
    }
}
