package com.example.gdteamproject11;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class mypageFragment extends Fragment {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();; //현재 로그인된 유저 정보 데이터베이스 참조
    private FirebaseAuth mFirebaseAuth; //이메일 비밀번호 로그인된 모듈 변수
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //현재 로그인된 유저 정보를 담을 변수
    TextView tvSignOut;
    Dialog signOutDig;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mypage, container, false);
        tvSignOut = v.findViewById(R.id.tvSignOut);
        mFirebaseAuth = FirebaseAuth.getInstance();
        ;
        //로그아웃 구현
        signOutDig = new Dialog(getActivity());
        signOutDig.requestWindowFeature(Window.FEATURE_NO_TITLE);
        signOutDig.setContentView(R.layout.signout_dialog);
        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutDig.show();
                Button yesBtn = signOutDig.findViewById(R.id.yesBtn);
                Button noBtn = signOutDig.findViewById(R.id.noBtn);
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mFirebaseAuth.signOut();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        signOutDig.dismiss();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().remove(mypageFragment.this).commit();
                        fragmentManager.popBackStack();
                        getActivity().finish();
                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "취소하셨습니다 !", Toast.LENGTH_SHORT).show();
                        signOutDig.dismiss();
                    }
                });
            }
        });
        //로그인 여부 확인
        readUser(user.getUid());
        return v;
    }


    private void readUser(String uid) {
        mDatabase.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "데이터를 가져오는데 실패했습니다", Toast.LENGTH_LONG).show();
            }
        });
    }
}