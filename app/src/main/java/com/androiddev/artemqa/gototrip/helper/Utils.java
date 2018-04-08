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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


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

    public static String timestampToDateMessage(Long timestamp) {

        Locale ruLocale = new Locale("ru");
        SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MM-yyyy", ruLocale);
        SimpleDateFormat mgFormat = new SimpleDateFormat("HH:mm", ruLocale);

        Date timeStampRemoveTime = dateRemoveTime(new Date(timestamp));
        Date currentTimeRemoveTime = dateRemoveTime(new Date());

        if (timeStampRemoveTime.equals(currentTimeRemoveTime)) {
            return mgFormat.format(new Date(timestamp));
        } else {
            return dmyFormat.format(new Date(timestamp));
        }
    }

    private static Date dateRemoveTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static String getInterlocutorId(Map<String, Boolean> members, String currentUserId) {
        if (members.size()>1){
            members.remove(currentUserId);
            Set<String> set = members.keySet();
            return new ArrayList<>(set).get(0);
        }
        else {
            Set<String> set = members.keySet();
            return new ArrayList<>(set).get(0);
        }
    }
}