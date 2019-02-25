package com.example.feverfinder.questions;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.feverfinder.R;

public class IntegerQuestion extends Question implements TextWatcher {

    public IntegerQuestion(String name, String label) {
        super(name, label);
        JSONOutput = new Integer(-1);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View generateView(Context context, ViewGroup root) {
        View view = context.getSystemService(LayoutInflater.class).inflate(R.layout.integer_question, root, false);
        TextInputLayout editText = view.findViewById(R.id.editTextLayout);
        editText.setHint(getLabel());
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        JSONOutput = Integer.valueOf(s.toString());
    }
}
