package com.example.feverfinder.questions;

import android.os.Parcel;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TextQuestionTest {

    @Test
    public void test_text_question_is_parcelable() {
        TextQuestion textQuestion = new TextQuestion("Name", "Label", new LinkedList<Relevancy>());

        Parcel parcel = Parcel.obtain();
        textQuestion.writeToParcel(parcel, textQuestion.describeContents());
        parcel.setDataPosition(0);

        Question createdFromParcel = Question.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getName(), is("Name"));
        assertThat(createdFromParcel.getLabel(), is("Label"));
    }

}