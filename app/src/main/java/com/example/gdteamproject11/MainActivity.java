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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    cardFragment fragmentcard ;
    mainFragment fragmentmain;
    mypageFragment fragmentmypage;
    BottomNavigationView bt_navi;
    Dialog joinDialog;
    TextView tvName;
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
        tvName = findViewById(R.id.tvUser);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragmentmain).commit();
        initNavigationBar();

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
                        Intent intent2 = new Intent(getApplicationContext(),MapsActivity.class);
                        startActivity(intent2);
                        return true;
                }
                return false;
            }
        });
    }
}