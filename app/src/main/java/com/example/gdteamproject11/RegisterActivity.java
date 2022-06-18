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
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * loginActivity에서 "회원가입"을 클릭했을 경우에 나타난다.
 * firebase와 연동해 입력한 정보를 firebase에 넣어준다.
 */
public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;     //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private EditText mUserName, mUserEmail, mUserPwd, mUserPwdCheck, mUserPhone;
    private Button mBtnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mUserName = findViewById(R.id.et_name);
        mUserEmail = findViewById(R.id.et_email);
        mUserPwd = findViewById(R.id.et_pwd);
        mUserPwdCheck = findViewById(R.id.et_pwdCheck);
        mUserPhone = findViewById(R.id.et_Phone);
        mBtnRegister = findViewById(R.id.btnRegister);
        /** FirebaseAuth 인스턴스, DatabaseReference 초기화 **/
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* 회원가입 처리 시작 */
                final String strEmail = mUserEmail.getText().toString().trim();
                String strPwd = mUserPwd.getText().toString().trim();
                String strPwdCheck = mUserPwdCheck.getText().toString().trim();

                /* 입력한 비밀번호 오타 확인 */
                if(strPwd.equals(strPwdCheck)){
                    /*
                    다이얼로그는 가입 처리가 지연될 경우
                    사용자에게 가입 처리가 진행중임을 알려주기 위해서 사용
                     */
                    final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
                    mDialog.setMessage("가입중입니다...");
                    mDialog.show();
                    /* 파이어베이스 신규 등록 */
                    mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).
                            addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //가입 성공
                                    if(task.isSuccessful()){
                                        mDialog.dismiss();
                                        FirebaseUser userF = mFirebaseAuth.getCurrentUser();
                                        String email = userF.getEmail();
                                        String uid = userF.getUid();
                                        String name = mUserName.getText().toString();
                                        String phone = mUserPhone.getText().toString();

                                        //HashMap Table을 생성, 파이어베이스 데이터베이스에 저장
                                        HashMap<Object, String> hashMap = new HashMap<>();
                                        hashMap.put("uid",uid);
                                        hashMap.put("email",email);
                                        hashMap.put("name",name);
                                        hashMap.put("phone",phone);
                                        /* databaseReference를 매채체 삼아 저장하고, 읽어옴
                                        *  접근할 때 DatabaseReference의 인스턴스가 필요하기 때문에
                                        * 아래 코드를 작성한다. */
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("Users");

                                        /* 만들어진 reference의 uid안에 HashMap으로 만들어진 키와 값을
                                           파이어베이스에 넣어준다.
                                         */
                                        reference.child(uid).setValue(hashMap);
                                        //가입 성공시 로그인 페이지로
                                        startActivity(new Intent(RegisterActivity.this, loginActivity.class));
                                        finish();
                                        Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mDialog.dismiss();
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
