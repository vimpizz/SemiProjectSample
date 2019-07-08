package com.swu.semiprojectsample.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.swu.semiprojectsample.R;
import com.swu.semiprojectsample.activity.ModifyMemoActivity;
import com.swu.semiprojectsample.activity.NewMemoActivity;
import com.swu.semiprojectsample.bean.MemberBean;
import com.swu.semiprojectsample.bean.MemoBean;
import com.swu.semiprojectsample.database.FileDB;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class FragmentMemo extends Fragment {

    public static final int SAVE = 1001;

    public ListView mLstMemo;
    List<MemoBean> memos = new ArrayList<>();
    ListAdapter adapter;
    MemberBean member;

    public FragmentMemo() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo,container, false);

        mLstMemo = view.findViewById(R.id.lstMemo);

       view.findViewById(R.id.btnNewMemo).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(getActivity(), NewMemoActivity.class);
               startActivity(i);
           }
       });
       return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        member= FileDB.getLoginMember(getContext());
        memos = FileDB.getMemberMemoList(getContext(),member.memId);

        adapter = new ListAdapter(memos,getContext());
        mLstMemo.setAdapter(adapter);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SAVE) { // 리스트 갱신
            // DB 데이터 획득
            memos = FileDB.getMemberMemoList(getContext(),member.memId);
            // Adapter에 원본데이터 저장
            adapter.setMemos(memos);
            adapter.notifyDataSetChanged();// 리스트 UI 갱신
        }
    }

    class ListAdapter extends BaseAdapter {
        List<MemoBean> memos; // 원본 데이터
        Context mContext;
        LayoutInflater inflater;

        public ListAdapter(List<MemoBean> memos, Context context) {
            this.memos = memos;
            this.mContext = context;
            this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        public void setMemos(List<MemoBean> memos) {
            this.memos = memos;
        }

        @Override
        public int getCount() { return memos.size(); }

        @Override
        public Object getItem(int i) { return memos.get(i); }

        @Override
        public long getItemId(int i) { return i; }


        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            // view_item.xml 획득
            view = inflater.inflate(R.layout.view_memo, null);
            Button btnUpdate = view.findViewById(R.id.btnUpdate);
            Button btnDelete = view.findViewById(R.id.btnDelete);
            Button btnDetail = view.findViewById(R.id.btnDetail);

            // 객체 획득
            //ImageView imgView = view.findViewById(R.id.itemImg);
            TextView txtvMemo = view.findViewById(R.id.txtvMemo);
            TextView txtvDate = view.findViewById(R.id.txtvDate);

            // 원본에서 i번째 Item 획득
            final MemoBean memo = memos.get(i);

            // 원본 데이터를 UI에 적용
            //imgView.setImageResource(memo.getImgId());
            txtvMemo.setText(memo.memo);
            txtvDate.setText(memo.memoDate);


            // 이미지를 클릭했을 때 -> 상세화면 이동
            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ModifyMemoActivity.class);
                    //intent.putExtra("INDEX", i);   // 원본데이터의 순번
                    //intent.putExtra("ITEM", item); // 상세표시할 원본 데이터
                    startActivity(intent);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            return view; // 완성된 UI 리턴
        }
    }
}
