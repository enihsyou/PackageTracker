package com.enihsyou.shane.packagetracker.helper;

import android.content.Context;
import android.util.Log;
import com.enihsyou.shane.packagetracker.model.PackageTrafficSearchResult;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataBase {
    private static final String TAG = "DataBase";
    private final static String PACKAGE_FILENAME = "Package";
    private static DataBase self;
    private Context mContext;

    private DataBase(Context context) {
        mContext = context;
    }

    public static DataBase getInstance(Context context) {
        if (self == null) self = new DataBase(context);
        return self;
    }

    public boolean writePackages(ArrayList<PackageTrafficSearchResult> packages) {
        try {
            ObjectOutputStream outputStream =
                new ObjectOutputStream(mContext.openFileOutput(PACKAGE_FILENAME, Context.MODE_PRIVATE));
            outputStream.writeObject(packages);
            outputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "writePackages: 文件未找到", e);
        } catch (IOException e) {
            Log.e(TAG, "writePackages: IO", e);
        }
        return false;
    }

    public ArrayList<PackageTrafficSearchResult> readPackages() {
        try {
            ObjectInputStream inputStream =
                new ObjectInputStream(mContext.openFileInput(PACKAGE_FILENAME));
            ArrayList<PackageTrafficSearchResult> p =
                ((ArrayList<PackageTrafficSearchResult>) inputStream.readObject());
            inputStream.close();
            return p;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "writePackages: 文件未找到", e);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "writePackages: 类未找到", e);
        } catch (IOException e) {
            Log.e(TAG, "writePackages: IO", e);
        }
        return null;
    }

}
