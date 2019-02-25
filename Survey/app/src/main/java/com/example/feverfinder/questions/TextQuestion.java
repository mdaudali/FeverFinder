package com.example.feverfinder.questions;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.feverfinder.R;

public class TextQuestion extends Question implements TextWatcher {
    private String content;

    public TextQuestion(String name, String label) {
        super(name, label);
    }

    @Override
    public Object getJSONOutput() {
        return content;
    }

    //TODO: don't want these requires APIs
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View generateView(Context context, ViewGroup root) {
        View view = context.getSystemService(LayoutInflater.class)
                .inflate(R.layout.text_question, root, false);
        TextInputLayout textInputLayout = view.findViewById(R.id.editTextLayout);
        textInputLayout.setHint(getLabel());

        EditText editText = view.findViewById(R.id.editText);
        editText.setText(content);
        editText.addTextChangedListener(this);

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
        // If the content was cleared, then set output to 'unknown'.
        // Otherwise set it to the value provided.
        Log.d("TXT_CHANGE", s.toString());
        content = s.toString().equals("") ? "unknown" : s.toString();
    }
}
