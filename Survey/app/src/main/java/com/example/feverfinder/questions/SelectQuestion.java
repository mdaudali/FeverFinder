package com.example.feverfinder.questions;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.feverfinder.R;

import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.util.List;

public class SelectQuestion extends Question {
    private List<Option> options;
    private boolean multiple;

    /**
     * @param name is the name for storage
     * @param label is the label for display
     * @param multiple is whether you can select multiple options
     * @param options is the list of options
     */
    public SelectQuestion(String name, String label, boolean multiple, List<Option> options) {
        super(name, label);
        this.multiple = multiple;
        this.options = options;

        if(multiple) {
            JSONArray jsonArr = new JSONArray();
            for (Option opt : options) {
                jsonArr.add(opt.label);
            }

            JSONOutput = jsonArr.toJSONString();
        } else {
            if(options.get(0).label.equals("Yes")) JSONOutput = "true";
            else JSONOutput = options.get(0).label;
        }
        Log.d("JSONARR", (String)JSONOutput);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View generateView(Context context, ViewGroup root) {
        View view;
        view = context.getSystemService(LayoutInflater.class)
                .inflate(R.layout.select_question, root, false);

        TextView textView = view.findViewById(R.id.text_label);
        textView.setText(getLabel());

        RadioGroup radioGroup = view.findViewById(R.id.radio_container);
        for (Option option : options) {
            if (multiple) {
                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(option.label);
                radioGroup.addView(checkBox);
            } else {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(option.label);
                radioGroup.addView(radioButton);
            }
        }
        return view;
    }
}
