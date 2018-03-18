package com.androiddev.artemqa.gototrip.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by artemqa on 16.03.2018.
 */

public class Utils {

    public static byte[] compressPhoto(Uri photoUri, Context context) {
        ByteArrayOutputStream bos = null;
        try {
            Bitmap originalPhotoBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoUri);
            bos = new ByteArrayOutputStream();
            originalPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

}