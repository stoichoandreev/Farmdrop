package sniper.farmdrop.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class ConnectivityChangeBroadcastReceiver extends BroadcastReceiver {
    private IConnectivityChangeListener mListener;

    public ConnectivityChangeBroadcastReceiver(IConnectivityChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (InternetUtil.hasInternet(context)) {
                mListener.connectivityChange(true);
            } else {
                mListener.connectivityChange(false);
            }
        }
    }
}