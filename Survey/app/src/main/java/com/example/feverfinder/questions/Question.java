package com.example.feverfinder.questions;

/* type indicates what sort of response is given.
 * name gives a unique ID for each question - useful when saving responses
 * label gives the test of each question*/
public class Question {
    String type;
    String name;
    String label;

    public Question(String type, String name, String label){
        this.type = type;
        this.name = name;
        this.label = label;
    }
}
