package com.swu.semiprojectsample.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.swu.semiprojectsample.R;
import com.swu.semiprojectsample.bean.MemberBean;
import com.swu.semiprojectsample.database.FileDB;

public class LoginActivity extends AppCompatActivity {

    //멤버변수 자리
    private EditText mEdtId,mEdtPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEdtId=findViewById(R.id.edtId);
        mEdtPw=findViewById(R.id.edtPw);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnJoin = findViewById(R.id.btnJoin);

        btnLogin.setOnClickListener(mBtnLoginClick);
        btnJoin.setOnClickListener(mBtnJoinClick);
    }

    //로그인 버튼 클릭 이벤트
    private View.OnClickListener mBtnLoginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(TextUtils.isEmpty(mEdtId.getText().toString())){
                Toast.makeText(getApplicationContext(),"회원 아이디를 입력하세요",Toast.LENGTH_SHORT).show();
                return;
            }
            else if(TextUtils.isEmpty(mEdtPw.getText().toString())){
                Toast.makeText(getApplicationContext(),"회원 비밀번호를 입력하세요",Toast.LENGTH_SHORT).show();
                return;
            }

            MemberBean findMemBean = FileDB.getFindMember(getApplicationContext(),mEdtId.getText().toString());
            if(findMemBean == null){
                Toast.makeText(getApplicationContext(),"입력하신 회원 아이디는 존재하지 않습니다",Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.equals(findMemBean.memPw,mEdtPw.getText().toString())){
                FileDB.setLoginMember(LoginActivity.this, findMemBean);
                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
            else{
                Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };

    //회원가입 버튼 클릭 이벤트
    private  View.OnClickListener mBtnJoinClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(LoginActivity.this, CameraCapture2Activity.class);
            startActivity(i);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mEdtId.setText("");
        mEdtPw.setText("");

    }
}
