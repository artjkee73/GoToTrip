package com.androiddev.artemqa.gototrip.helper;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.GlideApp;
import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.modules.editProfile.EditProfileContract;
import com.androiddev.artemqa.gototrip.modules.editProfile.view.EditProfileActivity;
import com.androiddev.artemqa.gototrip.modules.viewPost.view.ViewPostActivity;
import com.developers.imagezipper.ImageZipper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by artemqa on 16.03.2018.
 */

public class Utils {

    public static byte[] compressPhotoThumbnail(Uri photoUri, Context context) {
        Bitmap compressBitmap = null;
        try {
            compressBitmap = new ImageZipper(context)
                    .setQuality(15)
                    .setMaxWidth(100)
                    .setMaxHeight(100)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .compressToBitmap(new File(photoUri.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressBitmap.compress(Bitmap.CompressFormat.WEBP, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] compressPhotoOriginal(Uri photoUri, Context context) {
        Bitmap compressBitmap = null;
        try {
            compressBitmap = new ImageZipper(context)
                    .setQuality(50)
                    .setMaxWidth(640)
                    .setMaxHeight(640)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .compressToBitmap(new File(photoUri.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressBitmap.compress(Bitmap.CompressFormat.WEBP, 100, baos);
        return baos.toByteArray();
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
        if (members.size() > 1) {
            members.remove(currentUserId);
            Set<String> set = members.keySet();
            return new ArrayList<>(set).get(0);
        } else {
            Set<String> set = members.keySet();
            return new ArrayList<>(set).get(0);
        }
    }

    public static void loadImage(Context context, String urlImage, ImageView setView) {
        StorageReference refUrlImage = FirebaseStorage.getInstance().getReferenceFromUrl(urlImage);
        GlideApp.with(context)
                .load(refUrlImage)
                .error(R.color.black_overlay)
                .thumbnail(0.2f)
                .into(setView);
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static Uri getUriStringFromResource(Context context, int resourceId) {
        Resources resources = context.getResources();
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(resourceId))
                .appendPath(resources.getResourceTypeName(resourceId))
                .appendPath(resources.getResourceEntryName(resourceId))
                .build();
        return uri;
    }

    public static String millisTimeToReadbleString(Long currentTimeMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Date resultDate = new Date(currentTimeMillis);
        return sdf.format(resultDate);
    }

    public static String getPath(Context context, Uri uri) {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static Icon iconFromResource(Context context, int idResource) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.location_circle);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bm, 50, 50, false);
        return IconFactory.getInstance(context).fromBitmap(resizedBitmap);
    }

    public static void stressTesting(int count) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refBaseDatabase = database.getReference();
        DatabaseReference refBasePost = refBaseDatabase.child(Constants.POSTS_LOCATION);
        List<Post> listPosts = new ArrayList<>();
        HashMap<String,Boolean> postPhoto = new HashMap<>();
        postPhoto.put("-LC7RABMCdiEPDZ8lPmb",true);
        for (int i = 0; count < i; i++) {
        listPosts.add(new Post("9176487572878987645678653123"+i,"Заголовок",
                "91sfyU7cb2dTyYypmW8OxyOxCvJ3","Витя Головин",
                "https://firebasestorage.googleapis.com/v0/b/artjkee-gototrip.appspot.com" +
                        "/o/users%2F91sfyU7cb2dTyYypmW8OxyOxCvJ3%2Fimages%2Forig_avatar_91sfyU7cb2dTyYypmW8OxyOxCvJ3" +
                        ".jpeg?alt=media&token=65484e2c-cc94-49c8-98b8-76c616fe9978", "Тесовый пост " + i ,
                Long.valueOf(12334334),postPhoto,45.324234,33.324234 ));
        }

        for(Post currentPost: listPosts){
            refBasePost.child(currentPost.getPostId()).setValue(currentPost);
        }
    }

}