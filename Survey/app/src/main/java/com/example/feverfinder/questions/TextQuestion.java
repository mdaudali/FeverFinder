package com.example.feverfinder.questions;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.feverfinder.R;

import java.util.List;

public class TextQuestion extends Question implements TextWatcher {
    public static final Parcelable.Creator<TextQuestion> CREATOR
            = new Parcelable.Creator<TextQuestion>() {
        public TextQuestion createFromParcel(Parcel in) {
            return new TextQuestion(in);
        }

        public TextQuestion[] newArray(int size) {
            return new TextQuestion[size];
        }
    };
    private String content;

    public TextQuestion(String name, String label, List<Relevancy> relevant) {
        super(Question.TYPE_TEXT, name, label, relevant);
        content = "";
    }

    protected TextQuestion(Parcel in) {
        super(Question.TYPE_TEXT, in);
        content = in.readString();
    }

    @Override
    public Object getJSONOutput() {
        if (!content.equals("")) return content;
        return "unknown";
    }

    /**
     * Generate a View which displays the question
     *
     * @param context Context used to set up the View correctly
     * @param root    The parent this View will be inserted in
     * @return The generated View
     */
    @Override
    public View generateView(Context context, ViewGroup root) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.text_question, root, false);
        TextInputLayout textInputLayout = view.findViewById(R.id.editTextLayout);
        textInputLayout.setHint(getLabel());

        EditText editText = view.findViewById(R.id.editText);
        editText.setId(getId());

        editText.addTextChangedListener(this);

        setView(view);
        return view;
    }

    @Override
    public void updateView() {
        View view = getView();
        //EditText editText = view.findViewById(R.id.editText);
        //editText.setText(content);
        //editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        content = s.toString();
    }

    /**
     * Flatten this Question in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(content);
    }
}
