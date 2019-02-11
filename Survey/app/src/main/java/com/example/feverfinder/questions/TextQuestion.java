package com.example.feverfinder.questions;

import android.view.View;

public class TextQuestion extends Question {
    public TextQuestion(String name, String label) {
        super(name, label);
    }

    @Override
    View generateView() {
        return null;
    }
}
