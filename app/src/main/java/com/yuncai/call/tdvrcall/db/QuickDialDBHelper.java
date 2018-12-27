package com.yuncai.call.tdvrcall.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Function:
 * Created by TianMing.Xiong on 18-12-20.
 */

public class QuickDialDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "quickDial.db";
    private static final String sql = "create table "+
            QuickDialDbSchema.QuickDialTable.NAME+"("+
            "_id integer primary key autoincrement,"+
            QuickDialDbSchema.QuickDialTable.Cols.GROUP_ID+","+
            QuickDialDbSchema.QuickDialTable.Cols.QUICK_NAME+","+
            QuickDialDbSchema.QuickDialTable.Cols.QUICK_PHONE+")";

    public QuickDialDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
          db.execSQL("drop table if exists "+ QuickDialDbSchema.QuickDialTable.NAME);
          onCreate(db);
    }
}
