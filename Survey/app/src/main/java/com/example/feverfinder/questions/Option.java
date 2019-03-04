package com.example.feverfinder.questions;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/* Each option has a name and label e.g.
 * {"list_name": "see_dump", "options": [{"name": 1.0, "label": "Yes"}, {"name": 2.0, "label": "No"}]} */
public class Option implements Parcelable {
    public static final Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };
    private String name;
    private String label;
    private int id;

    public Option(String name, String label) {
        this.name = name;
        this.label = label;
        this.id = View.generateViewId();
    }

    private Option(Parcel in) {
        name = in.readString();
        label = in.readString();
        id = in.readInt();
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Option.class) return false;
        Option option2 = (Option) o;
        return (this.name.equals(option2.name) && this.label.equals(option2.label));
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
        dest.writeString(name);
        dest.writeString(label);
        dest.writeInt(id);
    }
}
