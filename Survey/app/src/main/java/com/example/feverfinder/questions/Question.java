package com.example.feverfinder.questions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public boolean waitUntilRelevant(List<Question> currentQuestions){
        // in the form ${<parentName>} = '<parentValue>' or selected(${<parentName>}, '<parentValue>')
        // ${<parentName>} = <parentName> is select_one
        // selected(${<parentName>}, '3') = <parentName> is select_multiple

        // name of the question q depends on
        String parentName = ""; String parentValue = "";

        // Pattern Matching over strings in relevant
        Pattern p; Matcher m;
        if (getRelevant().startsWith("$")){ // select_one
            p = Pattern.compile("\\$\\{([^}]+)\\} ?\\= ?\\'([^{]+)\\'"); // to match e.g. ${see_dump}='1'
            m = p.matcher(getRelevant());
            if (m.find()) {
                parentName = m.group(1);
                parentValue = m.group(2);
            }
        } else { // select multiple
            p = Pattern.compile("selected\\(\\$\\{([^}]+)\\} ?\\, ?\\'([^{]+)\\'\\)"); // to match e.g. selected(${contact}, '7')
            m = p.matcher(getRelevant());
            if (m.find()){
                parentName = m.group(1);
                parentValue = m.group(2);
            }
        }

        // question q depends on is always a SelectQuestion
        SelectQuestion parentSelectQuestion = null;

        // find question that q depends on
        for (Question parentQuestion: currentQuestions){
            if (parentQuestion.getName().equals(parentName)){
                parentSelectQuestion = (SelectQuestion) parentQuestion;
                break;
            }
        }
        // see if answer of question allows q to be visible
        for (Option o: parentSelectQuestion.getSelected()){
            if (o.getName().equals(parentValue)){
                return true;
            }
        }
        return false;
    }

    public abstract View generateView(Context context, ViewGroup root);

}
