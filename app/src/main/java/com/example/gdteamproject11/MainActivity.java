package com.example.gdteamproject11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    cardFragment fragmentcard ;
    mainFragment fragmentmain;
    mypageFragment fragmentmypage;
    BottomNavigationView bt_navi;
    Dialog joinDialog;
    TextView tvName;
    DBHelper dbHelper;
    private  FirebaseAuth mFirebaseAuth; //로그인 상태여부 체크
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentcard = new cardFragment();
        fragmentmain = new mainFragment();
        fragmentmypage = new mypageFragment();
        mFirebaseAuth = FirebaseAuth.getInstance();
        joinDialog = new Dialog(MainActivity.this);
        joinDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        joinDialog.setContentView(R.layout.join_dialog);
        bt_navi = findViewById(R.id.bottomNavigationView);
        dbHelper = new DBHelper(this,1);
        tvName = findViewById(R.id.tvUser);
        new Thread(() -> {
            try {
                //xml 파싱 -> 데이터 베이스 insert
                getXmlData();

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }).start();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragmentmain).commit();
        initNavigationBar();

    }
    void getXmlData() throws XmlPullParserException {
        String arr[] = new String[5];
        //파싱 url
        String queryUrl = "https://openapi.gg.go.kr/GDreamCard?KEY=60c8ba80bc6543ce932cfb76cb872dc9&pIndex=10&pSize=1000&";
        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
            String tag;
            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기
                        if (tag.equals("SIGUN_NM")) { //xml 태그의 값이 SIGUN_NM일경우 실행.
                            xpp.next();
                            arr[0]=(xpp.getText());
                        } else if (tag.equals("FACLT_NM")) { // 가맹점명
//
                            xpp.next();
                            arr[1]=(xpp.getText());//FACLT_NM 요소의 TEXT 읽어와서 배열에 추가
                        } else if (tag.equals("DIV_NM")) {
//
                            xpp.next();

                            arr[2]=(xpp.getText());//DIV_NM 요소의 TEXT 읽어와서 배열에 추가
                        }
                        else if (tag.equals("REFINE_LOTNO_ADDR")) {
//
                            xpp.next();
                        }
                        else if (tag.equals("REFINE_ROADNM_ADDR")) {
//
                            xpp.next();
                        }
                        else if (tag.equals("REFINE_ZIP_CD")) {
//
                            xpp.next();
                        }else if (tag.equals("REFINE_WGS84_LOGT")) { // 경도
//
                            xpp.next();
                            arr[3]=(xpp.getText());//REFINE_WGS84_LOGT 요소의 TEXT 읽어와서 배열에 추가

                        } else if (tag.equals("REFINE_WGS84_LAT")) { // 위도
//
                            xpp.next();
                            arr[4]=(xpp.getText());//REFINE_WGS84_LAT 요소의 TEXT 읽어와서 배열에 추가
                            if (arr[4] != null){ //
                                dbHelper.insert(arr[0],arr[1],arr[2],arr[3],arr[4]); //데이터베이스 데이터 추가.
                            }

                        }

                        break;
                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기
                        if(tag.equals("row"))
                            break;
                }
                eventType= xpp.next();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void initNavigationBar(){
        bt_navi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragmentmain).commit();
                        return true;
                    case R.id.shopping:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragmentcard).commit();
                        return true;
                    case R.id.my:
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user == null){
                            joinDialog.show();
                            Button yesBtn = joinDialog.findViewById(R.id.yesBtn);
                            Button noBtn = joinDialog.findViewById(R.id.noBtn);
                            yesBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getApplicationContext(), loginActivity.class));
                                    joinDialog.dismiss();
                                }
                            });
                            noBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(MainActivity.this, "취소하셨습니다 !", Toast.LENGTH_SHORT).show();
                                    joinDialog.dismiss();
                                }
                            });
                            return false;
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragmentmypage).commit();
                        return true;
                    //인텐트사용 , 메인 스레드 사용위함. 프래그먼트에서는 실행안됌.
                    case R.id.location:
                        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }
}