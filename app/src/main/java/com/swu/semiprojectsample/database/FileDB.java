package com.swu.semiprojectsample.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swu.semiprojectsample.bean.MemberBean;
import com.swu.semiprojectsample.bean.MemoBean;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

public class FileDB {

    private  static final String FILE_DB="FileDB";
    private  static Gson mGson = new Gson();


    private static SharedPreferences getSP(Context context){
        SharedPreferences sp = context.getSharedPreferences(FILE_DB,Context.MODE_PRIVATE);
        return sp;
    }

    //새로운 멤버 추가
    public static void addMember(Context context, MemberBean memberBean){
        //1.기존의 멤버 리스트를 불러온다
        List<MemberBean> memberList = getMemberList(context);
        //2.기존의 멤버 리스트에 추가한다
        memberList.add(memberBean);
        //3.멤버 리스트를 저장한다
        String listStr = mGson.toJson(memberList);
        //4.저장
        SharedPreferences.Editor editor = getSP(context).edit();
        editor.putString("memberList", listStr);
        editor.commit();

    }

    //멤버 교체. 메모 수정했을때나
    public static void setMember(Context context, MemberBean memberBean){
        //전체 멤버 리스트를 취득한다
        List<MemberBean> memberList = getMemberList(context);
        if(memberList.size()==0)return;

        for(int i =0; i<memberList.size(); i++){
            MemberBean bean = memberList.get(i);
            if(TextUtils.equals(bean.memId, memberBean.memId)){
                //같은 멤버 ID를 찾았다
                memberList.set(i, memberBean);
                break;
            }
        }
        //새롭게 update 된 리스트를를 저장한다
       String jsonStr = mGson.toJson(memberList);
        //멤버 리스트를 저장한다
        SharedPreferences.Editor editor = getSP(context).edit();
        editor.putString("memberList",jsonStr);
        editor.commit();
    }

    public static List<MemberBean> getMemberList(Context context) {
       String listStr = getSP(context).getString("memberList",null);
       //저장된 리스트가 없을 경우에 새로운 리스트를 리턴한다
       if(listStr == null){
           return new ArrayList<MemberBean>();
       }
        //Gson으로 변환한다
        List<MemberBean> memberList = mGson.fromJson(listStr,new TypeToken<List<MemberBean>>(){}.getType() );
       return memberList;
    }

    public static MemberBean getFindMember(Context context, String memId){
        //1.멤버 리스트를 가져온다
        List<MemberBean> memberList = getMemberList(context);
        //2.for문 돌면서 해당 아이디를 찾는다
        for(MemberBean bean : memberList){
            if(TextUtils.equals(bean.memId, memId))
                //3.찾았을 경우는 해당 MemberBean을 리턴한다
                return bean;
        }

        //3-2.못 찾았을 경우는 null리턴
        return  null;
    }

    //로그인한 MemberBean을 저장한다
    public static void setLoginMember(Context context, MemberBean bean){
        if(bean!=null){
            String str = mGson.toJson(bean);
            SharedPreferences.Editor editor = getSP(context).edit();
            editor.putString("loginMemberBean",str);
            editor.commit();
        }

    }

    //로그인한 memberBean을 취득한다
    public static MemberBean getLoginMember(Context context){
        String str = getSP(context).getString("loginMemberBean",null);
        if(str == null) return null;
        MemberBean memberBean = mGson.fromJson(str, MemberBean.class);
        return memberBean;
    }

    //메모를 추가하는 메서드
    public static void addMemo(Context context,String memId, MemoBean memoBean) {
        MemberBean findMember =  getFindMember(context,memId);
        if(findMember==null) return;

        List<MemoBean> memoList = findMember.memoList;
        if(memoList==null){
            memoList = new ArrayList<>();
        }
        //고유번호 생성
        memoBean.memoId = System.currentTimeMillis();
        memoList.add(memoBean);
        findMember.memoList=memoList;

        //저장
        setMember(context,findMember);
    }

    //메모 리스트를 획득
    public static List<MemoBean> getMemberMemoList(Context context,String memId) {
        MemberBean findMember =  getFindMember(context,memId);
        if(findMember == null) return null;

        if(findMember.memoList == null)
            findMember.memoList= new ArrayList<>();

            return findMember.memoList;
    }

    //기존 메모 수정
    public  static void setMemo(Context context,String memId, MemoBean memoBean, long memoId){
        MemberBean findMember =  getFindMember(context,memId);
        List<MemoBean> memoList = getMemberMemoList(context,memId);
        MemoBean m = findMemo(context,memId,memoId);

        m.memo=memoBean.memo;
        m.memoDate=memoBean.memoDate;
        m.memoPicPath=memoBean.memoPicPath;
        m.memoId=memoBean.memoId;

        for(int i=0;i<memoList.size();i++) {
            if(memoList.get(i).memoId==memoId) {
                memoList.set(i,m);
                break;
            }
        }

        findMember.memoList = memoList;
        setMember(context,findMember);
    }

    //메모 삭제
    public static void deleteMemo(Context context,String memId, long memoId){
        MemberBean findMember =  getFindMember(context,memId);
        List<MemoBean> memoList = getMemberMemoList(context,memId);
        MemoBean m = findMemo(context,memId,memoId);
        for(int i=0;i<memoList.size();i++) {
            if(memoList.get(i)==m) {
                memoList.remove(i);
                break;
            }
        }

        findMember.memoList = memoList;
        setMember(context,findMember);
    }

    //특정 메모 찾기
    public static MemoBean findMemo(Context context,String memId, long memoId){
        List<MemoBean> memoList = getMemberMemoList(context,memId);
        MemoBean m=null;
        for(MemoBean memo : memoList){
         if(memo.memoId == memoId){
             m = memo;
             break;
         }
        }
        return m;
    }
}
