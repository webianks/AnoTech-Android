package com.webianks.anotech;

import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by R Ankit on 22-02-2017.
 */

public class FileUtils {

    private static File filepath;

    public static boolean writeOutputFile(String content) {

        BufferedWriter bw = null;
        boolean success = false;

        try {
            bw = new BufferedWriter(new FileWriter(filepath, true));
            bw.write(content);
            bw.newLine();
            bw.flush();
            success = true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            success = false;
        } finally {
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {
            }
        }
        return success;
    }

    public static void createOutputFile(){

        File root = new File(Environment.getExternalStorageDirectory(), "Anotech");
        if (!root.exists()) {
            root.mkdirs();
        }
        filepath = new File(root,"orders_date_difference.csv");
    }

    public static void deleteFile(){

        if (filepath!=null){
            boolean deleted = filepath.delete();
            if (deleted)
                Log.d("webi","Deleted older file.");
        }
    }

}
