package com.example.feverfinder.questions;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.feverfinder.R;

import java.util.LinkedList;
import java.util.List;

public class YesNoQuestion extends Question {
    private List<Option> options;

    public YesNoQuestion(String name, String label) {
        super(name, label);

        options = new LinkedList<>();
        options.add(new Option("true", "Yes"));
        options.add(new Option("false", "No"));
    }

    @Override
    public Object getJSONOutput() {
        return new Boolean(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View generateView(Context context, ViewGroup root) {
        View view = context.getSystemService(LayoutInflater.class)
                .inflate(R.layout.select_question, root, false);

        TextView textView = view.findViewById(R.id.text_label);
        textView.setText(getLabel());

        RadioGroup radioGroup = view.findViewById(R.id.radio_container);
        for (Option option : options) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(option.label);
            radioGroup.addView(radioButton);
        }

        return view;
    }
}
