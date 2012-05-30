package com.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MyActivity extends Activity implements LocationListener, View.OnClickListener {
    private LocationManager mLocationManager;
    private double mLongitude;
    private double mLatitude;
    private Button mStartButton;
    private Button mStopButton;
    private Button mQuit;
    private boolean mStartRetrieveLocationFlag = false;
    private TextView mCoordView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        mStartButton = (Button) findViewById(R.id.start_retrieve_location);
        mStartButton.setOnClickListener(this);
        mStopButton = (Button) findViewById(R.id.stop_retrieve_location);
        mStopButton.setOnClickListener(this);
        mQuit = (Button) findViewById(R.id.quit);
        mQuit.setOnClickListener(this);

        mCoordView = new TextView(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.text_layout);
        layout.addView(mCoordView);
    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            gpsActivator();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && mStartRetrieveLocationFlag) {
            mLongitude = location.getLongitude();
            mLatitude = location.getLatitude();
            mCoordView.setText("Longitude = " + mLongitude + "\n\nLatitude = " + mLatitude);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Toast.makeText(MyActivity.this, "Provider status change", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(MyActivity.this, "GPS turned on", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(MyActivity.this, "GPS turned off", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if(viewId == R.id.start_retrieve_location)
            mStartRetrieveLocationFlag = true;
        if(viewId == R.id.stop_retrieve_location)
            mStartRetrieveLocationFlag = false;
        if(viewId == R.id.quit)
            finish();
    }

    private void gpsActivator(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS is disabled! Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent gpsOptionsIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(gpsOptionsIntent);
                            }
                        });
        builder.setNegativeButton("Do nothing",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
