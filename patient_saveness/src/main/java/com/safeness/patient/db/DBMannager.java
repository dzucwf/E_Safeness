package com.safeness.patient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.safeness.patient.db.DBAdapter.DatabaseHelper;


/**
 * Created by EISAVISA on 2015/1/21.
 */
public class DBMannager {


    private static DatabaseHelper helper;
    private static SQLiteDatabase db;



    public DBMannager(Context context) {
        helper = new DatabaseHelper(context);
        if(db==null)
            db = helper.getWritableDatabase();


    }



}
