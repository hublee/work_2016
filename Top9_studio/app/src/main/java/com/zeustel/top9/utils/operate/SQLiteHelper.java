package com.zeustel.top9.utils.operate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/28 15:26
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "top9.db";
    private static final int DATABASE_VERSION = 1;
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        createTables(db);
    }
    public static void createTables(SQLiteDatabase db) {

    }
    public static void dropTables(SQLiteDatabase db) {

    }
    // 用户
    public static final String TABLE_USER = "userinfo";
    private static final String SQL_USER = "CREATE TABLE IF NOT EXISTS "+TABLE_USER+"("
            + "id integer,"
            + "username varchar(30),"
            + "nickName varchar(30),"
            + "email varchar(30),"
            + "icon varchar(30),"
            + "gender integer,"
            + "time long,"
            + "lastModifyTime long,"
            + "phone varchar(30),"
            + "integralAmount integer,"
            + "flowerAmount integer,"
            + "loginPlatform integer,"
            + "primary key(id,username)" + ");";

    private static final String SQL_HTML = "id integer primary key autoincrement,"
            + "title varchar(255),"
            + "title subTitle(255),"
            + "time long,"
            + "content varchar(255),"
            + "commentAmount integer,"
            + "htmlPaperType integer,";


    //网页 评测
    public static final String TABLE_EVA = "gameEva";
    private static final String SQL_EVA = "CREATE TABLE IF NOT EXISTS "+TABLE_EVA+"("
            + SQL_HTML
            + "banner varchar(255),"
            + "evaluatingType integer,"
            + "apkUrl varchar(100),"
            + "videoUrl varchar(100),"
            + "videoIcon varchar(100)"
            + ");";


    //网页 评论
    public static final String TABLE_COMMENT = "comment";
    private static final String SQL_COMMENT = "CREATE TABLE IF NOT EXISTS "+TABLE_COMMENT+"("
            + "id integer,"
            + "content varchar(255),"
            + "time long,"
            + "whichId integer,"
            + "byId integer,"
            + "foreignkey(byId) references "+TABLE_USER+"(id)" + ");";
}
