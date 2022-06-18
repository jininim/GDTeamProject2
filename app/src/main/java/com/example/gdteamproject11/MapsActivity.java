package com.example.gdteamproject11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.gdteamproject11.databinding.ActivityMapsBinding;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    DBHelper dbHelper;
    SQLiteDatabase sqlDB;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        dbHelper = new DBHelper(this,1);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        sqlDB = dbHelper.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT * FROM Gdream;", null);
        while (cursor.moveToNext()) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LatLng latLng1= new LatLng(Double.parseDouble(cursor.getString(4)),Double.parseDouble(cursor.getString(3)));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng1);
                    mMap.addMarker(markerOptions);
                }
            });


        }
        LatLng latLng = new LatLng(37.381282869618, 126.92872052667);
        // 성결대 위도 경도 	37.381282869618, 126.92872052667
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }
}