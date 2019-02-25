package com.example.feverfinder.questions;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a relevancy field - whether a question should be displayed or not
 * given the value of some field.
 */
public class Relevancy implements Parcelable {
    private String questionName;
    private String value;
    private boolean currentlyRelevant;

    /**
     * @param formatString a string in the format "selected(${**FIELD**}, '**VALUE**')"
     *                     for multiple choice questions or "${**FIELD**} = '**VALUE**'" for single
     *                     choice questions.
     */
    public Relevancy(String formatString) {
        currentlyRelevant = false;
        Pattern p;
        Matcher m;

        if (formatString.startsWith("$")) { // select_one
            p = Pattern.compile("\\$\\{([^}]+)\\} ?\\= ?\\'([^{]+)\\'"); // to match e.g. ${see_dump}='1'
            m = p.matcher(formatString);
            if (m.find()) {
                questionName = m.group(1);
                value = m.group(2);
            }
        } else { // select multiple
            p = Pattern.compile("selected\\(\\$\\{([^}]+)\\} ?\\, ?\\'([^{]+)\\'\\)"); // to match e.g. selected(${contact}, '7')
            m = p.matcher(formatString);
            if (m.find()) {
                questionName = m.group(1);
                value = m.group(2);
            }
        }
    }

    private Relevancy(Parcel in) {
        questionName = in.readString();
        value = in.readString();
        currentlyRelevant = in.readByte() != 0;
    }

    public static final Creator<Relevancy> CREATOR = new Creator<Relevancy>() {
        @Override
        public Relevancy createFromParcel(Parcel in) {
            return new Relevancy(in);
        }

        @Override
        public Relevancy[] newArray(int size) {
            return new Relevancy[size];
        }
    };

    public String getQuestionName() {
        return questionName;
    }

    /**
     * @param selectQuestion The question we are checking against
     * @return True if relevant based on the provided question
     */
    public boolean isRelevantTo(SelectQuestion selectQuestion) {
        if (selectQuestion.getName().equals(questionName)) {
            for (Option o : selectQuestion.getSelected()) {
                if (o.getName().equals(value)) {
                    currentlyRelevant = true;
                    return true;
                }
            }
            currentlyRelevant = false;
        }
        return currentlyRelevant;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionName);
        dest.writeString(value);
        dest.writeByte((byte) (currentlyRelevant ? 1 : 0));
    }
}
