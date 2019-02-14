package com.example.feverfinder.questions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

/* name gives a unique ID for each question - useful when saving responses
 * label gives the text of each question
 *  */
abstract public class Question implements Serializable {
    private String name;
    private String label;
    private String relevant;

    public Question(String name, String label, String relevant){
        this.name = name;
        this.label = label;
        this.relevant = relevant;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getRelevant() {
        return relevant;
    }

    public abstract View generateView(Context context, ViewGroup root);

}
