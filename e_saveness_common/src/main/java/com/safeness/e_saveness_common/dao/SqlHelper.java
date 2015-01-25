package com.safeness.e_saveness_common.dao;

import android.content.ContentValues;

import com.safeness.e_saveness_common.dao.Table.Column;

import java.lang.reflect.Field;
import java.util.List;

/**
 * SQL语句的辅助类
 *
 *
 */
public class SqlHelper {

    /**
     * 获取CreateTable的SQL语句
     *
     * @param clazz
     * @param mOnPrimaryKeyListener
     * @return
     */
    public static String getCreateTableSQL(Class<?> clazz,
                                           OnPrimaryKeyListener mOnPrimaryKeyListener) {
        StringBuilder sqlBuidler = new StringBuilder();
        Table table = clazz.getAnnotation(Table.class);
        sqlBuidler.append("CREATE TABLE IF NOT EXISTS ");
        sqlBuidler.append(table.name());
        sqlBuidler.append("(");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAccessible() == false)
                field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if (column == null)
                continue;
            sqlBuidler.append(column.name() + " ");
            sqlBuidler.append(column.type() + " ");
            if (!column.isNull()) {
                sqlBuidler.append(" NOT NULL");
            }

            if (column.isPrimaryKey()) {
                sqlBuidler.append(" PRIMARY KEY AUTOINCREMENT");
                if (mOnPrimaryKeyListener != null)
                    mOnPrimaryKeyListener.onGetPrimaryKey(column.name());
            }

            if (column.isUnique()) {
                sqlBuidler.append(" UNIQUE");
            }

            if (!column.defaultValue().equals("null")) {
                sqlBuidler.append(" DEFAULT " + column.defaultValue());
            }

