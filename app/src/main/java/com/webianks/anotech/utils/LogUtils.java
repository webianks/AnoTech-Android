package com.webianks.anotech.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by R Ankit on 16-05-2017.
 */

public class LogUtils {

    public static String readDebugDBLog() {
        String finalLine = " ";
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                if (line.contains("DebugDB:")){
                    finalLine = line;
                    break;
                }
            }
        } catch (IOException e) {
            Log.d("webi",e.getMessage());
        }
        return finalLine;
    }


    public static StringBuilder readLogs() {
        StringBuilder logBuilder = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                logBuilder.append(line + "\n");
            }
        } catch (IOException e) {
        }
        return logBuilder;
    }
}
