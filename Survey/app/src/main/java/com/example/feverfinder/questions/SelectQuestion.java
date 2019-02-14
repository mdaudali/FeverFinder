package com.example.feverfinder.questions;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.feverfinder.R;

import java.util.LinkedList;
import java.util.List;

public class SelectQuestion extends Question implements CompoundButton.OnCheckedChangeListener {
    private List<Option> options;
    private boolean multiple;

    public List<Option> getSelected() {
        return selected;
    }

    private List<Option> selected;
    private List<SelectionChangedListener> listeners;

    /**
     * @param name is the name for storage
     * @param label is the label for display
     * @param multiple is whether you can select multiple options
     * @param options is the list of options
     */
    public SelectQuestion(String name, String label, String relevant, boolean multiple, List<Option> options) {
        super(name, label, relevant);
        this.multiple = multiple;
        this.options = options;
        selected = new LinkedList<>();
        listeners = new LinkedList<>();
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
                checkBox.setChecked(false);
                checkBox.setText(option.label);
                checkBox.setOnCheckedChangeListener(this);
                radioGroup.addView(checkBox);
            } else {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setChecked(false);
                radioButton.setText(option.label);
                radioButton.setOnCheckedChangeListener(this);
                radioGroup.addView(radioButton);
            }
        }
        setView(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            for (Option option : options) {
                if (buttonView.getText().equals(option.label)) {
                    selected.add(option);
                    break;
                }
            }
        } else {
            for (Option option : selected) {
                if (buttonView.getText().equals(option.label)) {
                    selected.remove(option);
                }
            }
        }

        //Notify the listeners
        for (SelectionChangedListener listener : listeners) {
            listener.onSelectionChanged(this);
        }
    }

    public void addSelectionChangedListener(SelectionChangedListener listener) {
        listeners.add(listener);
    }
}
