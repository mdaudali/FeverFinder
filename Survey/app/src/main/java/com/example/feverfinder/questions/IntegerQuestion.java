package com.example.feverfinder.questions;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.feverfinder.R;

import java.util.List;

public class IntegerQuestion extends Question implements TextWatcher {
    private int content;

    public IntegerQuestion(String name, String label, List<Relevancy> relevant) {
        super(name, label, relevant);
    }

    @Override
    public View generateView(Context context, ViewGroup root) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.integer_question, root, false);
        TextInputLayout editText = view.findViewById(R.id.editTextLayout);
        editText.setHint(getLabel());

        setView(view);
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    //TODO: javadoc
    @Override
    public void afterTextChanged(Editable s) {
        content = Integer.parseInt(s.toString());
    }
}

