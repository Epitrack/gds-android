package com.epitrack.guardioes.manager;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.Extension;
import com.epitrack.guardioes.helper.FileHandler;

/**
 * @author Igor Morais
 */
public class AsyncSave extends AsyncTask<Void, Void, String> {

    private static final String PATH = Environment.getExternalStorageDirectory().getPath();

    private final String name;
    private final Extension extension;
    private final Bitmap bitmap;

    private AsyncListener<String> listener;

    public AsyncSave(final String name, final Extension extension, final Bitmap bitmap) {

        this.name = name;
        this.extension = extension;
        this.bitmap = bitmap;
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
    }

    @Override
    protected String doInBackground(final Void... voidArray) {

        if (new FileHandler().save(name, extension, bitmap)) {

            return PATH + "/" + Constants.DIRECTORY_TEMP + "/" + name + extension.getExtension();
        }

        return null;
    }

    @Override
    protected void onPostExecute(final String path) {
        listener.onEnd(path);
    }

    public AsyncSave setListener(final AsyncListener<String> listener) {
        this.listener = listener;

        return this;
    }
}
