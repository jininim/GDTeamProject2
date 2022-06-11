package com.example.gdteamproject11;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class LocationFragment extends Fragment {
    EditText edit_location; //주소검색
    TextView text_location;
    Button btn_search;
    XmlPullParser xpp;

    String key="60c8ba80bc6543ce932cfb76cb872dc9";
    String data;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_location, container, false);
        edit_location =  (EditText) v.findViewById(R.id.edit_location);
        text_location = (TextView) v.findViewById(R.id.result_location);
        btn_search = (Button) v.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(View ->{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        data= getXmlData();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() { //오류수정해야함
                        @Override
                        public void run() {
                            text_location.setText(data);
                        }
                    });
                }
            }).start();
        });
        return v;
    }


    private String getXmlData() throws XmlPullParserException {
        StringBuffer buffer=new StringBuffer();
        String str= edit_location.getText().toString();//EditText에 작성된 Text얻어오기
        String location = URLEncoder.encode(str);
        String queryUrl = " https://openapi.gg.go.kr/GDreamCard?KEY=60c8ba80bc6543ce932cfb76cb872dc9&SIGUN_NM="+location +"pIndex=1&pSize=100&";
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
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("row")) ;// 첫번째 검색결과
                        else if(tag.equals("SIGUN_NM")){
                            buffer.append("주소 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("FACLT_NM")){
                            buffer.append("가맹점 명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("DIV_NM")){
                            buffer.append("구분 :");
                            xpp.next();
                            buffer.append(xpp.getText());//description 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_ROADNM_ADDR")){
                            buffer.append("도로명 주소 :");
                            xpp.next();
                            buffer.append(xpp.getText());//telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("REFINE_LOTNO_ADDR")){
                            buffer.append("지번 주소:");
                            xpp.next();
                            buffer.append(xpp.getText());//address 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        }
//                        else if(tag.equals("cpTp")){
//                            buffer.append("충전 방식 :");
//                            xpp.next();
//                            buffer.append(xpp.getText());//mapx 요소의 TEXT 읽어와서 문자열버퍼에 추가
//                            buffer.append("  ,  "); //줄바꿈 문자 추가
//                        }
//                        else if(tag.equals("csId")){
//                            buffer.append("충전소 ID :");
//                            xpp.next();
//                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
//                            buffer.append("\n"); //줄바꿈 문자 추가
//                        }
//                        else if(tag.equals("cpNm")){
//                            buffer.append("충전소 명칭 :");
//                            xpp.next();
//                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
//                            buffer.append("\n"); //줄바꿈 문자 추가
//                        }
//                        else if(tag.equals("lat")){
//                            buffer.append("위도 :");
//                            xpp.next();
//                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
//                            buffer.append("\n"); //줄바꿈 문자 추가
//                        }
//                        else if(tag.equals("longi")){
//                            buffer.append("경도 :");
//                            xpp.next();
//                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
//                            buffer.append("\n"); //줄바꿈 문자 추가
//                        }
//                        else if(tag.equals("statUpdateDatetime")){
//                            buffer.append("충전기상태갱신시각 :");
//                            xpp.next();
//                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
//                            buffer.append("\n"); //줄바꿈 문자 추가
//                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("row")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.append("파싱 끝\n");
        return buffer.toString();//StringBuffer 문자열 객체 반환
    }
}