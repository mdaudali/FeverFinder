package com.example.feverfinder.questions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.io.Serializable;

/* name gives a unique ID for each question - useful when saving responses
 * label gives the test of each question*/
abstract public class Question implements Serializable {
    private String name;
    private String label;
    protected Object JSONOutput;

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

    public Object getJSONOutput() {
        return JSONOutput;
    }

    public abstract View generateView(Context context, ViewGroup root);
}
