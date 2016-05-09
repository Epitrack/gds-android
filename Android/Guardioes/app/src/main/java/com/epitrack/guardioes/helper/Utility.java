package com.epitrack.guardioes.helper;


import com.afollestad.materialdialogs.MaterialDialog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

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

    public static List<String> toList(final JSONArray jsonArray) {

        final List<String> tagList = new ArrayList<>(jsonArray.length());

        for(int i = 0; i < jsonArray.length(); i++) {

            try {

                tagList.add(jsonArray.getString(i));

            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }

        return tagList;
    }
}
