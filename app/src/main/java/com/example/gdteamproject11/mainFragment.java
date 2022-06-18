package com.example.gdteamproject11;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class mainFragment extends Fragment {
    ViewGroup viewGroup;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //현재 로그인된 유저 정보를 담을 변수
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();; //현재 로그인된 유저 정보 데이터베이스 참조
    TextView tvName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        tvName = v.findViewById(R.id.tvUser);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            readUser(user.getUid());
        } else{
            tvName.setText("사용자님 환영합니다 !");
        }

        return v;
    }//onCreateView END

    private void readUser(String uid) {
        mDatabase.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                tvName.setText(user.name +"님 환영합니다 !");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "데이터를 가져오는데 실패했습니다", Toast.LENGTH_LONG).show();
            }
        });
    }//readUser END
}//class END
