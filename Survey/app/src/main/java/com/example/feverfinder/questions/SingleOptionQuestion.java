package com.example.feverfinder.questions;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.feverfinder.R;

import java.util.List;

public class SingleOptionQuestion extends Question {
    private List<Option> options;
    private String selected;

    public SingleOptionQuestion(String name, String label, List<Option> opts) {
        super(name, label);

        options = opts;
        selected = "unknown";
    }

    @Override
    public Object getJSONOutput() {
        return selected;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View generateView(Context context, ViewGroup root) {
        View view = context.getSystemService(LayoutInflater.class)
                .inflate(R.layout.select_question, root, false);

        TextView textView = view.findViewById(R.id.text_label);
        textView.setText(getLabel());

        RadioGroup radioGroup = view.findViewById(R.id.radio_container);
        for (final Option option : options) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(option.label);

            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d("LIST_CALLED", option.name);
                    selected = option.name;
                }
            });

            radioGroup.addView(radioButton);
        }

        return view;
    }
}
