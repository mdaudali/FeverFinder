package com.example.feverfinder.questions;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

/**
 * name gives a unique ID for each question - useful when saving responses
 * label gives the text of each question
 */
abstract public class Question implements Serializable, SelectionChangedListener {
    private String name;
    private String label;
    private List<Relevancy> relevancies;
    private View view;

    public Question(String name, String label, List<Relevancy> relevancies) {
        this.name = name;
        this.label = label;
        this.relevancies = relevancies;
    }

    public List<Relevancy> getRelevancies() {
        return relevancies;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    /**
     * This method returns true if the question is relevant and should be displayed
     *
     * @param selectQuestion The SelectQuestion which this Question is dependant on
     * @return true if this question is relevant
     */
    public boolean isRelevant(SelectQuestion selectQuestion) {
        for (Relevancy relevancy : relevancies) {
            if (!relevancy.isRelevantTo(selectQuestion)) return false;
        }
        return true;
    }

    @Override
    public void onSelectionChanged(SelectQuestion selectQuestion) {
        Log.d("SELECTION_CHANGED", "started");
        if (isRelevant(selectQuestion)) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public abstract View generateView(Context context, ViewGroup root);

    protected void setView(View view) {
        this.view = view;
    }

}
