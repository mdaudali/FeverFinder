package com.example.feverfinder.questions;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* Converts JSON objects of questions into a Map<Question, Response>
* Response has a type and if the type is of the form select_... response will also contain
* a list of options and null otherwise*/
public class QuestionParser {

    /**
     * @param questionsFile is a json file containing a list of the sections
     * @param choicesFile is a json file containing the options for any multiple choice questions
     * @return a list of sections
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Section> getSections(InputStream questionsFile, InputStream choicesFile) {
        List<Section> sections = new LinkedList<>();
        Map<String, List<Option>> responseChoices = parseChoices(questionsFile);

        try {
            Object obj = new JSONParser().parse(new InputStreamReader(choicesFile));
            JSONArray sectionsJson = (JSONArray)obj;

            // iterate over each section in turn
            for (int i=0; i<sectionsJson.size(); i++) {
                List<Question> questions = new LinkedList<>();
                JSONArray sectionJson = (JSONArray) sectionsJson.get(i);

                // iterate over questions in a section
                for (int j = 0; j < sectionJson.size(); j++) {
                    JSONObject currentQ = (JSONObject) sectionJson.get(j);

                    // skip empty questions
                    if (!currentQ.get("label").equals("")) {
                        String type = currentQ.get("type").toString();
                        String name = currentQ.get("name").toString();
                        String label = currentQ.get("label").toString();

                        if (type.equals("text")) {
                            questions.add(new TextQuestion(name, label));
                        } else if (type.startsWith("select")) {
                            // If this is a multiple choice question
                            if(type.startsWith("select_multiple")) {
                                questions.add(new MultiOptionQuestion(name, label, responseChoices.get(name)));
                            }
                            // Otherwise it must be a single choice question
                            else {
                                // But can be a boolean question which we distinguish, because it
                                // represents a boolean value.
                                if(responseChoices.get(name).get(0).label.equals("Yes")) {
                                    Log.d("YESNO", label);
                                    questions.add(new YesNoQuestion(name, label));
                                } else {
                                    questions.add(new SingleOptionQuestion(name, label, responseChoices.get(name)));
                                }
                            }
                        }

                        else if (type.equals("integer")) {
                            questions.add(new IntegerQuestion(name, label));
                        }

                        else if (type.equals("decimal")) {
                            questions.add(new DecimalQuestion(name, label));
                        }
                    }
                }
                sections.add(new Section("Section " + String.valueOf(i + 1), questions));
            }
        } catch (ParseException | IOException e){
            e.printStackTrace();
        }

        return sections;
    }


    /**
     * @param file is a json file containing the options for any multiple choice questions
     * @return map from the question name to a list of options
     */
    private static Map<String, List<Option>> parseChoices(InputStream file){
        // local variables
        Map<String, List<Option>> responseChoices = new HashMap<>();
        List<Option> optionsList; JSONObject currentList; JSONArray optionsArray;

        try {
            Object obj = new JSONParser().parse(new InputStreamReader(file));

            // array of JSON objects for each choice type
            JSONArray listsOfChoices = (JSONArray)obj;

            // iterate over different lists of options
            for (int i=0; i<listsOfChoices.size(); i++){
                currentList = (JSONObject) listsOfChoices.get(i);

                // add options to a list one by 1
                optionsList = new ArrayList<>();
                optionsArray = (JSONArray) currentList.get("options");

                // add each option for a choice to list of options
                JSONObject currentOption;
                for (int j=0; j<optionsArray.size(); j++){
                    currentOption = (JSONObject) optionsArray.get(j);
                    Option newOption = new Option(currentOption.get("name").toString(),  currentOption.get("label").toString());
                    optionsList.add(newOption);
                }

                // add list_name and list of options to map
                responseChoices.put(currentList.get("list_name").toString(), optionsList);
            }

        } catch (ParseException | IOException e){
            e.printStackTrace();
        }
        return responseChoices;
    }
}
