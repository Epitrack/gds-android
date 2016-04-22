package com.epitrack.guardioes.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class FileHandler {

    private static final String TAG = FileHandler.class.getSimpleName();

    private static final int EMPTY = 0;

    private static final String DIRECTORY = "Temp";

    public static final String PATH_INTERNAL = Environment.getExternalStorageDirectory().getPath() + "/" + DIRECTORY;

    public static final String PATH_TEMP = Environment.getExternalStorageDirectory().getPath() + "/" + Constants.DIRECTORY_TEMP;

    public boolean createDirectory(final String name) {

        final File directory = new File(Environment.getExternalStorageDirectory(), name);

        return directory.exists() || directory.mkdir();
    }

    public boolean cleanDirectory(final String name) {

        final File directory = new File(Environment.getExternalStorageDirectory(), name);

        if (directory.isDirectory()) {

            if (directory.length() == EMPTY) {
                return true;

            } else {

                try {

                    FileUtils.cleanDirectory(directory);

                } catch (final IOException e) {
                    Logger.logError(TAG, e.getMessage());

                    return false;
                }
            }
        }

        return true;
    }

    public File loadFile(final String name, final String extension) {
        return new File(Environment.getExternalStorageDirectory() + "/" + Constants.DIRECTORY_TEMP, name + extension);
    }

    public File loadFile(final String name, final Extension extension) {
        return new File(Environment.getExternalStorageDirectory() + "/" + Constants.DIRECTORY_TEMP, name + extension.getExtension());
    }

    public boolean moveFile(final File source, final File destination) {

        try {

            if (destination.exists() && destination.delete()) {
                Logger.logDebug(TAG, "The file has deleted.");
            }

            FileUtils.moveFile(source, destination);

        } catch (final IOException e) {
            Logger.logError(TAG, e.getMessage());

            return false;
        }

        return true;
    }

    public boolean save(final String name, final Extension extension, final Bitmap bitmap) {

        try {

            final File file = loadFile(name, extension);

            final FileOutputStream stream = new FileOutputStream(file);

            stream.write(ImageHelper.toByteArray(bitmap));

            stream.close();

        } catch (final IOException e) {
            Logger.logError(TAG, e.getMessage());

            return false;
        }

        return true;
    }

    public boolean write(final File file, final String content) {

        try {

            FileUtils.writeStringToFile(file, content);

        } catch (final IOException e) {
            return false;
        }

        return true;
    }

    public <T> boolean write(final File file, final T entity) {
        return write(file, toJson(entity));
    }

    private <T> String toJson(final T entity) {

        try {

            return new ObjectMapper().writeValueAsString(entity);

        } catch (final JsonProcessingException e) {
            Logger.logError(TAG, e.getMessage());
        }

        return null;
    }

    public static class Internal {

        private final Context context;

        public Internal(final Context context) {
            this.context = context;
        }

        public boolean createDirectory(final String name) {

            final File directory = new File(context.getFilesDir(), name);

            return createDirectory(directory);
        }

        public boolean createDirectory(final File directory) {
            return directory.exists() || directory.mkdirs();
        }

        public boolean deleteDirectory(final String name) {
            return new File(context.getFilesDir(), name).delete();
        }

        public File loadTempFile(final String name, final Extension extension) {
            return new File(PATH_INTERNAL, name + extension.getExtension());
        }

        public File loadFile(final String name, final Extension extension) {
            return new File(PATH_INTERNAL, name + extension.getExtension());
        }

        public File loadFile(final String directory, final String name, final Extension extension) {
            return new File(PATH_INTERNAL + "/" + directory, name + extension.getExtension());
        }

        public File loadFile(final File directory, final String name, final Extension extension) {
            return new File(directory, name + extension.getExtension());
        }

        public boolean save(final String name, final Extension extension, final Bitmap bitmap) {

            try {

                final File file = loadFile(name, extension);

                final FileOutputStream stream = new FileOutputStream(file);

                stream.write(ImageHelper.toByteArray(bitmap));

                stream.close();

            } catch (final IOException e) {
                Logger.logError(TAG, e.getMessage());

                return false;
            }

            return true;
        }
    }
}
