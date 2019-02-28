package com.example.feverfinder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.feverfinder.questions.Question;
import com.example.feverfinder.questions.QuestionParser;
import com.example.feverfinder.questions.Section;
import com.example.feverfinder.questions.SelectQuestion;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SurveySection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SurveySection extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_SECTION = "section";

    private Section mSection;


    public SurveySection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param section The section we want to show in the fragment.
     * @return A new instance of fragment SurveySection.
     */
    public static SurveySection newInstance(Section section) {
        SurveySection fragment = new SurveySection();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SECTION, section);
        fragment.setArguments(args);
        return fragment;
    }

    public Section getSection() {
        return mSection;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSection = getArguments().getParcelable(ARG_SECTION);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        out.putParcelable(ARG_SECTION, mSection);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_survey_section, container, false);

        //Add the questions
        LinearLayout linearLayout = view.findViewById(R.id.question_container);
        for (Question q : mSection.getQuestions()) {
            //Find out if the question depends on anything - i.e. if it should
            // only be displayed based on other entries and if it does depend on
            //something register it so it can change based on the dependency.
            List<SelectQuestion> dependencies = QuestionParser.getDependencies(q, mSection.getQuestions());
            boolean relevant = true;
            for (SelectQuestion dependency : dependencies) {
                dependency.addSelectionChangedListener(q);
                if (!q.isRelevant(dependency)) relevant = false;
            }

            View child = q.generateView(getContext(), linearLayout);
            child.setId(View.generateViewId());
            linearLayout.addView(child);

            if (!relevant) child.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Could attach listeners here if we wanted
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
