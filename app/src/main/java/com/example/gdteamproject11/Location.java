package com.example.gdteamproject11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Location extends AppCompatActivity {
    EditText edit_location; //주소검색
    TextView text_location; //결과 값
    Button btn_search,btn_result; //검색버튼
    XmlPullParser xpp;
    String data; //데이터
    // DBHelper
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        edit_location =  (EditText) findViewById(R.id.edit_location);
        text_location = (TextView) findViewById(R.id.result_location);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_result = findViewById(R.id.btn_result);
        dbHelper = new DBHelper(this,1);
        btn_search.setOnClickListener(View ->{

        });
        btn_result.setOnClickListener(view ->{
            text_location.setText(dbHelper.getResult());
        });

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
}