package com.swu.semiprojectsample.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.swu.semiprojectsample.R;
import com.swu.semiprojectsample.bean.MemberBean;
import com.swu.semiprojectsample.bean.MemoBean;
import com.swu.semiprojectsample.database.FileDB;
import com.swu.semiprojectsample.fragment.FragmentCamera;
import com.swu.semiprojectsample.fragment.FragmentMember;
import com.swu.semiprojectsample.fragment.FragmentMemo;
import com.swu.semiprojectsample.fragment.FragmentMemoWrite;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewMemoActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_memo);

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);

        findViewById(R.id.btnCancel).setOnClickListener(mBtnClick);
        findViewById(R.id.btnSave).setOnClickListener(mBtnClick);

        // Tab 생성
        mTabLayout.addTab(mTabLayout.newTab().setText("글쓰기"));
        mTabLayout.addTab(mTabLayout.newTab().setText("사진찍기"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // ViewPager 생성
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                mTabLayout.getTabCount());
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
    }

    private View.OnClickListener mBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnCancel:
                    finish();
                    break;
                case R.id.btnSave:
                    saveProc();
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
                    return new FragmentMemoWrite();
                case 1:
                    return new FragmentCamera();
            }

            return null;
        }

        @Override
        public int getCount() {
            return this.tabSize;
        }
    }

    //저장버튼 저장처리
    private void saveProc(){
        //첫번째 플러그먼트의 EditText 값을 받아온다.
        FragmentMemoWrite f0 =(FragmentMemoWrite)mViewPagerAdapter.instantiateItem(mViewPager, 0);
        ////두번째 플러그먼트의 mPhotoPath 값을 받아온다.
        FragmentCamera f1 =(FragmentCamera)mViewPagerAdapter.instantiateItem(mViewPager, 1);

        EditText edtWriteMemo = f0.getView().findViewById(R.id.edtWriteMemo);
        String memoStr = edtWriteMemo.getText().toString();
        String photoPath = f1.mPhotoPath;

        Toast.makeText(this,"메모내용: "+memoStr +" 사진위치: "+photoPath, Toast.LENGTH_SHORT).show();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        //TODO 파일에 저장처리
        MemoBean newMemo = new MemoBean();
        newMemo.memo=memoStr;
        newMemo.memoPicPath=photoPath;
        newMemo.memoDate=timeStamp;

        MemberBean member = FileDB.getLoginMember(this);
        FileDB.addMemo(this,member.memId, newMemo);

        finish();

    }
}
