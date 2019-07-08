package com.swu.semiprojectsample.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.swu.semiprojectsample.R;
import com.swu.semiprojectsample.bean.MemberBean;
import com.swu.semiprojectsample.bean.MemoBean;
import com.swu.semiprojectsample.database.FileDB;

import java.lang.reflect.Member;

public class FragmentModifyWrite extends Fragment {

    EditText edtModifyWriteMemo;
    MemoBean memo;
    public long memoId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_write,container, false);


        edtModifyWriteMemo = view.findViewById(R.id.edtModifyWriteMemo);

        Intent intent = getActivity().getIntent();

        int index = intent.getIntExtra("INDEX", -1); // 리스트 데이터의 Index
        if (index != -1) {
            memoId=intent.getLongExtra("MEMO_ID",-1);
            MemberBean member = FileDB.getLoginMember(getContext());
            MemoBean memo = FileDB.findMemo(getContext(),member.memId,memoId);
            //memo = (MemoBean) intent.getSerializableExtra("MEMO"); // 원본 Item
            edtModifyWriteMemo.setText(memo.memo);
        }

        return view;
    }
}
