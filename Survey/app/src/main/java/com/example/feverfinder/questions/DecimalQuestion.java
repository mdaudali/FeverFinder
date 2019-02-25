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

public class DecimalQuestion extends Question implements TextWatcher {
    private Object content;

    public DecimalQuestion(String name, String label) {
        super(name, label);
        content = new Double(-1.0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View generateView(Context context, ViewGroup root) {
        View view = context.getSystemService(LayoutInflater.class)
                .inflate(R.layout.decimal_question, root, false);
        TextInputLayout textInputLayout = view.findViewById(R.id.editTextLayout);
        textInputLayout.setHint(getLabel());

        EditText editText = view.findViewById(R.id.editText);
        //TODO: setup editText to respond to content and also set up the listener

        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * This method is called when the text box is changed, and will update the content
     *
     * @param s The new content of the text box
     */
    @Override
    public void afterTextChanged(Editable s) {
        content = Double.valueOf(s.toString());
    }
}
