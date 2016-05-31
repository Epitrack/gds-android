package com.epitrack.guardioes.view.game.model;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String title;
    private List<Option> optionList = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void add(final Option option) {
        optionList.add(option);
    }

    public void remove(final Option option) {
        optionList.remove(option);
    }

    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(final List<Option> optionList) {
        this.optionList = optionList;
    }
}
