package com.swu.semiprojectsample.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.swu.semiprojectsample.R;
import com.swu.semiprojectsample.bean.MemberBean;
import com.swu.semiprojectsample.bean.MemoBean;
import com.swu.semiprojectsample.database.FileDB;
import com.swu.semiprojectsample.fragment.FragmentModifyCamera;
import com.swu.semiprojectsample.fragment.FragmentModifyWrite;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyMemoActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_memo);

        mTabLayout = findViewById(R.id.tabLayout3);
        mViewPager = findViewById(R.id.viewPager3);

        findViewById(R.id.btnModifyCancel).setOnClickListener(mBtnClick);
        findViewById(R.id.btnModifyUpdate).setOnClickListener(mBtnClick);

        // Tab 생성
        mTabLayout.addTab(mTabLayout.newTab().setText("글쓰기"));
        mTabLayout.addTab(mTabLayout.newTab().setText("사진찍기"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // ViewPager 생성
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });


        TextView textView3 = findViewById(R.id.textView3);
        Typeface typeface = Typeface.createFromAsset(getResources().getAssets(),"godo.ttf");
        textView3.setTypeface(typeface);
    }

    private View.OnClickListener mBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnModifyCancel:
                    finish();
                    break;
                case R.id.btnModifyUpdate:
                    updateProc();
                    break;

            }
        }
    };

    class ViewPagerAdapter extends FragmentPagerAdapter {
        int tabSize; // TAB 수

        public ViewPagerAdapter(FragmentManager fm, int count) {
            super(fm);
            this.tabSize = count; // TAB 수
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new FragmentModifyWrite();
                case 1:
                    return new FragmentModifyCamera();
            }

            return null;
        }

        @Override
        public int getCount() {
            return this.tabSize;
        }
    }

    //수정하기 버튼 저장처리
    private void updateProc(){
        //첫번째 플러그먼트의 EditText 값을 받아온다.
        FragmentModifyWrite f0 =(FragmentModifyWrite)mViewPagerAdapter.instantiateItem(mViewPager, 0);
        ////두번째 플러그먼트의 mPhotoPath 값을 받아온다.
        FragmentModifyCamera f1 =(FragmentModifyCamera)mViewPagerAdapter.instantiateItem(mViewPager, 1);

        EditText edtWriteMemo = f0.getView().findViewById(R.id.edtModifyWriteMemo);
        long memoId = f0.memoId;
        String memoStr = edtWriteMemo.getText().toString();
        String photoPath = f1.mPhotoPath;

        Toast.makeText(this,"메모내용: "+memoStr +" 사진위치: "+photoPath, Toast.LENGTH_SHORT).show();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        //TODO 파일에 저장처리
        MemoBean memo = new MemoBean();
        memo.memo=memoStr;
        memo.memoPicPath=photoPath;
        memo.memoDate=timeStamp;
        memo.memoId = memoId;

        MemberBean member = FileDB.getLoginMember(this);
        FileDB.setMemo(this,member.memId,memo,memoId );

        finish();

    }
}