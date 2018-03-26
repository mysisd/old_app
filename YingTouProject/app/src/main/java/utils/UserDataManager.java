package utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/1/2 0002.
 * .
 */

public class UserDataManager {
    private static final String DB_NAME = "yingtou";
    private static final String TABLE_NAME = "users";
    private static final String ID = "_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_PWD = "user_pwd";
    private static final int DB_VERSION = 1;
    private Context mContext = null;
    //建表
    private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + ID + " integer primary key,"
            + USER_NAME + " varchar,"
            + USER_PWD + " varchar"
            + ");";
    private SQLiteDatabase mSqLiteDatabase = null;
    private DataBaseManagementHelper mDataBaseManagementHelper = null;


    /**
     * 创建数据库
     */
    public static class DataBaseManagementHelper extends SQLiteOpenHelper {

        public DataBaseManagementHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
            db.execSQL(DB_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    public UserDataManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 打开数据库
     */
    public void openDataBase() throws SQLException {
        mDataBaseManagementHelper = new DataBaseManagementHelper(mContext);
        mSqLiteDatabase = mDataBaseManagementHelper.getWritableDatabase();
    }

    /**
     * 关闭数据库
     */
    public void closeDataBase() throws SQLException {
        mDataBaseManagementHelper.close();
    }

    /**
     * 添加新用户，即注册（增）
     */
    public long insertData(UserData userData) {
        String userName = userData.getUserName();
        String userPwd = userData.getUserPwd();
        ContentValues values = new ContentValues();
        values.put(USER_NAME, userName);
        values.put(USER_PWD, userPwd);
        return mSqLiteDatabase.insert(TABLE_NAME, ID, values);
    }

    /**
     * 根据用户信息，修改密码
     */
    public boolean updateData(UserData userData) {
        String userName = userData.getUserName();
        String userPwd = userData.getUserPwd();
        ContentValues values = new ContentValues();
        values.put(USER_PWD, userPwd);
        String where = USER_NAME + "=" + "=\"" + userName + "\"";
        return mSqLiteDatabase.update(TABLE_NAME, values, where, null) > 0;

    }

    /**
     * 修改密码
     */
    public boolean updatePwd(UserData userData) {
        String userPwd = userData.getUserPwd();
        ContentValues values = new ContentValues();
        values.put(USER_PWD, userPwd);
        return mSqLiteDatabase.update(TABLE_NAME, values, null, null) > 0;

    }

    /**
     * 根据用户名找用户，可以判断注册时用户名是否已经存在（查）
     */
    public int queryName(String userName) {
        int result = 0;
        //第四个参数要用数组方式，不能直接用userName，不然密码无法输入英文
        Cursor cursor = mSqLiteDatabase.query(
                TABLE_NAME, null, USER_NAME + "=?", new String[]{userName}, null, null, null);
        if (cursor != null) {
            result = cursor.getCount();
            cursor.close();
        }
        return result;
    }

    /**
     * 根据用户密码修改密码，可以判断输入的密码是否跟保存的一样（查）
     */
    public int queryPwd(String userPwd) {
        int result = 0;
        //第四个参数要用数组方式，不能直接用userName，不然密码无法输入英文
        Cursor cursor = mSqLiteDatabase.query(
                TABLE_NAME, null, USER_PWD + "=?", new String[]{userPwd}, null, null, null);
        if (cursor != null) {
            result = cursor.getCount();
            cursor.close();
        }
        return result;
    }


    /**
     * 根据用户名和密码找用户，用于登录
     */
    public int queryNameAndPwd(String userName, String pwd) {
        int result = 0;
        //第四个参数要用数组方式，不能直接用userName，不然密码无法输入英文
        Cursor cursor = mSqLiteDatabase.query(
                TABLE_NAME, null, USER_NAME + "=?" + " and " + USER_PWD + "=?", new String[]{userName, pwd}, null, null, null);
        if (cursor != null) {
            result = cursor.getCount();
            cursor.close();
        }
        return result;

    }


}
