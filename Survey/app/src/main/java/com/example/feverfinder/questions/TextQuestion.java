package com.example.feverfinder.questions;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.feverfinder.R;

public class TextQuestion extends Question {
    public TextQuestion(String name, String label) {
        super(name, label);
    }


    //TODO: don't want these requires APIs
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View generateView(Context context, ViewGroup root) {

        View view = context.getSystemService(LayoutInflater.class)
                .inflate(R.layout.text_question, root, false);
        TextInputLayout textInputLayout = view.findViewById(R.id.editTextLayout);
        textInputLayout.setHint(getLabel());
        return view;
    }
}
