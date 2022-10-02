package com.example.notes;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class fileHelper {

    public static final String FILETITLE="titleinfo.dat";
    public static final String FILEDATA="datainfo.dat";

    public static void writeTitle(Context context,ArrayList<String> title){
        try {
            FileOutputStream fos=context.openFileOutput(FILETITLE,Context.MODE_PRIVATE);
            ObjectOutputStream oas=new ObjectOutputStream(fos);
            oas.writeObject(title);
            oas.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeData(Context context, ArrayList<String> data){
        try {
            FileOutputStream fos=context.openFileOutput(FILEDATA,Context.MODE_PRIVATE);
            ObjectOutputStream oas=new ObjectOutputStream(fos);
            oas.writeObject(data);
            oas.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readTitle(Context context){
        ArrayList<String> title = null;

        try {
            FileInputStream fis = context.openFileInput(FILETITLE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            title= (ArrayList<String>) ois.readObject();
        } catch (FileNotFoundException e) {
            title =new ArrayList<>();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return title;
    }

    public static ArrayList<String> readData(Context context){
        ArrayList<String> data = null;

        try {
            FileInputStream fis = context.openFileInput(FILEDATA);
            ObjectInputStream ois = new ObjectInputStream(fis);
            data= (ArrayList<String>) ois.readObject();
        } catch (FileNotFoundException e) {
            data =new ArrayList<>();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
}
