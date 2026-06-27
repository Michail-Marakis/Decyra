package com.example.decyra.extras;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * The type Profile image manager.
 */
public class ProfileImageManager {

    private static final String FOLDER_NAME = "profile_images";

    /**
     * Save bitmap string.
     *
     * @param context the context
     * @param userId  the user id
     * @param bitmap  the bitmap
     * @return the string
     */
    public static String saveBitmap(Context context, String userId, Bitmap bitmap) {
        try {
            File folder = new File(context.getFilesDir(), FOLDER_NAME);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File imageFile = new File(folder, userId + ".jpg");

            FileOutputStream fos = new FileOutputStream(imageFile, false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();

            return imageFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Load bitmap bitmap.
     *
     * @param context the context
     * @param userId  the user id
     * @return the bitmap
     */
    public static Bitmap loadBitmap(Context context, String userId) {
        try {
            File folder = new File(context.getFilesDir(), FOLDER_NAME);
            File imageFile = new File(folder, userId + ".jpg");

            if (imageFile.exists()) {
                return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}