package com.safeness.e_saveness_common.chart;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Lionnd on 2015/1/27.
 */
public class GenericChart {
    public static final class CHART_TYPE{
        public static final String LINE = "line";
        public static final String PIE = "pie";
        public static final String COLUMN = "column";
    }
    public static String getChartStr(Context ttt, String[] xAxis, String yAxisTitle, double[] series){
        String reStr = "";
        try {
            reStr = getFromAssets(ttt, "www/h.htm");
            reStr = reStr.replace("<LIONND|REPLACE|SERIES>", arrayToString(series));
            reStr = reStr.replace("<LIONND|REPLACE|XAXIS>",arrayToString(xAxis));
            reStr = reStr.replace("<LIONND|REPLACE|YAXIS|TITLE>", yAxisTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return reStr;
        }
    }

    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String arrayToString(double[] arr){
        String reStr = "";
        for (int i = 0; i < arr.length; i++) {
            reStr += arr[i];
            if (i<arr.length-1){reStr += ",";}
        }
        return reStr;
    }
    private static String arrayToString(String[] arr){
        String reStr = "";
        for (int i = 0; i < arr.length; i++) {
            reStr += "'" + arr[i] + "'";
            if (i<arr.length-1){reStr += ",";}
        }
        return reStr;
    }
    //读取文本文件中的内容
    private static String ReadTxtFile(String strFilePath)
    {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory())
        {
            Log.d("TestFile", "The File doesn't not exist.");
        }
        else
        {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null)
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while (( line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            }
            catch (java.io.FileNotFoundException e)
            {
                Log.d("TestFile", "The File doesn't not exist.");
            }
            catch (IOException e)
            {
                Log.d("TestFile", e.getMessage());
            }
        }
        return content;
    }
}
