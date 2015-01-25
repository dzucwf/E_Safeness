package com.safeness.e_saveness_common.dao;

import java.util.List;

public interface IBaseDao<T> {

    /**
     * 创建表
     */
    void createTable();

    /**
     * 插入一条记录
     *
     * @param model
     * @return
     */
    boolean insert(T model);

    /**
     * 插入多条记录
     *
     * @param dataList
     * @return
     */
    boolean batchInsert(List<T> dataList);

    /**
     * 更新记录
     *
     * @param model
     * @param whereClause
     * @param whereArgs
     * @return
     */
    boolean update(T model, String whereClause, String... whereArgs);

    /**
     * 决定是否insert或者Update
     * @param model
     * @param bindColumnNames 绑定的列名 ,默认是主键
     * @return
     */
    boolean insertOrUpdate(T model, String... bindColumnNames);

    /**
     * 删除记录
     *
     * @param whereClause
     * @param whereArgs
     * @return
     */
    boolean delete(String whereClause, String... whereArgs);


    /**
     * 删除全部记录
     * @return
     */
    boolean deleteAll();

    /**
     * 根据条件查询
     *
     * @param selection
     * @param selectionArgs
     * @return
     */
    List<T> queryByCondition(String selection, String... selectionArgs);

    /**
     *  根据条件查询
     * @param columns
     * @param selection
     * @param orderBy
     * @param selectionArgs
     * @return
     */
    List<T> queryByCondition(String[] columns, String selection,
                             String orderBy, String... selectionArgs);

    /**
     * 根据条件查询
     * @param columns
     * @param selection
     * @param groupBy
     * @param having
     * @param orderBy
     * @param selectionArgs
     * @return
     */
    List<T> queryByCondition(String[] columns, String selection,
                             String groupBy, String having, String orderBy,
                             String... selectionArgs);

    /**
     * 只有唯一一条记录的查询
     *
     * @return 如果没有则返回null
     */
    T queryUniqueRecord(String selection, String... selectionArgs);

    /**
     * 自定义查询
     * @param sql
     * @param bindArgs
     * @return
     */
    List<QueryResult> execQuerySQL(String sql, String... bindArgs);


    /**
     * 执行Insert/Update/Delete等其他非查询SQL
     * @param sql
     * @param bindArgs
     * @return
     */
    boolean execUpdateSQL(String sql, Object... bindArgs);
}