package com.newproject.jhull3341.trackiteq; /**
 * Created by jhull3341 on 11/12/2015.
 */
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.util.ArrayList;

public class fileIO {
    private static final String eTAG = "Exception";

//    public void writeToFile(String fName, String allData) {
//
//        ContextWrapper c = new ContextWrapper(this);
//        Log.i(eTAG,c.getApplicationInfo().dataDir);
//
//        try {
//            File file = new File(getContext().getApplicationContext().getString(R.string.local_data_path),fName);
//            FileWriter oFile = new FileWriter(file);    // no append
//            oFile.write(allData);
//            oFile.flush();
//            oFile.close();
//        }
//        catch (IOException e) {
//            Log.i(eTAG, e.getMessage());
//        }
//    }
//    public ArrayList<String> GetFiles(String DirectoryPath) {
//
//        // this function will generate a list of files based on the passed path
//
//        ArrayList<String> MyFiles = new ArrayList<>();
//        File f = new File(DirectoryPath);
//
//        f.mkdirs();
//        File[] files = f.listFiles();
//        if (files.length == 0)
//            return null;
//        else {
//            for (int ic=0; ic<files.length; ic++) {
//                MyFiles.add(files[ic].getName());
//            }
//        }
//
//        return MyFiles;
//    }
//    private ArrayList<String> readFromFile(String fName) {
//        Log.i(eTAG,"readFromFile");
//        String ret = "";
//
//        File file = new File(getString(R.string.local_data_path),fName);
//        ArrayList<String> rows = new ArrayList<>();
//
//        try {
//            FileReader iFile = new FileReader(file);
//            BufferedReader br = new BufferedReader(iFile);
//
//            String row;
//            while ((row = br.readLine()) != null) {
//                rows.add(row);
//                Log.i(eTAG,row);
//            }
//            iFile.close();
//
//        }
//        catch (FileNotFoundException e) {
//            Log.e("login activity", "File not found: " + e.toString());
//        } catch (IOException e) {
//            Log.e("login activity", "Can not read file: " + e.toString());
//        }
//
//        return rows;
//    }
}
