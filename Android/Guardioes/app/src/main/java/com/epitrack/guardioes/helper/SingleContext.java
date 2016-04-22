package com.epitrack.guardioes.helper;

import android.content.Context;

/**
 * @author Miqueias Lopes
 */
public class SingleContext {

    private static SingleContext instance;
    private Context context;

    private SingleContext() {

    }

    public static synchronized SingleContext getInstance() {

        if (instance == null) {
            instance = new SingleContext();
            return instance;
        } else {
            return instance;
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
