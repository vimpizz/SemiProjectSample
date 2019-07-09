package com.swu.semiprojectsample.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.swu.semiprojectsample.R;
import com.swu.semiprojectsample.activity.ModifyMemberActivity;
import com.swu.semiprojectsample.activity.ModifyPwActivity;
import com.swu.semiprojectsample.bean.MemberBean;
import com.swu.semiprojectsample.database.FileDB;

import java.io.File;

public class FragmentMember extends Fragment {

    MemberBean memberBean;
    ImageView imgProfile;
    TextView txtMemId,txtMemName, txtMemPw,txtMemDate;

    MemberBean member;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member,container, false);

        imgProfile = view.findViewById(R.id.imgProfile);
        txtMemId = view.findViewById(R.id.txtMemId);
        txtMemName = view.findViewById(R.id.txtMemName);
        txtMemPw = view.findViewById(R.id.txtMemPw);
        txtMemDate = view.findViewById(R.id.txtMemDate);



        Button btnModifyMember = view.findViewById(R.id.btnModifyMember);
        btnModifyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ModifyMemberActivity.class);
                startActivity(intent);
            }
        });

        Button btnModifyPw = view.findViewById(R.id.btnModifyPw);
        btnModifyPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ModifyPwActivity.class);
                startActivity(intent);
            }
        });


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        //파일DB에서 가져온다
        memberBean = FileDB.getLoginMember(getActivity());
        //member = FileDB.getFindMember(getContext(),memberBean.memId);
        //memberBean= member;


        if( memberBean.photoPath != null ){
            imgProfile.setImageURI(Uri.fromFile(new File(memberBean.photoPath)));
        }

        txtMemId.setText("아이디 : "+memberBean.memId);
        txtMemName.setText("이름 : "+memberBean.memName);
        txtMemPw.setText("비밀번호 : "+memberBean.memPw);
        txtMemDate.setText("가입 날짜 : "+memberBean.memRegDate);

    }
}
