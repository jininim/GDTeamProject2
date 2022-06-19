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
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    DBHelper dbHelper;
    SQLiteDatabase sqlDB;
    // 구글 맵 참조변수 생성
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //데이터베이스 객체 생성
        dbHelper = new DBHelper(this,1);
        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 구글 맵 객체를 불러온다.
        mMap = googleMap;

        //데이터 베이스 연결 getReadableDatabase
        sqlDB = dbHelper.getReadableDatabase();

        Cursor cursor = sqlDB.rawQuery("SELECT * FROM Gdream;", null);
        while (cursor.moveToNext()) {

            runOnUiThread(new Runnable() { //runOnUiThread 메인 스레드에서 실행.
                @Override
                public void run() {
                    //데이터베이스에서 위도와 경도의 값을 받아서 Double.parseDouble(cursor.getString(4)),Double.parseDouble(cursor.getString(3))값을 넣어줌.
                    LatLng latLng1= new LatLng(Double.parseDouble(cursor.getString(4)),Double.parseDouble(cursor.getString(3)));
                    // 구글 맵에 표시할 마커에 대한 옵션 설정
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng1);
                    markerOptions.title(cursor.getString(1)); // title로 데이터베이스의 가맹점명
                    markerOptions.snippet(cursor.getString(2)); // snippet으로 데이터베이스의 구분 값을 넣어줌. (편의점 or 일반음식점)
                    mMap.addMarker(markerOptions); // 맵에 마커 추가
                }
            });


        }
        //기본 카메라 위도 경도 성결대학교로 설정.
        LatLng latLng = new LatLng(37.381282869618, 126.92872052667);
        // 성결대 위도 경도 	37.381282869618, 126.92872052667
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }

}