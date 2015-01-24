package com.safeness.patient.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;

/**
 * Dao的创建对象
 *
 */
public class DaoFactory {


    private static Map<Class<?>,Object> daoMap = new ConcurrentHashMap<Class<?>,Object>();

    /**
     * 获取GenericDao
     * @param context
     * @param modelClazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> IBaseDao<T> createGenericDao(Context context, Class<?> modelClazz){
        if(!daoMap.containsKey(modelClazz)){
            IBaseDao<T> dao = new GenericDao<T>(context,modelClazz);
            dao.createTable();
            daoMap.put(modelClazz, dao);
        }
        return (IBaseDao<T>) daoMap.get(modelClazz);
    }

}