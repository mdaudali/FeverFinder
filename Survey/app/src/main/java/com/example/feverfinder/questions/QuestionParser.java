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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                        String relevant = currentQ.get("relevant").toString();
                        Question newQuestion = null;

                        if (type.equals("text")) {
                            newQuestion = new TextQuestion(name, label, relevant);
                        }

                        else if (type.startsWith("select")) {
                            newQuestion = new SelectQuestion(name, label, relevant,
                                    type.startsWith("select_multiple"), responseChoices.get(name));
                        }

                        else if (type.equals("integer")) {
                            newQuestion = new IntegerQuestion(name, label, relevant);
                        }

                        else if (type.equals("decimal")) {
                            newQuestion = new DecimalQuestion(name, label, relevant);
                        }

                        else if (type.equals("range")) {
                            try {
                                newQuestion = new RangeQuestion(name, label, relevant,
                                        currentQ.get("parameters").toString());
                            } catch (ParameterParseException e) {
                                e.printStackTrace();
                            }
                        }

                        if (newQuestion != null) {
                            questions.add(newQuestion);

                            //Find out if the question depends on anything - i.e. if it should
                            // only be displayed based on other entries and if it does depend on
                            //something register it so it can change based on the dependency.
                            SelectQuestion dependency = getDependency(newQuestion, questions);
                            if (dependency != null) {
                                //TODO: remove this

                                Log.d("DEPENDENT", newQuestion.getName());
                                Log.d("DEPENDENCY", dependency.getName());

                                dependency.addSelectionChangedListener(newQuestion);
                            }
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
     * This finds the SelectQuestion which the Question provided is dependant on based on the
     * relevancy String the question has
     *
     * @param question The question we are looking for the dependency of
     * @param searchList The list of questions in which we should search
     * @return The SelectQuestion which question is dependant on (null if no such exists)
     */
    private static SelectQuestion getDependency(Question question, List<Question> searchList){
        String relevant = question.getRelevant();

        if (!relevant.equals("")) {
            // name of the question q depends on
            String parentName = "";

            // Pattern Matching over strings in relevant
            Pattern p;
            Matcher m;
            if (relevant.startsWith("$")) { // select_one
                p = Pattern.compile("\\$\\{([^}]+)\\} ?\\= ?\\'([^{]+)\\'"); // to match e.g. ${see_dump}='1'
                m = p.matcher(relevant);
                if (m.find()) {
                    parentName = m.group(1);
                }
            } else { // select multiple
                p = Pattern.compile("selected\\(\\$\\{([^}]+)\\} ?\\, ?\\'([^{]+)\\'\\)"); // to match e.g. selected(${contact}, '7')
                m = p.matcher(relevant);
                if (m.find()) {
                    parentName = m.group(1);
                }
            }

            for (Question q : searchList) {
                if (q.getName().equals(parentName) && q.getClass() == SelectQuestion.class)
                    return (SelectQuestion) q;
            }
        }

        return null;
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


                    //TODO: fix this - in the JSON 9 is represented as "9.0"
                    String name;
                    if (currentOption.get("name").getClass() ==  Double.class) {
                        name = String.valueOf(Math.round((Double) currentOption.get("name")));
                    }
                    else {
                        name = (String) currentOption.get("name");
                    }



                    Option newOption = new Option(name,  currentOption.get("label").toString());
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
