package com.epitrack.guardioes.helper;

import com.epitrack.guardioes.model.Country;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class Helper {

    private Helper() {

    }

    public static List<Country> loadCountry() {

        final List<Country> countryList = new ArrayList<>();

        for (final String country : Locale.getISOCountries()) {

            final String name = new Locale(Locale.getDefault().getDisplayLanguage(), country).getDisplayCountry();

            countryList.add(new Country(country, name));
        }

        return countryList;
    }
}
