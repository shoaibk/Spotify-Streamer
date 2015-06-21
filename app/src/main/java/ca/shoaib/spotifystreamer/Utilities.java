package ca.shoaib.spotifystreamer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by khan on 2015-06-21.
 */
public final class Utilities {

    public static Toast showToast(Toast toast, Context context, String text) {
        if(toast != null) toast.cancel();
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
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
