package com.enihsyou.shane.packagetracker.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.helper.LocationGetter;
import com.enihsyou.shane.packagetracker.network.FetchLocationTask;

import java.util.Locale;

public class SendNewPackageActivity extends AppCompatActivity {
    private static final String TAG = "SendNewPackageActivity";
    private static final int REQUEST_LOCATION_SERVICE = 254;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_new_package);
        if (resolvePermission()) getLocation();
        new FetchLocationTask(this).execute(mLocation.getLatitude(), mLocation.getLongitude());
    }

    private boolean resolvePermission() {
        int GpsPermissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int NetworkPermissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (GpsPermissionCheck != PackageManager.PERMISSION_GRANTED
            || NetworkPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("没有有效的定位手段");
                alertDialog.setMessage("请启用GPS或者网络定位");
                alertDialog.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        SendNewPackageActivity.this.startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                }, REQUEST_LOCATION_SERVICE);
            }
            return false;
        }
        return true;
    }

    private void getLocation() {
        LocationGetter getter = new LocationGetter(this);
        Log.d(TAG, "onRequestPermissionsResult: "
            + String.format(Locale.getDefault(), "%s %s", getter.getLongitude(), getter.getLatitude()));
        mLocation = getter.getCurrent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_SERVICE: {
                getLocation();
            }
        }
    }
}
