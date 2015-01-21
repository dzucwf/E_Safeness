package com.safeness.patient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.safeness.patient.bussiness.DBConstant;

/**
 * Created by EISAVISA on 2015/1/21.
 */
public class DBAdapter {

    private static  String TAG="DBAdapter";

    private Context mContext;

    private  DatabaseHelper databaseHelper;

    public DBAdapter(Context context){
        mContext = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 0;

        public DatabaseHelper(Context context) {
            super(context, DBConstant.DBName, null, DATABASE_VERSION);
        }


        //以下是建表语句

        // Creation sql command

        private static final String TABLE_USER_CREATE = "CREATE TABLE IF NOT EXISTS "
                + DBConstant.USER
                + " ("
                + DBConstant.FIELD_ID+ 				" INTEGER PRIMARY KEY AUTOINCREMENT,"

                // Application relative fields
                + DBConstant.FIELD_NAME				+ " TEXT,"


                + ");";
        @Override
        public void onCreate(SQLiteDatabase db) {
                db.execSQL(TABLE_USER_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
