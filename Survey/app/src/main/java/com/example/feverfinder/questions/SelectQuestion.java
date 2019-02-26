package com.example.feverfinder.questions;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
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
    public static final int SELECT_TYPE_YES_NO = 0;
    public static final int SELECT_TYPE_MULTIPLE = 1;
    public static final int SELECT_TYPE_SINGLE = 2;

    public static final Parcelable.Creator<SelectQuestion> CREATOR
            = new Parcelable.Creator<SelectQuestion>() {
        public SelectQuestion createFromParcel(Parcel in) {
            return new SelectQuestion(in);
        }

        public SelectQuestion[] newArray(int size) {
            return new SelectQuestion[size];
        }
    };

    private List<Option> options;
    private int select_type;
    private List<Option> selected;
    private List<SelectionChangedListener> listeners;

    /**
     * @param name    is the name for storage
     * @param label   is the label for display
     * @param type    is whether you can select multiple options
     * @param options is the list of options
     */
    public SelectQuestion(String name, String label, List<Relevancy> relevant, int type, List<Option> options) {
        super(Question.TYPE_SELECT, name, label, relevant);
        this.select_type = type;
        this.options = options;
        selected = new LinkedList<>();
        listeners = new LinkedList<>();
    }

    protected SelectQuestion(Parcel in) {
        super(Question.TYPE_SELECT, in);
        options = new LinkedList<>();
        in.readTypedList(options, Option.CREATOR);
        select_type = in.readInt();
        selected = new LinkedList<>();
        in.readTypedList(selected, Option.CREATOR);
        listeners = new LinkedList<>();
    }

    public List<Option> getSelected() {
        return selected;
    }

    @Override
    public Object getJSONOutput() {
        Object output = "Unknown";
        if (select_type == SELECT_TYPE_YES_NO) {
            if (selected.size() == 1) output = selected.get(0).getName().equals("yes");
            else output = false; //TODO: should this be false
        } else if (select_type == SELECT_TYPE_MULTIPLE) {
            List<String> out = new LinkedList<>();
            for (Option option : selected) out.add(option.getName());
            output = out;
        } else if (select_type == SELECT_TYPE_SINGLE) {
            if (selected.size() == 1) output = selected.get(0).getName();
        }
        return output;
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
        View view;
        view = LayoutInflater.from(context)
                .inflate(R.layout.select_question, root, false);

        TextView textView = view.findViewById(R.id.text_label);
        textView.setText(getLabel());

        RadioGroup radioGroup = view.findViewById(R.id.radio_container);
        for (Option option : options) {
            if (select_type == SELECT_TYPE_MULTIPLE) {
                CheckBox checkBox = new CheckBox(context);
                checkBox.setChecked(isSelected(option));
                checkBox.setText(option.getLabel());
                checkBox.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                checkBox.setOnCheckedChangeListener(this);
                radioGroup.addView(checkBox);
            } else if (select_type == SELECT_TYPE_SINGLE || select_type == SELECT_TYPE_YES_NO) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setChecked(isSelected(option));
                radioButton.setText(option.getLabel());
                radioButton.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                radioButton.setOnCheckedChangeListener(this);
                radioGroup.addView(radioButton);
            }
        }
        setView(view);
        return view;
    }

    /**
     * Calculates whether a given Option is selected
     *
     * @param option the option to check
     * @return true if the option is selected
     */
    private boolean isSelected(Option option) {
        for (Option selectedOpt : selected) {
            if (selectedOpt.equals(option)) return true;
        }
        return false;
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //Ensure there is no concurrent access to the list of options
        if (isChecked) {
            for (Option option : options) {
                if (buttonView.getText().equals(option.getLabel())) {
                    selected.add(option);
                    break;
                }
            }
        } else {
            for (Option option : selected) {
                if (buttonView.getText().equals(option.getLabel())) {
                    selected.remove(option);
                    break;
                }
            }
        }

        //Notify the listeners
        for (SelectionChangedListener listener : listeners) {
            listener.onSelectionChanged(this);

        }
    }

    /**
     * Called to add a new listener which depends on the values stored in here
     *
     * @param listener Object to be notified if this Question's response changes
     */
    public void addSelectionChangedListener(SelectionChangedListener listener) {
        listeners.add(listener);
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
        dest.writeTypedList(options);
        dest.writeInt(select_type);
        dest.writeTypedList(selected);
    }
}
