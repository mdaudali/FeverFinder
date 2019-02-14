package com.example.feverfinder.questions;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.feverfinder.R;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View generateView(Context context, ViewGroup root) {
        // TODO add indicator numbers to slider
        View view = context.getSystemService(LayoutInflater.class)
                .inflate(R.layout.range_question, root, false);

        TextView rangeTextView = view.findViewById(R.id.rangeTextView);
        rangeTextView.setText(getLabel());

        SeekBar rangeSeekBar = view.findViewById(R.id.rangeSeekBar);
        rangeSeekBar.setMax(this.end);
        rangeSeekBar.setProgress(this.step);

        return view;
    }


}
