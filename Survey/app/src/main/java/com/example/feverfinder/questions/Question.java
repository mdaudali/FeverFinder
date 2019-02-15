package com.example.feverfinder.questions;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* name gives a unique ID for each question - useful when saving responses
 * label gives the text of each question
 *  */
abstract public class Question implements Serializable, SelectionChangedListener {
    private String name;
    private String label;
    private String relevant;
    private View view;

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

    /**
     * This method returns true if the question is relevant and should be displayed
     *
     * @param selectQuestion The SelectQuestion which this Question is dependant on
     * @return true if this question is relevant
     */
    public boolean isRelevant(SelectQuestion selectQuestion) {
        // value of the question q depends on
        String parentValue = "";

        // Pattern Matching over strings in relevant
        //TODO: move this to constructor if it works
        Pattern p; Matcher m;
        if (getRelevant().startsWith("$")){ // select_one
            p = Pattern.compile("\\$\\{([^}]+)\\} ?\\= ?\\'([^{]+)\\'"); // to match e.g. ${see_dump}='1'
            m = p.matcher(getRelevant());
            if (m.find()) {
                parentValue = m.group(2);
            }
        } else { // select multiple
            p = Pattern.compile("selected\\(\\$\\{([^}]+)\\} ?\\, ?\\'([^{]+)\\'\\)"); // to match e.g. selected(${contact}, '7')
            m = p.matcher(getRelevant());
            if (m.find()){
                parentValue = m.group(2);
            }
        }

        // see if answer of question allows q to be visible
        for (Option o: selectQuestion.getSelected()){
            if (o.getName().equals(parentValue)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onSelectionChanged(SelectQuestion selectQuestion) {
        Log.d("SELECTION_CHANGED", "started");
        if (isRelevant(selectQuestion)) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public abstract View generateView(Context context, ViewGroup root);

    protected void setView(View view) {
        this.view = view;
    }

}
