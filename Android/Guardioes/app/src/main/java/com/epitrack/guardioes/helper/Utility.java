package com.epitrack.guardioes.helper;


import com.afollestad.materialdialogs.MaterialDialog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Igor Morais
 */
public final class Utility {

    static MaterialDialog.Builder builder = null;
    private static final String TAG = Utility.class.getSimpleName();

    private Utility() {

    }

    public static <T> void print(final T entity) {

        try {

            Logger.logDebug(TAG, new ObjectMapper().writeValueAsString(entity));

        } catch (JsonProcessingException e) {
            Logger.logError(TAG, e.getMessage());
        }
    }
}
