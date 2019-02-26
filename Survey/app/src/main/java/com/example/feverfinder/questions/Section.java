package com.example.feverfinder.questions;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.feverfinder.SurveySection;

import java.util.ArrayList;
import java.util.List;

public class Section implements Parcelable {
    public static final Creator<Section> CREATOR = new Creator<Section>() {
        @Override
        public Section createFromParcel(Parcel in) {
            return new Section(in);
        }

        @Override
        public Section[] newArray(int size) {
            return new Section[size];
        }
    };
    private String name;
    private List<Question> questions;


    /**
     * Section constructor
     *
     * @param name      Name of this section
     * @param questions List of the questions within the section
     */
    public Section(String name, List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

    protected Section(Parcel in) {
        name = in.readString();
        Log.d("Help", name);
        questions = new ArrayList<>();
        in.readTypedList(questions, Question.CREATOR);
    }

    public String getName() {
        return name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public SurveySection getSurveySectionFragment() {
        SurveySection surveySectionFragment = new SurveySection();
        Bundle args = new Bundle();
        args.putParcelable(SurveySection.ARG_SECTION, this);
        surveySectionFragment.setArguments(args);
        return surveySectionFragment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(questions);
    }
}
