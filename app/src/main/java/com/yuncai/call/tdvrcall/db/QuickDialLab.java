package com.yuncai.call.tdvrcall.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yuncai.call.tdvrcall.bean.QuickContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 * Created by TianMing.Xiong on 18-12-20.
 */

public class QuickDialLab {
    private QuickContact quickContact;
    private Context mContext;
    private final SQLiteDatabase mDatabase;
    private final ArrayList<QuickContact> quickContacts;

    public QuickDialLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new QuickDialDBHelper(mContext).getWritableDatabase();
        quickContacts = new ArrayList<>();

    }
    // 添加号码
    public boolean addQuickDial(QuickContact q){
        if(isQuickContact(q)){
            return false;
        }
        ContentValues values = getContentValue(q);
        long insert = mDatabase.insert(QuickDialDbSchema.QuickDialTable.NAME, null, values);
        return insert>0;
    }
    // 删除号码
    public void deleteQuickDial(QuickContact quickContact){

        String groupId = quickContact.getqGroupId();
        String name = quickContact.getqName();
        String phone = quickContact.getqPhoneNumber();
        int delete = mDatabase.delete(QuickDialDbSchema.QuickDialTable.NAME,
                QuickDialDbSchema.QuickDialTable.Cols.GROUP_ID
                        +" = ? and "+ QuickDialDbSchema.QuickDialTable.Cols.QUICK_NAME
                        +" = ? and "+ QuickDialDbSchema.QuickDialTable.Cols.QUICK_PHONE+" = ?", new String[]{groupId+"",name +"",phone});
        Log.e("========","delete:"+delete);
    }
    // 修改号码
    public void updateQuickDial(QuickContact oldQuickContact,QuickContact newQuickContact){
        String groupId = oldQuickContact.getqGroupId();
        String name = oldQuickContact.getqName();
        String phone = oldQuickContact.getqPhoneNumber();
        ContentValues contentValue = getContentValue(newQuickContact);
        mDatabase.update(QuickDialDbSchema.QuickDialTable.NAME,contentValue,
                QuickDialDbSchema.QuickDialTable.Cols.GROUP_ID
                        +" = ? and "+ QuickDialDbSchema.QuickDialTable.Cols.QUICK_NAME
                        +" = ? and "+ QuickDialDbSchema.QuickDialTable.Cols.QUICK_PHONE+" = ?", new String[]{groupId+"",name +"",phone});
    }

    // 查询单个号码
    public boolean  isQuickContact(QuickContact quickContact){
        String groupId = quickContact.getqGroupId();
        String name = quickContact.getqName();
        String phone = quickContact.getqPhoneNumber();
        Cursor cursor = queryQuickContacts(QuickDialDbSchema.QuickDialTable.Cols.GROUP_ID
                +" = ? and "+ QuickDialDbSchema.QuickDialTable.Cols.QUICK_NAME
                +" = ? and "+ QuickDialDbSchema.QuickDialTable.Cols.QUICK_PHONE+" = ?", new String[]{groupId+"",name +"",phone});
       if(cursor==null || cursor.getCount() == 0){
           return false;
       }
        return true;
    }
    // 根据号码查询名字
    public String  qureyNameByPhone(String phone){
        Cursor cursor = queryQuickContacts(QuickDialDbSchema.QuickDialTable.Cols.QUICK_PHONE
                +" = ? ", new String[]{phone});
        if(cursor==null || cursor.getCount() == 0){
            return phone;
        }
        cursor.moveToNext();
        String name = cursor.getString(cursor.getColumnIndex(QuickDialDbSchema.QuickDialTable.Cols.QUICK_NAME));
        return name;
    }
    // 查询全部号码
    public List<QuickContact> queryAllQuickContact(String groupId){
        quickContacts.clear();
        Cursor cursor = queryQuickContacts(QuickDialDbSchema.QuickDialTable.Cols.GROUP_ID
                +" = ? ", new String[]{groupId});
        while (cursor.moveToNext()){
            groupId = cursor.getString(cursor.getColumnIndex(QuickDialDbSchema.QuickDialTable.Cols.GROUP_ID));
            String name = cursor.getString(cursor.getColumnIndex(QuickDialDbSchema.QuickDialTable.Cols.QUICK_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(QuickDialDbSchema.QuickDialTable.Cols.QUICK_PHONE));
            QuickContact quickContact = new QuickContact();
            quickContact.setqGroupId(groupId);
            quickContact.setqName(name);
            quickContact.setqPhoneNumber(phone);
            quickContacts.add(quickContact);
        }
        return quickContacts;
    }

    private ContentValues getContentValue(QuickContact quickContact) {
        ContentValues values = new ContentValues();
        values.put(QuickDialDbSchema.QuickDialTable.Cols.GROUP_ID,quickContact.getqGroupId());
        values.put(QuickDialDbSchema.QuickDialTable.Cols.QUICK_NAME,quickContact.getqName());
        values.put(QuickDialDbSchema.QuickDialTable.Cols.QUICK_PHONE,quickContact.getqPhoneNumber());
        return values;
    }

    private Cursor queryQuickContacts(String whereClause,String[] whereArgs){
        Cursor cursor = mDatabase.query(QuickDialDbSchema.QuickDialTable.NAME, null, whereClause,
                whereArgs, null, null, null);
        return cursor;
    }



}
