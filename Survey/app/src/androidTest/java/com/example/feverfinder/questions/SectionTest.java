package com.example.feverfinder.questions;

import android.os.Parcel;

import junit.framework.AssertionFailedError;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SectionTest {

    @Test
    public void test_section_is_parcelable() {
        TextQuestion textQuestion = new TextQuestion("Text", "Label", new LinkedList<Relevancy>());

        LinkedList<Question> questions = new LinkedList<>();
        questions.add(textQuestion);
        questions.add(new DecimalQuestion("Decimal", "Label", new LinkedList<Relevancy>()));
        questions.add(new IntegerQuestion("Integer", "Label", new LinkedList<Relevancy>()));
        LinkedList<Option> options = new LinkedList<>();
        options.add(new Option("Option", "Label"));
        questions.add(new SelectQuestion("Select", "label", new LinkedList<Relevancy>(), true, options));
        questions.add(new IntegerQuestion("Integer", "Label", new LinkedList<Relevancy>()));
        try {
            questions.add(new RangeQuestion("Range", "Label", new LinkedList<Relevancy>(), "start=0 end=5 step=1"));
        }
        catch (ParameterParseException e) {
            throw new AssertionFailedError();
        }


        Section section = new Section("Name", questions);

        Parcel parcel = Parcel.obtain();
        section.writeToParcel(parcel, section.describeContents());
        parcel.setDataPosition(0);

        Section createdFromParcel = Section.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getName(), is("Name"));
        Assert.assertEquals(createdFromParcel.getQuestions().get(0).getName(), "Text");
        Assert.assertEquals(createdFromParcel.getQuestions().get(1).getName(), "Decimal");
        Assert.assertEquals(createdFromParcel.getQuestions().get(2).getName(), "Integer");
        Assert.assertEquals(createdFromParcel.getQuestions().get(3).getName(), "Select");
        Assert.assertEquals(createdFromParcel.getQuestions().get(4).getName(), "Integer");
        Assert.assertEquals(createdFromParcel.getQuestions().get(5).getName(), "Range");



    }

}