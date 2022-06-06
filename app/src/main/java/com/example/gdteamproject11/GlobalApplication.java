package com.example.gdteamproject11;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //Kakao SDK 초기화화
       KakaoSdk.init(this, "a9a577525dfc6766419240f9c4dd608f");
    }
}
