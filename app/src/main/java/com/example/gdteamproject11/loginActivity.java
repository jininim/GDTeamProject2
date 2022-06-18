package com.example.gdteamproject11;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class loginActivity extends AppCompatActivity {
    Button mLogin;
    TextView mRegister;
    EditText mEmail, mPwd;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        firebaseAuth = FirebaseAuth.getInstance();
        mLogin = findViewById(R.id.btnLogin);
        mRegister = findViewById(R.id.tvRegister);
        mEmail = findViewById(R.id.inputEmail);
        mPwd = findViewById(R.id.inputPwd);

        //회원가입
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this, RegisterActivity.class));
            }
        });

        //로그인 버튼 기능 구현
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String pwd = mPwd.getText().toString();
                /*
                다이얼로그는 가입 처리가 지연될 경우
                사용자에게 가입 처리가 진행중임을 알려주기 위해서 사용
                */
                final ProgressDialog mDialog = new ProgressDialog(loginActivity.this);
                mDialog.setMessage("로그인 중");
                mDialog.show();
                /** firebase 기본 제공 로그인기능 */
                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) { //로그인에 성공할 경우
                                    startActivity(new Intent(loginActivity.this, MainActivity.class));
                                    mDialog.dismiss();
                                    finish();
                                } else { //로그인에 실패할 경우
                                    Toast.makeText(loginActivity.this, "아이디 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                                    mDialog.dismiss();
                                }
                            }
                        });
            }
        });
    }
}
