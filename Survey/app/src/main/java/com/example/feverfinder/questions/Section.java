package com.example.feverfinder.questions;

import android.os.Bundle;

import com.example.feverfinder.SurveySection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Section {
    private String name;
    private List<Question> questions;
    private SurveySection surveySectionFragment = null;

    /**
     * Section constructor
     * @param name Name of this section
     * @param questions List of the questions within the section
     */
    public Section(String name, List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public SurveySection getSurveySectionFragment() {
        if (surveySectionFragment == null) {
            surveySectionFragment = new SurveySection();
            Bundle args = new Bundle();
            //args.putSerializable(SurveySection.ARG_QUESTIONS, (Serializable) getQuestions());
            args.putString(SurveySection.ARG_TITLE, getName());
            surveySectionFragment.setArguments(args);
        }
        surveySectionFragment.setmQuestions(getQuestions());
        return surveySectionFragment;
    }
}
