package com.example.feverfinder.questions;

import android.view.View;

/* name gives a unique ID for each question - useful when saving responses
 * label gives the test of each question*/
abstract public class Question {
    private String name;
    private String label;

    public Question(String name, String label){
        this.name = name;
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    abstract View generateView();
}
