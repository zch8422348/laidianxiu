package com.yuncai.call.tdvrcall.db;

/**
 * Function:定义快速拨号 数据表结构
 * Created by TianMing.Xiong on 18-12-20.
 */

public class QuickDialDbSchema {
    public static final class QuickDialTable{
        public static final String NAME = "quick_dial";
        public static final class Cols{
            public static final String _ID = "_id";
            public static final String GROUP_ID = "group_id";
            public static final String QUICK_NAME = "quick_name";
            public static final String QUICK_PHONE = "quick_phone";
        }

    }
}
