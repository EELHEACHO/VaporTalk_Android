package com.project.eelhea.vaportalk_android.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.project.eelhea.vaportalk_android.DTO.User;

import java.util.ArrayList;

/**
 * Created by eelhea on 2017. 4. 25..
 */

public class UserMyFriendDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "UserMyFriendDBHelper";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VaporTalkDB.db";
    public static final String MY_FRIEND_TABLE_NAME = "myFriend";
    public static final String COLUMN_UID = "uid";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SEX = "sex";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_TEL = "tel";
    public static final String COLUMN_COMM_AGREE = "isCommerceAgree";
    public static final String COLUMN_NEAR_AGREE = "isNearAgree";
    public static final String COLUMN_PROFILE_IMG = "profileImage";

    public UserMyFriendDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MY_FRIEND_TABLE_NAME + " (" + COLUMN_UID + " TEXT PRIMARY KEY, " +
                                                                    COLUMN_EMAIL + " TEXT, " +
                                                                    COLUMN_NAME + " TEXT, " +
                                                                    COLUMN_SEX + " TEXT, " +
                                                                    COLUMN_BIRTHDAY + " TEXT, " +
                                                                    COLUMN_TEL + " TEXT, " +
                                                                    COLUMN_COMM_AGREE + " TEXT, " +
                                                                    COLUMN_NEAR_AGREE + " TEXT, " +
                                                                    COLUMN_PROFILE_IMG + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MY_FRIEND_TABLE_NAME);
    }

    public boolean deleteAllMyFriend(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + MY_FRIEND_TABLE_NAME);

        return true;
    }

    public boolean deleteMyFriend(String email){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + MY_FRIEND_TABLE_NAME + " WHERE EMAIL = '" + email + "'");

        return true;
    }

    public boolean insertMyFriend(User userMyFriend){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uid", userMyFriend.getUid());
        contentValues.put("email", userMyFriend.getEmail());
        contentValues.put("name", userMyFriend.getName());
        contentValues.put("sex", userMyFriend.getSex());
        contentValues.put("birthday", userMyFriend.getBirthday());
        contentValues.put("tel", userMyFriend.getTel());
        contentValues.put("isCommerceAgree", userMyFriend.getIsCommerceAgree());
        contentValues.put("isNearAgree", userMyFriend.getIsNearAgree());
        contentValues.put("profileImage", userMyFriend.getProfileImage());
        sqLiteDatabase.insert(MY_FRIEND_TABLE_NAME, null, contentValues);

        return true;
    }

    public ArrayList<User> getAllMyFriend(){
        ArrayList<User> userMyFriendArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + MY_FRIEND_TABLE_NAME, null);

        if(cursor.moveToFirst()){
            do{
                User userMyFriend = new User();
                userMyFriend.setUid(cursor.getString(0));
                userMyFriend.setEmail(cursor.getString(1));
                userMyFriend.setName(cursor.getString(2));
                userMyFriend.setSex(cursor.getString(3));
                userMyFriend.setBirthday(cursor.getString(4));
                userMyFriend.setTel(cursor.getString(5));
                userMyFriend.setIsCommerceAgree(cursor.getString(6));
                userMyFriend.setIsNearAgree(cursor.getString(7));
                userMyFriend.setProfileImage(cursor.getString(8));

                userMyFriendArrayList.add(userMyFriend);
            }while(cursor.moveToNext());
        }

        Log.e(TAG, String.format("%d", userMyFriendArrayList.size()));

        return userMyFriendArrayList;
    }

    public User getOneMyFriend(String email){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + MY_FRIEND_TABLE_NAME + " WHERE EMAIL = '" + email + "'", null);

        if(cursor!=null){
            cursor.moveToFirst();
        }

        User userMyFriend = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));

        return userMyFriend;
    }

    public int UpdateMyFriend(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        return 0;
    }
}
