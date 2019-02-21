package com.example.feverfinder.questions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a relevancy field - whether a question should be displayed or not
 * given the value of some field.
 */
public class Relevancy {
    private String questionName;
    private String value;
    private boolean currentlyRelevant;

    /**
     * @param formatString a string in the format "selected(${**FIELD**}, '**VALUE**')"
     *                     for multiple choice questions or "${**FIELD**} = '**VALUE**'" for single
     *                     choice questions.
     */
    public Relevancy(String formatString) {
        currentlyRelevant = false;
        Pattern p;
        Matcher m;

        if (formatString.startsWith("$")) { // select_one
            p = Pattern.compile("\\$\\{([^}]+)\\} ?\\= ?\\'([^{]+)\\'"); // to match e.g. ${see_dump}='1'
            m = p.matcher(formatString);
            if (m.find()) {
                questionName = m.group(1);
                value = m.group(2);
            }
        } else { // select multiple
            p = Pattern.compile("selected\\(\\$\\{([^}]+)\\} ?\\, ?\\'([^{]+)\\'\\)"); // to match e.g. selected(${contact}, '7')
            m = p.matcher(formatString);
            if (m.find()) {
                questionName = m.group(1);
                value = m.group(2);
            }
        }
    }

    public String getQuestionName() {
        return questionName;
    }

    /**
     * @param selectQuestion The question we are checking against
     * @return True if relevant based on the provided question
     */
    public boolean isRelevantTo(SelectQuestion selectQuestion) {
        if (selectQuestion.getName().equals(questionName)) {
            for (Option o : selectQuestion.getSelected()) {
                if (o.getName().equals(value)) {
                    currentlyRelevant = true;
                    return true;
                }
            }
            currentlyRelevant = false;
        }
        return currentlyRelevant;
    }
}
