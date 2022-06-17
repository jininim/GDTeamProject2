package com.example.gdteamproject11;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;     //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private EditText mUserName, mUserEmail, mUserPwd, mUserPwdCheck, mUserPhone;
    private Button mBtnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mUserName = findViewById(R.id.et_name);
        mUserEmail = findViewById(R.id.et_email);
        mUserPwd = findViewById(R.id.et_pwd);
        mUserPwdCheck = findViewById(R.id.et_pwdCheck);
        mUserPhone = findViewById(R.id.et_Phone);
        mBtnRegister = findViewById(R.id.btnRegister);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 처리 시작
                String strName = mUserName.getText().toString().trim();
                final String strEmail = mUserEmail.getText().toString().trim();
                String strPwd = mUserPwd.getText().toString().trim();
                String strPwdCheck = mUserPwdCheck.getText().toString().trim();
                String strPhone = mUserPhone.getText().toString().trim();

                if(strPwd.equals(strPwdCheck)){
                    Log.d(TAG,"등록 버튼" + "Email : " + strEmail + ", Pwd : " + strPwd + ", PwdCheck : "+ strPwdCheck);
                    final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
                    mDialog.setMessage("가입중입니다...");
                    mDialog.show();
                    //파이어베이스 신규 등록
                    mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).
                            addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //가입 성공
                                    if(task.isSuccessful()){
                                        mDialog.dismiss();
                                        FirebaseUser userF = mFirebaseAuth.getCurrentUser(); //현재 로그인된 유저를 userF에 저장
                                        String email = userF.getEmail();
                                        String uid = userF.getUid();
                                        String name = mUserName.getText().toString();
                                        String phone = mUserPhone.getText().toString();
                                        Log.d("해쉬맵 전", "hash");
                                        //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                                        HashMap<Object, String> hashMap = new HashMap<>();

                                        hashMap.put("uid",uid);
                                        hashMap.put("email",email);
                                        hashMap.put("name",name);
                                        hashMap.put("phone",phone);

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("Users");
                                        reference.child(uid).setValue(hashMap);

                                        //가입 성공시 로그인 페이지로
                                        startActivity(new Intent(RegisterActivity.this, loginActivity.class));
                                        finish();
                                        Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "이메일 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
                }//if(strPwd.equals(strPwdCheck))END
                //비밀번호 오류시
                else {
                    Toast.makeText(RegisterActivity.this, "비밀번호가 틀렸습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }//onClickEND
        });//mBtnRegisterEnd
    }//onCreateEnd
}//RegisterActivity_class_END
