package com.swu.semiprojectsample.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.swu.semiprojectsample.R;
import com.swu.semiprojectsample.activity.MainActivity;
import com.swu.semiprojectsample.activity.ModifyMemoActivity;
import com.swu.semiprojectsample.activity.NewMemoActivity;
import com.swu.semiprojectsample.bean.MemberBean;
import com.swu.semiprojectsample.bean.MemoBean;
import com.swu.semiprojectsample.database.FileDB;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
            //Button btnUpdate = view.findViewById(R.id.btnUpdate);
            Button btnDelete = view.findViewById(R.id.btnDelete);
            Button btnDetail = view.findViewById(R.id.btnDetail);

            // 객체 획득
            ImageView memoImg = view.findViewById(R.id.memoImg);
            TextView txtvMemo = view.findViewById(R.id.txtvMemo);
            TextView txtvDate = view.findViewById(R.id.txtvDate);

            // 원본에서 i번째 Item 획득
            final MemoBean memo = memos.get(i);

            // 원본 데이터를 UI에 적용
            if( memo.memoPicPath != null ){
                memoImg.setImageURI(Uri.fromFile(new File(memo.memoPicPath))); }
            txtvMemo.setText(memo.memo);
            txtvDate.setText(memo.memoDate);


            // 버튼 클릭했을 때 -> 상세화면 이동
            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ModifyMemoActivity.class);
                    intent.putExtra("INDEX", i);   // 원본데이터의 순번
                    intent.putExtra("MEMO_ID", memo.memoId);
                    startActivity(intent);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    showDialog(getActivity(), "알림창","이 메모를 삭제하겠습니까?", "예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FileDB.deleteMemo(mContext,member.memId,memo.memoId);
                                memos = FileDB.getMemberMemoList(mContext, member.memId);
                                notifyDataSetChanged(); //갱신해라 명령어 어댑터를
                                Toast.makeText(getContext(), "메모를 삭제하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        },
                        "아니요", new DialogInterface.OnClickListener()  {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }
                );
                }
            });
            return view; // 완성된 UI 리턴
        }
    }

    public static void showDialog(Context context, String title, String msg,
                                  String okMsg, DialogInterface.OnClickListener okListener,
                                  String cancelMsg, DialogInterface.OnClickListener cancelListener
    ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);

        if (okListener != null) {
            builder.setPositiveButton(okMsg, okListener);
        }

        if (cancelListener != null) {
            builder.setNegativeButton(cancelMsg, cancelListener);
        }
        builder.show(); // 표시
    }
}
