package com.swu.semiprojectsample.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.swu.semiprojectsample.R;
import com.swu.semiprojectsample.bean.MemberBean;
import com.swu.semiprojectsample.database.FileDB;

import java.io.File;

public class FragmentMember extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member,container, false);

        ImageView imgProfile = view.findViewById(R.id.imgProfile);
        TextView txtMemId = view.findViewById(R.id.txtMemId);
        TextView txtMemName = view.findViewById(R.id.txtMemName);
        TextView txtMemPw = view.findViewById(R.id.txtMemPw);
        TextView txtMemDate = view.findViewById(R.id.txtMemDate);

        //파일DB에서 가져온다
        MemberBean memberBean = FileDB.getLoginMember(getActivity());


       if( memberBean.photoPath != null ){
            imgProfile.setImageURI(Uri.fromFile(new File(memberBean.photoPath)));
        }
        txtMemId.setText("아이디 : "+memberBean.memId);
        txtMemName.setText("이름 : "+memberBean.memName);
        txtMemPw.setText("비밀번호 : "+memberBean.memPw);
        txtMemDate.setText("가입 날짜 : "+memberBean.memRegDate);



        return view;

    }
}
