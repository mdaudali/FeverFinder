package com.example.feverfinder.questions;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.feverfinder.R;

import java.util.List;

//TODO: ensure that this updates to contain the correct content -
public class RangeQuestion extends Question implements SeekBar.OnSeekBarChangeListener {
    public static final Parcelable.Creator<RangeQuestion> CREATOR
            = new Parcelable.Creator<RangeQuestion>() {
        public RangeQuestion createFromParcel(Parcel in) {
            return new RangeQuestion(in);
        }

        public RangeQuestion[] newArray(int size) {
            return new RangeQuestion[size];
        }
    };
    private int start;
    private int end;
    private int step;
    private int content;


    /**
     * @param name       is the question name for data storage
     * @param label      is the question label for displaying
     * @param parameters is a parameter string in the form "start=x end=y step=z"
     * @throws ParameterParseException if parameters not in the form "start=x end=y step=z"
     *                                 where x, y and z are integers
     */
    public RangeQuestion(String name, String label, List<Relevancy> relevant, String parameters) throws ParameterParseException {
        super(Question.TYPE_RANGE, name, label, relevant);
        try {
            start = Integer.parseInt(parameters.split("=")[1].split(" ")[0]);
            end = Integer.parseInt(parameters.split("=")[2].split(" ")[0]);
            step = Integer.parseInt(parameters.split("=")[3].split(" ")[0]);
            content = start;
        } catch (NumberFormatException e) {
            throw new ParameterParseException();
        }
    }

    /**
     * Constructor for Parcels
     *
     * @param in Parcel to construct from
     */
    protected RangeQuestion(Parcel in) {
        super(Question.TYPE_RANGE, in);
        start = in.readInt();
        end = in.readInt();
        step = in.readInt();
        content = in.readInt();
    }

    @Override
    public Object getJSONOutput() {
        return content;
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
        // TODO add indicator numbers to slider
        View view = LayoutInflater.from(context)
                .inflate(R.layout.range_question, root, false);

        TextView rangeTextView = view.findViewById(R.id.rangeTextView);
        rangeTextView.setText(getLabel());

        SeekBar rangeSeekBar = view.findViewById(R.id.rangeSeekBar);
        rangeSeekBar.setMax(this.end);
        rangeSeekBar.setProgress(this.step);
        rangeSeekBar.setProgress(content);
        rangeSeekBar.setOnSeekBarChangeListener(this);

        setView(view);
        return view;
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
        dest.writeInt(start);
        dest.writeInt(end);
        dest.writeInt(step);
        dest.writeInt(content);
    }

    /**
     * Notification that the progress level has changed. Clients can use the fromUser parameter
     * to distinguish user-initiated changes from those that occurred programmatically.
     *
     * @param seekBar  The SeekBar whose progress has changed
     * @param progress The current progress level. This will be in the range min..max where min
     *                 and max were set by {@link ProgressBar#setMin(int)} and
     *                 {@link ProgressBar#setMax(int)}, respectively. (The default values for
     *                 min is 0 and max is 100.)
     * @param fromUser True if the progress change was initiated by the user.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        content = progress;
    }

    /**
     * Notification that the user has started a touch gesture. Clients may want to use this
     * to disable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this
     * to re-enable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
