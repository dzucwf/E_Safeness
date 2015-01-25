package com.safeness.e_saveness_common.dao;

import android.content.ContentValues;
import android.content.Context;

import com.safeness.e_saveness_common.dao.Table.Column;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * <h5>通用数据访问接口</h5>
 * <p>
 * Usage:<br>
 * ===============================================<br>
 * <h5>应用事务:</h5>
 * new GenericDao.DBTransction(new DBTransctionInterface() {<br>
 *      //数据操作集合<br>
 * }.process();<br>
 * </p>
 *
 * <p>
 *    对于自定义的查询结果集，也可以通过调用静态的执行方法来获取:<br>
 *    GenericDao.execRawQuerySQL();
 * </p>
 * @param <T>
 *
 */
public class GenericDao<T> implements IBaseDao<T> {

    private Class<?> modelClazz;

    private String mTableName;

    private String primaryKey;

    private static DBUtils dbUtils;

    public GenericDao(){
    }

    public GenericDao(Context context, Class<?> modelClazz) {
        this.modelClazz = modelClazz;
        mTableName = SqlHelper.getTableName(modelClazz);
        if (dbUtils == null) {
            dbUtils = new DBUtils(context,"E_Safeness");
        }
    }


    @Override
    public void createTable() {
        String createTableSQL = SqlHelper.getCreateTableSQL(modelClazz,new SqlHelper.OnPrimaryKeyListener() {
            @Override
            public void onGetPrimaryKey(String keyName) {
                primaryKey = keyName;
            }
        });
        dbUtils.execSQL(createTableSQL);
    }

    @Override
    public boolean insert(T model) {
        ContentValues contentValues = new ContentValues();
        SqlHelper.parseModelToContentValues(model, contentValues);
        return dbUtils.insert(mTableName, contentValues);
    }

    @Override
    public boolean batchInsert(List<T> dataList) {
        List<ContentValues> listVal = new ArrayList<ContentValues>();
        for (T model : dataList) {
            ContentValues contentValues = new ContentValues();
            SqlHelper.parseModelToContentValues(model, contentValues);
            listVal.add(contentValues);
        }
        return dbUtils.batchInsert(mTableName, listVal);
    }

    @Override
    public boolean update(T model, String whereClause, String... whereArgs) {
        ContentValues contentValues = new ContentValues();
        SqlHelper.parseModelToContentValues(model, contentValues);
        return dbUtils.update(mTableName, contentValues, whereClause, whereArgs);
    }

    @Override
    public boolean delete(String whereClause, String... whereArgs) {
        return dbUtils.delete(mTableName, whereClause, whereArgs);
    }

    @Override
    public List<T> queryByCondition(String selection, String... selectionArgs) {
        return queryByCondition(null, selection, null, selectionArgs);
    }

    @Override
    public List<T> queryByCondition(String[] columns, String selection,
                                    String orderBy, String... selectionArgs) {
        return queryByCondition(columns, selection, null, null, orderBy, selectionArgs);
    }

    @Override
    public List<T> queryByCondition(String[] columns, String selection,
                                    String groupBy, String having, String orderBy,
                                    String... selectionArgs) {
        List<T> resultList = new ArrayList<T>();
        List<QueryResult> queryList = dbUtils.query(mTableName, columns, selection, groupBy, having, orderBy, selectionArgs);
        SqlHelper.parseQueryResultListToModelList(queryList, resultList, modelClazz);
        return resultList;
    }

    @Override
    public List<QueryResult> execQuerySQL(String sql, String... bindArgs){
        return dbUtils.execQuerySQL(sql, bindArgs);
    }

    /**
     * 静态执行方法获取结果集
     *
     * @param sql
     * @param bindArgs
     * @return
     */
    public static List<QueryResult> execRawQuerySQL(String sql, String... bindArgs){
        return dbUtils.execQuerySQL(sql, bindArgs);
    }

    /**
     * 事务处理类
     * @author Darcy
     *
     */
    public static class DBTransction{
        private DBTransctionInterface mTransctionInterface;

        public DBTransction(DBTransctionInterface mTransctionInterface) {
            super();
            this.mTransctionInterface = mTransctionInterface;
        }

        /**
         * 执行事务,不成功会自动回滚
         */
        public void process(){
            if(mTransctionInterface!=null){
                dbUtils.beginTransaction();
                try{
                    mTransctionInterface.onTransction();
                    dbUtils.setTransactionSuccessful();
                }finally{
                    dbUtils.endTransaction();
                }
            }
        }
    }

    /**
     * 需要进行数据库的事务处理的操作可以实现该接口
     * @author Darcy
     *
     */
    public interface DBTransctionInterface{
        void onTransction();
    }

    @Override
    public boolean execUpdateSQL(String sql, Object... bindArgs) {
        return dbUtils.execSQL(sql, bindArgs);
    }

    public static boolean execRawUpdateSQL(String sql, Object... bindArgs){
        return dbUtils.execSQL(sql, bindArgs);
    }

    @Override
    public boolean deleteAll() {
        return delete(null);
    }

    @Override
    public T queryUniqueRecord(String selection, String... selectionArgs) {
        List<T> resultList = queryByCondition(selection, selectionArgs);
        if(resultList!=null&&resultList.size()==1){
            return resultList.get(0);
        }else if(resultList!=null&&resultList.size()>1){
            StringBuilder  args = new StringBuilder();
            for (String arg : selectionArgs) {
                args.append(arg);
                args.append(",");
            }
            throw new RuntimeException("表名:"+mTableName+",条件为:selection="+selection+",selectionArgs="+args+"超过一条数据!");
        }else{
            return null;
        }
    }

    @Override
    public boolean insertOrUpdate(T model,String... bindColumnNames) {
        String[] bindColumnValues;
        String selection;
        T modelInDb;
        if(bindColumnNames==null||bindColumnNames.length==0){
            if(primaryKey==null)
                reGainPrimaryKey();
            bindColumnValues = new String[1];
            selection = primaryKey+"=?";
            bindColumnValues[0] = getValueByColumnName(model, primaryKey);
        }else{
            bindColumnValues = new String[bindColumnNames.length];
            StringBuilder selectionBuidler = new StringBuilder();
            String columnName;
            String columnValue;
            for(int index = 0; index< bindColumnNames.length; ++index){
                columnName = bindColumnNames[index];
                columnValue = getValueByColumnName(model, columnName);
                if(columnValue == null){
                    selectionBuidler.append(" "+columnName + " is null ");
                }else{
                    bindColumnValues[index]  = columnValue;
                    selectionBuidler.append(" "+columnName + "=? ");
                }
                selectionBuidler.append("and");
            }
            selection = selectionBuidler.substring(0, selectionBuidler.length()-3);
        }
        modelInDb = queryUniqueRecord(selection, bindColumnValues);
        ContentValues contentValues = new ContentValues();
        SqlHelper.parseModelToContentValues(model, contentValues);
        if(modelInDb==null){
            return dbUtils.insert(mTableName, contentValues);
        }else{
            return dbUtils.update(mTableName, contentValues, selection, bindColumnValues);
        }
    }



    /**
     * 根据列名获取对象中的值
     *
     * @param model
     * @param columnName
     * @return
     */
    private String getValueByColumnName(T model,String columnName){
        Class<?> mClazz = model.getClass();
        Field[] fields = mClazz.getDeclaredFields();

        Column column;
        for (Field field : fields) {
            field.setAccessible(true);
            column = field.getAnnotation(Column.class);
            if(column!=null&&columnName.equalsIgnoreCase(column.name())){
                try {
                    return field.get(model)==null?"":field.get(model).toString();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 重新获取primaryKey
     */
    private void reGainPrimaryKey(){
        createTable();
    }
}