package tw.com.chainsea.learnwebview;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * NetworkUtils
 * Created by Fleming on 2016/12/27.
 */

public class NetworkUtils {

    private static final int MOBILE_STATUS = 0;
    private static final int WIFI_STATUS = 1;
    private static final int NO_CONNECTION_STATUS = 2;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
            return true;
        }
        return false;
    }

    public static int getNetworkStatus(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (isNetworkAvailable(context)) {
            int type = activeNetworkInfo.getType();

            if (type == ConnectivityManager.TYPE_MOBILE) {
                return MOBILE_STATUS;
            } else if (type == ConnectivityManager.TYPE_WIFI) {
                return WIFI_STATUS;
            }
        }
        return NO_CONNECTION_STATUS;
    }

    public static String getNetworkStatusString(Context context) {
        int status = getNetworkStatus(context);
        String statusName = null;
        switch (status) {
            case NetworkUtils.MOBILE_STATUS:
                statusName = context.getResources().getString(R.string.mobile_connection);
                break;
            case NetworkUtils.WIFI_STATUS:
                statusName = context.getResources().getString(R.string.wifi_connection);
                break;
            case NetworkUtils.NO_CONNECTION_STATUS:
                statusName = context.getResources().getString(R.string.no_connection);
                break;
        }
        return statusName;
    }

}