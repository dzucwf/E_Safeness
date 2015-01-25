package com.safeness.e_saveness_common.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表注解格式
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * 表名
     * @return
     */
    public String name();


    /**
     * 表字段注解
     *
     * @author Darcy
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Column {

        //类型

        public static final String TYPE_INTEGER = "INTEGER";
        public static final String TYPE_STRING = "TEXT";
        public static final String TYPE_TIMESTAMP ="TIMESTAMP";
        public static final String TYPE_BOOLEAN = "BOOLEAN";
        public static final String TYPE_FLOAT = "FLOAT";
        public static final String TYPE_DOUBLE = "DOUBLE";
        public static final String TYPE_BLOB = "BLOB";

        /**
         * 默认值
         */
        public static final class DEFAULT_VALUE{
            public static final String TRUE = "1";
            public static final String FALSE = "0";
            public static final String CURRENT_TIMESTAMP = "(datetime(CURRENT_TIMESTAMP,'localtime'))";
        }

        /**
         * 列名
         * @return
         */
        public String name();

        /**
         * 字段类型
         * @return
         */
        public String type();

        /**
         * 默认值
         * @return
         */
        public String defaultValue()default "null";

        /**
         * 主键 ,默认不是
         * @return
         */
        public boolean isPrimaryKey()default false;

        /**
         * 是否可以为Null ,默认可以为Null
         * @return
         */
        public boolean isNull()default true;

        /**
         * 是否为唯一键，默认为否
         * @return
         */
        public boolean isUnique()default false;
    }

}