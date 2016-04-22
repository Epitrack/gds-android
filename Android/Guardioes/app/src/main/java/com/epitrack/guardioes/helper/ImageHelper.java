package com.epitrack.guardioes.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class ImageHelper {

    private static final String TAG = ImageHelper.class.getSimpleName();

    public static final int ANGLE_90 = 90;
    public static final int ANGLE_180 = 180;
    public static final int ANGLE_270 = 270;

    public static final int QUALITY = 100;

    private ImageHelper() {

    }

    public static int getAngle(final String path) {

        try {

            final int orientation = new ExifInterface(path).getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                                                            ExifInterface.ORIENTATION_NORMAL);

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return ANGLE_90;

            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                return ANGLE_180;

            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                return ANGLE_270;
            }

        } catch (final IOException e) {
            Logger.logError(TAG, e.getMessage());
        }

        return 0;
    }

    public static Bitmap rotate(final String path, final float angle) {

        final Bitmap bitmap = BitmapFactory.decodeFile(path);

        return rotate(bitmap, angle);
    }

    public static Bitmap rotate(final Bitmap bitmap, final float angle) {

        final Matrix matrix = new Matrix();

        matrix.postRotate(angle);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static byte[] toByteArray(final Bitmap bitmap) {
        return toByteArray(bitmap, QUALITY);
    }

    public static byte[] toByteArray(final Bitmap bitmap, final int quality) {

        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, quality, output);

        bitmap.recycle();

        return output.toByteArray();
    }

    public static Bitmap scale(final String path, final int width, final int height) {

        final BitmapFactory.Options option = new BitmapFactory.Options();

        option.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, option);

        option.inJustDecodeBounds = false;

        option.inSampleSize = Math.min(option.outWidth / width, option.outHeight / height);

        return BitmapFactory.decodeFile(path, option);
    }
}
