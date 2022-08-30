package com.varsitycollege.vinyl_warehouse.Utils;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.content.Context;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Util {
    //Code Attribution
    //Link: https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
    //Author: GeeksforGeeks
    //End
    //Generate randomID
    public static String idGenerator() {
        Random random = new Random();
        //Get a random int from 99999999
        int idInt = random.nextInt(99999999);
        //Convert to Hex to String
        String hexadecimal = Integer.toHexString(idInt);
        return hexadecimal;
    }
    //Code Attribution
    //Link: https://stackoverflow.com/questions/5568874/how-to-extract-the-file-name-from-uri-returned-from-intent-action-get-content
    //Author: Stefan Haustein
    //End
    // Method to get the file name
    @SuppressLint("Range")
    public static String getFileName(Uri uri, Context context) {
        // String variable for the result
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    // Method to get the systems current date
    public static String getTodaysDate() {

        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        month = month +1;
        int day = currentDate.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);

    }

    // Method to concatenate a string
    public static String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }
    //Code Attribution
    //Link: https://stackoverflow.com/questions/9570237/android-check-internet-connection
    //Author: Seshu Vinay & N Droidev
    //End
    // Method to get the file name
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    // Method to check for internet access
    private static boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;
    }

    // Method to get the connection
    public static boolean getConnection(Context context) {
       // if (isNetworkAvailable(context)) {
            if (isInternetAvailable()) {
                return true;
            } else {
                return false;
            }
      ///  } else {
      //      return false;
      //  }
    }
}
