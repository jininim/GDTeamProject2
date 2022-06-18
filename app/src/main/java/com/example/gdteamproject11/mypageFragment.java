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

/**
 * 로그인된 사용자 정보를 받아온다.
 * 만약 사용자가 로그인상태가 아니라면 로그인 페이지로 이동시킨다.
 * 만약 사용자의 uid가 "admin"이면 관리자로 인식, 매장 및 상품등록 버튼을 활성화한다.
 */

public class mypageFragment extends Fragment {
    /** 현재 로그인된 유저 정보 데이터베이스 참조 **/
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    /** 현재 로그인된 유저 정보를 담을 변수 **/
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    /** 이메일 비밀번호 로그인된 모듈 변수 **/
    private FirebaseAuth mFirebaseAuth;

    TextView tvSignOut;
    Dialog signOutDig;
    Button adminBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mypage, container, false);
        tvSignOut = v.findViewById(R.id.tvSignOut);
        adminBtn = v.findViewById(R.id.adminBtn);
        /** Custom Dialog **/
        signOutDig = new Dialog(getActivity());
        signOutDig.requestWindowFeature(Window.FEATURE_NO_TITLE);
        signOutDig.setContentView(R.layout.signout_dialog);
        /** 현재 로그인된 모듈 담기 **/
        mFirebaseAuth = FirebaseAuth.getInstance();

        /** 로그아웃 버튼이벤트 **/
        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** CustomDialog로 로그아웃 여부 재확인 **/
                signOutDig.show();
                Button yesBtn = signOutDig.findViewById(R.id.yesBtn);
                Button noBtn = signOutDig.findViewById(R.id.noBtn);

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mFirebaseAuth.signOut(); //로그인된 계정 로그아웃
                        signOutDig.dismiss();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        /** Fragment 삭제 **/
                        fragmentManager.beginTransaction().remove(mypageFragment.this).commit();
                        fragmentManager.popBackStack(); //최상위 트랜잭션이 스택에서 사라진다. 즉, 트랜잭션이 취소된다.
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
            }//onClick END
        });//tvSignout.setOnClickLister END

        /** 로그인 여부 확인 **/
        readUser(user.getUid());
        return v;
    }

    /**
     * firebase에서 현재 사용자 정보를 받아온다.
     * 만약 사용자의 uid가 "admin"인 경우 관리자로 인식한다.
     *
     * addValueEventListener
     * 경로 전체 내용에 대한 변경사항을 읽고 수신대기한다.
     */
    private void readUser(String uid) {
        mDatabase.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                /** 만약 로그인한 사용자 계정이 관리자일 경우 **/
                if(user.getUid().equals("admin")){
                    adminBtn.setVisibility(View.VISIBLE);
                    /*
                    adminBtn은 기존에 android:visibility="gone"으로
                    사용자는 사용할 수 없다.
                     */
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "데이터를 가져오는데 실패했습니다", Toast.LENGTH_LONG).show();
            }
        });
    }//readUser END
}//myPadgeFragment END