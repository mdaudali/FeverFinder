package com.example.feverfinder.questions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.feverfinder.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* Used when loading in questions */
public class QuestionAdapter extends BaseAdapter {
    Map<Question, Response> questions;
    List<Question> questionList;
    List<Response> responseList;


    LayoutInflater mInflator;

    public QuestionAdapter(Context c, Map<Question, Response> q){
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        questions = q;
        questionList = new ArrayList<Question>(questions.keySet());
        responseList = new ArrayList<Response>(questions.values());
    }

    @Override
    public int getCount(){
        // how many questions are there
        return questions.size();
    }

    @Override
    public Object getItem(int position){
        return questions.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // TODO sort this out

        // how to present information given
        View v = mInflator.inflate(R.layout.question_layout, null);

        TextView questionTextView = (TextView) v.findViewById(R.id.questionTextView);
        questionTextView.setText(questionList.get(position).name);
        return v;
    }
}
