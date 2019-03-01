package com.example.feverfinder.questions;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * name gives a unique ID for each question - useful when saving responses
 * label gives the text of each question
 */
public abstract class Question implements Parcelable, SelectionChangedListener {
    public static final int TYPE_DECIMAL = 0;
    public static final int TYPE_INTEGER = 1;
    public static final int TYPE_RANGE = 2;
    public static final int TYPE_TEXT = 3;
    public static final int TYPE_SELECT = 4;

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            int type = in.readInt();
            switch (type) {
                case TYPE_DECIMAL:
                    return new DecimalQuestion(in);
                case TYPE_INTEGER:
                    return new IntegerQuestion(in);
                case TYPE_RANGE:
                    return new RangeQuestion(in);
                case TYPE_TEXT:
                    return new TextQuestion(in);
                case TYPE_SELECT:
                    return new SelectQuestion(in);
                default:
                    return null;
            }
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
    private int type;
    private String name;
    private String label;
    private int id;
    private List<Relevancy> relevancies;
    private View view;

    public Question(int type, String name, String label, List<Relevancy> relevancies) {
        this.type = type;
        this.name = name;
        this.label = label;
        this.id = View.generateViewId();
        this.relevancies = relevancies;
    }

    protected Question(int type, Parcel in) {
        this.type = type;
        this.name = in.readString();
        this.label = in.readString();
        this.id = in.readInt();

        this.relevancies = new ArrayList<>();
        in.readTypedList(relevancies, Relevancy.CREATOR);
    }

    public List<Relevancy> getRelevancies() {
        return relevancies;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public int getId() {
        return id;
    }

    public abstract Object getJSONOutput();

    /**
     * This method returns true if the question is relevant and should be displayed
     *
     * @param selectQuestion The SelectQuestion which this Question is dependant on
     * @return true if this question is relevant
     */
    public boolean isRelevant(SelectQuestion selectQuestion) {
        for (Relevancy relevancy : relevancies) {
            if (!relevancy.isRelevantTo(selectQuestion)) return false;
        }
        return true;
    }

    /**
     * Displays or hides the Question based on whether this Question is relevant to the
     * SelectQuestion
     *
     * @param selectQuestion The Question which value may have changed
     */
    @Override
    public void onSelectionChanged(SelectQuestion selectQuestion) {
        if (isRelevant(selectQuestion)) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public abstract View generateView(Context context, ViewGroup root);

    protected void setView(View view) {
        this.view = view;
    }

    protected View getView() {
        return view;
    }

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeInt(type);
        dest.writeString(name);
        dest.writeString(label);
        dest.writeInt(id);
        dest.writeTypedList(relevancies);
    }
}
