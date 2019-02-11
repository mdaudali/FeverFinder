package com.example.feverfinder.questions;

import android.view.View;

public class RangeQuestion extends Question {
    private int start;
    private int end;
    private int step;


    /**
     * @param name is the question name for data storage
     * @param label is the question label for displaying
     * @param parameters is a parameter string in the form "start=x end=y step=z"
     * @throws ParameterParseException if parameters not in the form "start=x end=y step=z"
     *                                                         where x, y and z are integers
     */
    public RangeQuestion(String name, String label, String parameters) throws ParameterParseException {
        super(name, label);
        try {
            start = Integer.parseInt(parameters.split("=")[1].split(" ")[0]);
            end = Integer.parseInt(parameters.split("=")[2].split(" ")[0]);
            step = Integer.parseInt(parameters.split("=")[3].split(" ")[0]);
        }
        catch (NumberFormatException e) {
            throw new ParameterParseException();
        }
    }

    @Override
    View generateView() {
        return null;
    }


}
