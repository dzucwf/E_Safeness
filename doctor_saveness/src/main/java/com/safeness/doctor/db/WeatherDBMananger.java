package com.safeness.doctor.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.safeness.doctor.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by EISAVISA on 2015/3/5.
 */
public class WeatherDBMananger {

    private  static String TAG = "WeatherDBMananger";
    private final int BUFFER_SIZE =400000;
    public static final String DB_NAME ="chinacity.db";//保存的数据库文件名
    public static final String PACKAGE_NAME ="com.safeness.doctor";
    public static final String DB_PATH ="/data"
            + Environment.getDataDirectory().getAbsolutePath() +"/"
            + PACKAGE_NAME; //在手机里存放数据库的位置

    private SQLiteDatabase database;
    private Context context;

    WeatherDBMananger(Context context) {
        this.context = context;
    }

    private void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        try {
            if (!(new File(dbfile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = this.context.getResources().openRawResource(
                        R.raw.chinacity); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
                    null);
            return db;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询天气通讯录
     * @param paramString
     * @return
     */
    public  String queryWeatherID(String paramString)
    {
        String str1 = null;
        openDatabase();
        if (database != null){
            Log.d(TAG,"数据库为:" + database);
            Log.d(TAG,"输入数据为" + paramString);
            try
            {
                String str2 = "Select WEATHER_ID from city_table where CITY=\"" + paramString + "\"";
                Cursor localCursor = database.rawQuery(str2, null);
                localCursor.moveToFirst();
                str1 = localCursor.getString(localCursor.getColumnIndex("WEATHER_ID"));
                localCursor.close();
                closeDatabase();
                return str1;

            }
            catch (Exception localException)
            {
                localException.printStackTrace();
                Log.d(TAG,"没有这个数据!");
                closeDatabase();
            }
        }

        return str1;
    }
    public void closeDatabase() {
        this.database.close();
    }

}