            sqlBuidler.append(",");
        }
        sqlBuidler.deleteCharAt(sqlBuidler.lastIndexOf(","));// 删除最后的,号

        sqlBuidler.append(")");
        return sqlBuidler.toString();
    }

    /**
     * 获取表名称
     *
     * @param clazz
     * @return
     */
    public static String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        return table.name();
    }

    /**
     * 把model里面的值映射到ContentValues中
     *
     * 如果model里面字段没有赋值，也就是null的时候，则不把该值映射到ContentValues
     *
     * @param model
     * @return
     */
    public static void parseModelToContentValues(Object model,
                                                 ContentValues contentValues) {
        if (contentValues.size() > 0)
            contentValues.clear();

        Class<?> clazz = model.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Class<?> fieldType = null;
        Object fieldVal = null;

        for (Field field : fields) {
            try {
                if (field.isAccessible() == false)
                    field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                fieldType = field.getType();
                fieldVal = field.get(model);
                // 如果字段值为空的情况下，不放入contentValues

                if (column == null || fieldVal == null)
                    continue;
/*
                //如果TYPE_TIMESTAMP类型的字段=0而且有默认值，不放入contentValues,以便让默认值生效
                if (fieldVal.toString() =="0" && column.type().equals("")  && column.defaultValue().equals("CURRENT_TIMESTAMP"))
                    continue;
*/
                if (fieldType.equals(int.class)) {
                    contentValues.put(column.name(), field.getInt(model));
                } else if (fieldType.equals(Integer.class)) {
                    contentValues
                            .put(column.name(), (Integer) field.get(model));
                } else if (fieldType.equals(short.class)) {
                    contentValues.put(column.name(), field.getShort(model));
                } else if (fieldType.equals(Short.class)) {
                    contentValues.put(column.name(), (Short) field.get(model));
                } else if (fieldType.equals(long.class)) {
                    contentValues.put(column.name(), field.getLong(model));
                } else if (fieldType.equals(Long.class)) {
                    contentValues.put(column.name(), (Long) field.get(model));
                } else if (fieldType.equals(float.class)) {
                    contentValues.put(column.name(), field.getFloat(model));
                } else if (fieldType.equals(Float.class)) {
                    contentValues.put(column.name(), (Float) field.get(model));
                } else if (fieldType.equals(double.class)) {
                    contentValues.put(column.name(), field.getDouble(model));
                } else if (fieldType.equals(Double.class)) {
                    contentValues.put(column.name(), (Double) field.get(model));
                } else if (fieldType.equals(boolean.class)) {
                    if (field.getBoolean(model) == true) {
                        contentValues.put(column.name(), "1");
                    } else {
                        contentValues.put(column.name(), "0");
                    }
                } else if (fieldType.equals(Boolean.class)) {
                    if ((Boolean) field.get(model) == true) {
                        contentValues.put(column.name(), "1");
                    } else {
                        contentValues.put(column.name(), "0");
                    }
                } else if (fieldType.equals(String.class)) {
                    contentValues.put(column.name(), (String) field.get(model));
                } else if (fieldType.equals(byte[].class)) {
                    contentValues.put(column.name(), (byte[]) field.get(model));
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            }
        }
    }

    /**
     * 把queryResult里面的值映射到model中来
     *
     * @param queryResult
     * @param model
     */
    public static void parseQueryResultToModel(QueryResult queryResult,
                                               Object model) {
        Class<?> clazz = model.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Object fieldVal = null;
        Class<?> fieldType = null;
        try {
            for (Field field : fields) {
                if (field.isAccessible() == false)
                    field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                if (column == null)
                    continue;
                fieldVal = queryResult.getProperty(column.name());
                fieldType = field.getType();
                if (fieldVal != null) {
                    if (fieldType.equals(fieldVal.getClass())) {
                        field.set(model, fieldVal);
                    } else if (fieldType.equals(short.class)) {
                        field.setShort(model,
                                queryResult.getShortProperty(column.name()));
                    } else if (fieldType.equals(Short.class)) {
                        field.set(model, (Short) queryResult
                                .getShortProperty(column.name()));
                    } else if (fieldType.equals(int.class)) {
                        field.setInt(model,
                                queryResult.getIntProperty(column.name()));
                    } else if (fieldType.equals(Integer.class)) {
                        field.set(model, (Integer) queryResult
                                .getIntProperty(column.name()));
                    } else if (fieldType.equals(long.class)) {
                        field.setLong(model,
                                queryResult.getLongProperty(column.name()));
                    } else if (fieldType.equals(Long.class)) {
                        field.set(model, (Long) queryResult
                                .getLongProperty(column.name()));
                    } else if (fieldType.equals(float.class)) {
                        field.setFloat(model,
                                queryResult.getFloatProperty(column.name()));
                    } else if (fieldType.equals(Float.class)) {
                        field.set(model, (Float) queryResult
                                .getFloatProperty(column.name()));
                    } else if (fieldType.equals(double.class)) {
                        field.setDouble(model,
                                queryResult.getDoubleProperty(column.name()));
                    } else if (fieldType.equals(Double.class)) {
                        field.set(model, (Double) queryResult
                                .getDoubleProperty(column.name()));
                    } else if (fieldType.equals(boolean.class)) {
                        field.setBoolean(model,
                                queryResult.getBooleanProperty(column.name()));
                    } else if (fieldType.equals(Boolean.class)) {
                        field.set(model, (Boolean) queryResult
                                .getBooleanProperty(column.name()));
                    } else if (fieldType.equals(String.class)) {
                        field.set(model,
                                queryResult.getStringProperty(column.name()));
                    } else {
                        field.set(model, fieldVal);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把queryResultList里面的值映射到modelList中来
     *
     * @param queryResultList
     * @param mList
     * @param mdlType
     *            List容器的类类型
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void parseQueryResultListToModelList(
            List<QueryResult> queryResultList, List mList, Class<?> mdlType) {
        try {
            if (queryResultList == null)
                return;
            for (QueryResult queryResult : queryResultList) {
                Object model = mdlType.newInstance(); // 根据类型信息获取一个对象

                parseQueryResultToModel(queryResult, model);
                mList.add(model);
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }
    }

    public interface OnPrimaryKeyListener {
        void onGetPrimaryKey(String keyName);
    }
}