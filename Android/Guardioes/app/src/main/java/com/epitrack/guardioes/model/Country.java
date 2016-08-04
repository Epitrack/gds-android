package com.epitrack.guardioes.model;

public final class Country {

    private int id;
    private String code;
    private String name;
    private String name_en;

    public Country(final int id, final String code, final String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Country(final int id, final String code, final String name,final String name_en) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.name_en=name_en;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(final String name_en) {
        this.name_en = name_en;
    }
}
