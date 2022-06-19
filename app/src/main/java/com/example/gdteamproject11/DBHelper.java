package com.example.gdteamproject11;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DBHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "test.db";
    // DBHelper 생성자
    public DBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }
    // Table 생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Gdream(SIGUNNM TEXT, FACLTNM TEXT,DIVNM TEXT,REFINEROADNMADDR TEXT,REFINELOTNOADDR TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE Gdream");
    }
    //Table 데이터 입력
    public void insert(String SIGUN_NM,String FACLT_NM,String DIV_NM,String REFINE_ROADNM_ADDR,String REFINE_LOTNO_ADDR) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO Gdream VALUES('" + SIGUN_NM + "', '" + FACLT_NM + "','" + DIV_NM + "','" + REFINE_ROADNM_ADDR + "','" + REFINE_LOTNO_ADDR + "')");
        db.close();
    }
    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM Gdream;", null);
        while (cursor.moveToNext()) {
            result += "시군명 : " + cursor.getString(0)+ "\n"
                    + "가맹점명 : "
                    + cursor.getString(1)
                    + "\n"
                    + "구분 : "
                    + cursor.getString(2)
                    + "\n"
                    + "도로명주소 : "
                    + cursor.getString(3)
                    + "\n"
                    + "지번주소 : "
                    + cursor.getString(4)
                    + "\n"+ "\n"+ "\n"+ "\n";
        }
        return result;

    }
}