package com.safeness.patient.dao;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库实用类
 *
 */
public class DBUtils {
    private SQLiteDatabase mSQLiteDatabase;

    private Context mContext;

    private String dbName;

    public DBUtils(Context context, String dbName) {
        this.mContext = context;
        this.dbName = dbName;
        mSQLiteDatabase = mContext.openOrCreateDatabase(this.dbName, Context.MODE_PRIVATE,
                null);
    }

    /**
     * 更新操作
     *
     * @param table  表名
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public boolean update(String table, ContentValues values,
                          String whereClause, String[] whereArgs) {
        try {
            openDB();
            return mSQLiteDatabase
                    .update(table, values, whereClause, whereArgs) > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 插入操作
     *
     * @param table 表名
     * @param values
     * @return
     */
    public boolean insert(String table, ContentValues values) {
        try {
            openDB();
            return mSQLiteDatabase.insertOrThrow(table, null, values) != -1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 根据主键存在与否决定是否更新或者插入
     *
     * @param table
     * @param values
     * @return
     */
    public boolean insertOrReplace(String table, ContentValues values){
        try {
            openDB();
            return mSQLiteDatabase.replaceOrThrow(table, null, values)!= -1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 批量插入操作
     * @param table
     * @param listVal
     * @return
     */
    public boolean batchInsert(String table,List<ContentValues> listVal){
        try {
            openDB();
            for (ContentValues contentValues : listVal) {
                mSQLiteDatabase.insertOrThrow(table, null, contentValues);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * 删除操作
     *
     * @param table
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public boolean delete(String table, String whereClause, String[] whereArgs) {
        try {
            openDB();
            return mSQLiteDatabase.delete(table, whereClause, whereArgs) > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 查询结果集
     *
     * @param table
     * @param columns
     *            当为null的时候查出所有列
     * @param selection
     * @param selectionArgs
     * @return 出现异常的话返回null
     */
    public List<QueryResult> query(String table, String[] columns,
                                   String selection, String groupBy, String having, String orderBy,
                                   String... selectionArgs) {
        Cursor cursor = null;
        try {
            openDB();
            cursor = mSQLiteDatabase.query(table, columns, selection,
                    selectionArgs, groupBy, having, orderBy);
            List<QueryResult>  resultList = new ArrayList<QueryResult>();
            parseCursorToResult(cursor, resultList);
            return resultList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if(cursor!=null)
                cursor.close();
        }
    }

    /**
     * 查询结果集
     *
     * @param table
     * @param columns
     *            当为null的时候查出所有列
     * @param selection
     * @param selectionArgs
     * @return
     */
    public List<QueryResult> query(String table, String[] columns,
                                   String selection, String... selectionArgs) {
        return query(table, columns, selection, null, null, null, selectionArgs);
    }

    /**
     * Execute a single SQL statement that is NOT a SELECT/INSERT/UPDATE/DELETE.
     *
     * @param sql
     * @param bindArgs only byte[], String, Long and Double are supported in bindArgs.
     * @return
     */
    public boolean execSQL(String sql, Object... bindArgs) {
        try {
            openDB();
            mSQLiteDatabase.execSQL(sql, bindArgs);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 执行SQL,适用于自定义的Query
     *
     * @param sql
     * @param bindArgs
     * @return
     */
    public List<QueryResult> execQuerySQL(String sql, String... bindArgs){
        Cursor cursor = null;
        try{
            List<QueryResult>  resultList = new ArrayList<QueryResult>();
            openDB();
            cursor = mSQLiteDatabase.rawQuery(sql, bindArgs);
            parseCursorToResult(cursor, resultList);
            return resultList;
        }catch(SQLException ex) {
            ex.printStackTrace();
            return null;
        }finally{
            if(cursor!=null)
                cursor.close();
        }

    }

    public void beginTransaction(){
        mSQLiteDatabase.beginTransaction();
    }

    public void endTransaction(){
        mSQLiteDatabase.endTransaction();
    }

    public void setTransactionSuccessful(){
        mSQLiteDatabase.setTransactionSuccessful();
    }

    /**
     * 打开数据库
     */
    public void openDB() {
        if (mSQLiteDatabase.isOpen() == false)
            mSQLiteDatabase = mContext.openOrCreateDatabase(this.dbName,
                    Context.MODE_PRIVATE, null);
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        if (mSQLiteDatabase.isOpen()) {
            mSQLiteDatabase.close();
        }
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * 把Cursor对象的值映射到resultList中
     * @param cursor
     * @param resultList
     */
    @SuppressLint("NewApi")
    private void parseCursorToResult(Cursor cursor,List<QueryResult> resultList){
        int columnCount;
        int columnType;
        Object columnVal = null;
        while (cursor.moveToNext()) {
            columnCount = cursor.getColumnCount();
            QueryResult result = new QueryResult();
            for (int index = 0; index < columnCount; ++index) {
                columnType = cursor.getType(index);
                switch (columnType) {
                    case Cursor.FIELD_TYPE_BLOB:
                        columnVal = cursor.getBlob(index);
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        columnVal = cursor.getFloat(index);
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        columnVal = cursor.getInt(index);
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        columnVal = cursor.getString(index);
                        break;
                    default:
                        columnVal = cursor.getString(index);// 未知类型取用getString

                        break;
                }
                result.setProperty(cursor.getColumnName(index), columnVal);
            }
            resultList.add(result);
        }
    }
}