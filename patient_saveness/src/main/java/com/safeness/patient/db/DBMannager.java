package com.safeness.patient.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.safeness.patient.db.DBAdapter.DatabaseHelper;
import com.safeness.patient.model.User;

import java.util.ArrayList;
import java.util.List;


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

    /**
     * 增加用户
     *
     */
    public void addUser(User user) {

        db.beginTransaction();
        try {
            db.execSQL(
                    "INSERT INTO user VALUES(null, ?)",
                    new Object[]{user.getId(),
                            user.getName(),

                    });
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction(); // 结束事务
        }

    }


    /**
     * 更新user值
     *
     * @param
     */
    public void updateUser(int id ,String name) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        db.update("user", cv, "id = ?",
                new String[] { name+"" });
    }

    /**
     * delete old user
     *
     *
     */
    public  void deleteOldUser(
           int id) {
        db.delete("user", "id = ?",
                new String[] {id+"" });
    }

    /**
     * query all user, by id
     *
     * @return List<User>
     */
    public List<User> query(int id) {
        ArrayList<User> Users = new ArrayList<User>();
        String sql="SELECT * FROM user where id="+id;
        Cursor c = db.rawQuery(sql,null);
        while (c.moveToNext()) {
            User user = new User();
            user.setId(c.getInt(c.getColumnIndex("id")));
            user.setName(c.getString(c
                    .getColumnIndex("name")));

        }
        c.close();
        return Users;
    }




}
