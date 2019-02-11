package com.example.feverfinder.questions;

import android.view.View;

import java.util.List;

public class SelectQuestion extends Question {
    private List<Option> options;
    private boolean multiple;

    /**
     * @param name is the name for storage
     * @param label is the label for display
     * @param multiple is whether you can select multiple options
     * @param options is the list of options
     */
    public SelectQuestion(String name, String label, boolean multiple, List<Option> options) {
        super(name, label);
        this.multiple = multiple;
        this.options = options;
    }

    @Override
    View generateView() {
        //TODO: this
        return null;
    }
}
