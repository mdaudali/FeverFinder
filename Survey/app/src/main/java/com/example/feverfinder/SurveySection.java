package com.example.feverfinder;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.feverfinder.questions.Option;
import com.example.feverfinder.questions.Question;
import com.example.feverfinder.questions.SelectQuestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SurveySection.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SurveySection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SurveySection extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // TODO: remove the parameters?????????????
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_TITLE = "title";
    //public static final String ARG_QUESTIONS = "questions";

    // TODO: Rename and change types of parameters
    private String mTitle;
    private List<Question> mQuestions;
    private OnFragmentInteractionListener mListener;

    private List<Question> mRelevantQuestions;

    public void setmQuestions(List<Question> mQuestions) {
        this.mQuestions = mQuestions;
    }

    public SurveySection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Title of the survey section.
     * @param questions List of the questions for this section to display.
     * @return A new instance of fragment SurveySection.
     */
    public static SurveySection newInstance(String title, List<Question> questions) {
        SurveySection fragment = new SurveySection();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }
        Log.d("CREATION", "onCreate() running in" + this.mTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Questions that aren't yet relevant
        mRelevantQuestions = new LinkedList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_survey_section, container, false);
        TextView title = view.findViewById(R.id.fragment_title);
        title.setText(mTitle);

        //Add the questions
        LinearLayout linearLayout = view.findViewById(R.id.question_container);
        for (Question q : mQuestions) {
            //TODO: remove this once all generated views are not null

            // display mandatory questions
            if (q.getRelevant().equals("")) {
                View child = q.generateView(getContext(), linearLayout);
                if (child != null)
                    linearLayout.addView(child);
            } else {
                // TODO WHAT TO do if q is not yet relevant??
                // add name to list of strings of relevant qs to be asked
                mRelevantQuestions.add(q);
            }
        }

        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
