package com.example.gdteamproject11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    cardFragment fragmentcard ;
    mainFragment fragmentmain;
    mypageFragment fragmentmypage;
    BottomNavigationView bt_navi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentcard = new cardFragment();
        fragmentmain = new mainFragment();
        fragmentmypage = new mypageFragment();

        bt_navi = findViewById(R.id.bottomNavigationView);

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
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragmentmypage).commit();
                        return true;
                        //인텐트사용 , 메인 스레드 사용위함. 프래그먼트에서는 실행안됌.
                   case R.id.location:
                       Intent intent = new Intent(getApplicationContext(),Location.class);
                       startActivity(intent);
                }
                return false;
            }
        });
    }

}