package com.example.feverfinder.questions;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.feverfinder.R;

import org.json.simple.JSONArray;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiOptionQuestion extends Question {
    private List<Option> options;
    private Set<String> selected;

    public MultiOptionQuestion(String name, String label, List<Option> opts) {
        super(name, label);

        options = opts;
        selected = new HashSet<>();
    }

    @Override
    public Object getJSONOutput() {
        JSONArray jsonArray = new JSONArray();

        for(Object obj : selected.toArray()) {
            jsonArray.add(obj);
        }

        return jsonArray;
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
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(option.label);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        selected.add(option.label);
                    } else {
                        if(selected.contains(option.label)) {
                            selected.remove(option.label);
                        }
                    }
                }
            });

            radioGroup.addView(checkBox);
        }

        return view;
    }
}
