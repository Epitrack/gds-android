package com.epitrack.guardioes.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class Helper {

    private Helper() {

    }

    public static List<String> loadCountry() {

        final List<String> countryList = new ArrayList<>();

        for (final String country : Locale.getISOCountries()) {

            countryList.add(new Locale(Locale.getDefault().getDisplayLanguage(), country).getDisplayCountry());
        }

        return countryList;
    }
}
