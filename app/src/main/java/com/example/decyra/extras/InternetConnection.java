package com.example.decyra.extras;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.decyra.ui.login.LoginActivity;

public class InternetConnection {

    public void showCustomDialog(AppCompatActivity i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(i);
        builder.setMessage("Please Connect to the Internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        i.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        i.startActivity(new Intent(i, LoginActivity.class));
                        i.finish();
                    }
                });
        builder.show();
    }

    public boolean isConnected(AppCompatActivity i) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) i.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) return false;

        NetworkInfo wifiConnection =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnection =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifiConnection != null && wifiConnection.isConnected())
                || (mobileConnection != null && mobileConnection.isConnected());
    }
}