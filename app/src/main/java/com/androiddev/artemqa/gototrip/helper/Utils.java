package com.androiddev.artemqa.gototrip.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Created by artemqa on 16.03.2018.
 */

public class Utils {

    public static byte[] compressPhoto(Uri photoUri, Context context) {
        ByteArrayOutputStream bos = null;
        try {
            Bitmap originalPhotoBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoUri);
            bos = new ByteArrayOutputStream();
            originalPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    public static String timestampToDateMessage( Long timestamp){

            Locale ruLocale = new Locale("ru");
            SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MM-yyyy",ruLocale);
            SimpleDateFormat mgFormat = new SimpleDateFormat("HH:mm",ruLocale);

            Date timeStampRemoveTime = dateRemoveTime(new Date(timestamp));
            Date currentTimeRemoveTime = dateRemoveTime(new Date());

            if(timeStampRemoveTime.equals(currentTimeRemoveTime)){
                return mgFormat.format(new Date(timestamp));
            }
            else {
                return dmyFormat.format(new Date(timestamp));
            }
    }

    private static Date dateRemoveTime(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}