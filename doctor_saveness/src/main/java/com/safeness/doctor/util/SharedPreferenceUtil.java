package com.safeness.doctor.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Map;

/**
 * Created by EISAVISA on 2015/3/5.
 */
public class SharedPreferenceUtil {

    private static SharedPreferenceUtil mSharedPreferenceUtil;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    private SharedPreferenceUtil(Context paramContext)
    {
        this.sharedPreferences = paramContext.getSharedPreferences("com.safeness.doctor", 0);
        this.editor = this.sharedPreferences.edit();
    }

    public static SharedPreferenceUtil getInstance(Context paramContext)
    {
        if (mSharedPreferenceUtil == null)
            mSharedPreferenceUtil = new SharedPreferenceUtil(paramContext);
        return mSharedPreferenceUtil;
    }

    public Map<String, ?> loadAllPrefer(String paramString)
    {
        return this.sharedPreferences.getAll();
    }

    public boolean loadBooleanPrefer(String paramString)
    {
        return this.sharedPreferences.getBoolean(paramString, false);
    }

    public float loadFloatPrefer(String paramString)
    {
        return this.sharedPreferences.getFloat(paramString, 0.0F);
    }

    public int loadIntPrefer(String paramString)
    {
        return this.sharedPreferences.getInt(paramString, 0);
    }

    public long loadLongPrefer(String paramString)
    {
        return this.sharedPreferences.getLong(paramString, 0L);
    }

    public String loadStrPrefer(String paramString)
    {
        try
        {
            String str = this.sharedPreferences.getString(paramString, "");
            return str;
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return "";
    }

    public boolean removeAllKey()
    {
        SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
        localEditor.clear();
        return localEditor.commit();
    }

    public boolean removeKey(String paramString)
    {
        SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
        localEditor.remove(paramString);
        return localEditor.commit();
    }

    public boolean save(String paramString, float paramFloat)
    {
        try
        {
            this.editor.putFloat(paramString, paramFloat);
            return this.editor.commit();
        }
        catch (Exception localException)
        {
            while (true)
            {
                this.editor.putFloat(paramString, paramFloat);
                localException.printStackTrace();
            }
        }
    }

    public boolean save(String paramString, int paramInt)
    {
        try
        {
            this.editor.putInt(paramString, paramInt);
            return this.editor.commit();
        }
        catch (Exception localException)
        {
            while (true)
            {
                this.editor.putInt(paramString, paramInt);
                localException.printStackTrace();
            }
        }
    }

    public boolean save(String paramString, Boolean paramBoolean)
    {
        try
        {
            this.editor.putBoolean(paramString, paramBoolean.booleanValue());
            return this.editor.commit();
        }
        catch (Exception localException)
        {
            while (true)
            {
                this.editor.putBoolean(paramString, paramBoolean.booleanValue());
                localException.printStackTrace();
            }
        }
    }

    public boolean save(String paramString, Long paramLong)
    {
        try
        {
            this.editor.putLong(paramString, paramLong.longValue());
            return this.editor.commit();
        }
        catch (Exception localException)
        {
            while (true)
            {
                this.editor.putLong(paramString, paramLong.longValue());
                localException.printStackTrace();
            }
        }
    }

    public boolean save(String paramString1, String paramString2)
    {
        try
        {
            this.editor.putString(paramString1, paramString2);
            return this.editor.commit();
        }
        catch (Exception localException)
        {
            while (true)
            {
                this.editor.putString(paramString1, paramString2);
                localException.printStackTrace();
            }
        }
    }

    public boolean saveAllPrefer(String paramString, List<?> paramList)
    {
        int count = paramList.size();
        if (count < 1)
            return false;

        for (int j = 0; j < count; j++) {
            if ((paramList.get(j) instanceof Long))
            {
                this.editor.putLong(paramString + j, ((Long)paramList.get(j)).longValue());

                continue;
            }
            if ((paramList.get(j) instanceof Float))
            {
                this.editor.putFloat(paramString + j, ((Float) paramList.get(j)).floatValue());

                continue;
            }
            if ((paramList.get(j) instanceof Integer))
            {
                this.editor.putLong(paramString + j, ((Integer) paramList.get(j)).intValue());

                continue;
            }
            if ((paramList.get(j) instanceof String))
            {
                this.editor.putString(paramString + j, paramList.get(j).toString());
            }
            if ((paramList.get(j) instanceof Boolean))
            {
                this.editor.putBoolean(paramString + j, ((Boolean) paramList.get(j)).booleanValue());
            }
        }
        return this.editor.commit();


    }
}
