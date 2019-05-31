package jeno.com.myflutterapp.reciiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkChangedReceiver extends BroadcastReceiver {
    private static final String LOGTAG = "NetworkChangedReceiver";


    public NetworkChangedReceiver() {
    }


    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.isConnected()) {
                if (activeNetwork.getType() == 1) {
                    Log.i("NetworkChangedReceiver", "网络连接发生变化，当前WiFi连接可用，正在尝试重连。");
                } else if (activeNetwork.getType() == 0) {
                    Log.i("NetworkChangedReceiver", "网络连接发生变化，当前移动连接可用，正在尝试重连。");
                }

            } else {

            }
        } else {

        }
    }
}
