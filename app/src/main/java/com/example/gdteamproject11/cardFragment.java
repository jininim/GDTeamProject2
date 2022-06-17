package com.example.gdteamproject11;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

//카드 잔액조회 프래그먼트
public class cardFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        ImageButton btn_card = (ImageButton) v.findViewById(R.id.btn_card);
        btn_card.setOnClickListener(View->{
            Intent urlintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gdream.gg.go.kr/Login/PointCheck.jsp"));
            startActivity(urlintent);
        });

        return v;
    }
}