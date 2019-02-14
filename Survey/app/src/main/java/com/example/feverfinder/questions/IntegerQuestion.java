package com.example.feverfinder.questions;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.feverfinder.R;

public class IntegerQuestion extends Question {

    public IntegerQuestion(String name, String label, String relevant) {
        super(name, label, relevant);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View generateView(Context context, ViewGroup root) {
        View view = context.getSystemService(LayoutInflater.class).inflate(R.layout.integer_question, root, false);
        TextInputLayout editText = view.findViewById(R.id.editTextLayout);
        editText.setHint(getLabel());
        return view;
    }
}
