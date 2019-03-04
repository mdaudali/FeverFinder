package com.example.feverfinder.questions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.feverfinder.R;
import com.example.feverfinder.SurveyActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GPSQuestion extends Question implements View.OnClickListener {
    public static final Parcelable.Creator<GPSQuestion> CREATOR
            = new Parcelable.Creator<GPSQuestion>() {
        public GPSQuestion createFromParcel(Parcel in) {
            return new GPSQuestion(in);
        }

        public GPSQuestion[] newArray(int size) {
            return new GPSQuestion[size];
        }
    };
    //Used to keep track of the user's location
    LocationManager locManager;
    //These IDs are used to ensure Android knows how to repopulate the data in the survey if the view
    // is destroyed and recreated.
    private int latitudeID;
    private int longitudeID;
    private String latitude;
    private String longitude;


    public GPSQuestion(String name, String label, List<Relevancy> relevant) {
        super(Question.TYPE_GPS, name, label, relevant);
        latitudeID = View.generateViewId();
        longitudeID = View.generateViewId();
        latitude = "";
        longitude = "";
    }

    protected GPSQuestion(Parcel in) {
        super(Question.TYPE_GPS, in);
        latitudeID = in.readInt();
        longitudeID = in.readInt();
        latitude = in.readString();
        longitude = in.readString();
    }


    /**
     * Given a JSON object to output to, add the data relevant to the question to be submitted to
     * the database
     *
     * @param out the JSON object to add to
     * @throws JSONException
     */
    @Override
    public void addToJSON(JSONObject out) throws JSONException {
        out.put(getName().toLowerCase() + "_latitude", latitude.equals("") ? 0.0 : Double.valueOf(latitude));
        out.put(getName().toLowerCase() + "_longitude", longitude.equals("") ? 0.0 : Double.valueOf(longitude));
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
        View view = LayoutInflater.from(context)
                .inflate(R.layout.gps_question, root, false);
        TextView textView = view.findViewById(R.id.text_label);
        textView.setText(getLabel());

        final EditText latitude = view.findViewById(R.id.latitude);
        latitude.setId(latitudeID);
        latitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setLatitude(s.toString());
            }
        });

        EditText longitude = view.findViewById(R.id.longitude);
        longitude.setId(longitudeID);
        longitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setLongitude(s.toString());
            }
        });

        Button getLocationButton = view.findViewById(R.id.get_location_button);
        getLocationButton.setOnClickListener(this);

        setView(view);
        setupGps(view);

        return view;
    }

    private void setLatitude(String latitude) {
        this.latitude = latitude;
    }


    private void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    private LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // Previously mock location is cleared.
            // getLastKnownLocation(LocationManager.GPS_PROVIDER); will not return mock location.
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }


        @Override
        public void onProviderEnabled(String provider) {

        }


        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void setupGps(View view) {
        locManager = (LocationManager) view.getContext().getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ((SurveyActivity) view.getContext()).requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            }
        }
        locManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 10, mListener);
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
        dest.writeInt(latitudeID);
        dest.writeInt(longitudeID);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }

    /**
     * Called when the GPS button is clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ((SurveyActivity) v.getContext()).requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            }
        }
        //TODO: check accuracy of this method of location finding!
        locManager.requestLocationUpdates("gps", 0, 0, mListener);
        LocationManager lm = (LocationManager) v.getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        String strLocationProvider = lm.getBestProvider(criteria, true);
        Location location = lm.getLastKnownLocation(strLocationProvider);
        if (location != null) {
            ((EditText) getView().findViewById(latitudeID)).setText(latitude);
            ((EditText) getView().findViewById(longitudeID)).setText(longitude);
        }

        /*
        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());

        ((EditText) getView().findViewById(latitudeID)).setText(latitude);
        ((EditText) getView().findViewById(longitudeID)).setText(longitude); */
    }
}
