package com.example.feverfinder.questions;

import java.util.ArrayList;
import java.util.List;

public class Section {
    private String name;
    private List<Question> questions;

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
}
