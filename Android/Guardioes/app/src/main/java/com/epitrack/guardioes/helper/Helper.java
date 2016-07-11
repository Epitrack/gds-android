package com.epitrack.guardioes.helper;

import com.epitrack.guardioes.model.Country;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public final class Helper {

    private Helper() {

    }

    public static List<Country> loadCountry() {

        final List<Country> countryList = new LinkedList<>();

        int id = 0;

        for (final String country : Locale.getISOCountries()) {

            final String name = new Locale(Locale.getDefault().getDisplayLanguage(), country).getDisplayCountry();

            countryList.add(new Country(++id, country, name));
        }

        Collections.sort(countryList, new Comparator<Country>() {

            @Override
            public int compare(final Country country1, final Country country2) {
                return country1.getName().compareTo(country2.getName());
            }
        });

        return countryList;
    }
}
