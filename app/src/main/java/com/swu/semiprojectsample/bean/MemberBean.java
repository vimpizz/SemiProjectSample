package com.swu.semiprojectsample.bean;

import java.util.List;

public class MemberBean {


    public String photoPath;
    public String memId;
    public String memName;
    public String memPw;
    public String memRegDate;
    public List<MemoBean> memoList;

    public void setMemoList(List<MemoBean> memoList) {
        this.memoList = memoList;
    }
}
