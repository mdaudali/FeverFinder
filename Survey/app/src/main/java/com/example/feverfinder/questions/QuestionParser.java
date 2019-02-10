package com.example.feverfinder.questions;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Converts JSON objects of questions into a Map<Question, Response>
* Response has a type and if the type is of the form select_... response will also contain
* a list of options and null otherwise*/
public class QuestionParser {
    Map<String, List<Option>> responseChoices;
    Map<Question, Response> questions;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public QuestionParser(){
        parseChoices();
        parseQuestions();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addQuestionToMap(JSONObject currentQuestion){
        Question question = new Question(currentQuestion.get("type").toString(),
                currentQuestion.get("name").toString(),
                currentQuestion.get("label").toString());

        // get option
        String optionName = "";
        if (question.type.split(" ").length > 1){
            optionName = question.type.split(" ")[1];
        }

        // add question text and either list of responses or null if no options for response
        questions.put(question, new Response(question.type, responseChoices.getOrDefault(optionName, null)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void parseQuestions(){
        try {
            Object obj = new JSONParser().parse(new FileReader("C:\\Users\\sumaiyah\\OneDrive - University Of Cambridge\\Group Project\\JSONToObjects\\src\\questions.json"));
            JSONArray sections = (JSONArray)obj;

            // iterate over each section in turn
            for (int i=0; i<sections.size(); i++){
                JSONArray section = (JSONArray) sections.get(i);

                // iterate over questions in a section
                for (int j=0; j<section.size(); j++){
                    JSONObject currentQ = (JSONObject) section.get(j);

                    // skip empty questions
                    if (currentQ.get("label").equals("")) continue;

                    // Add question and details about response to hashmap
                    addQuestionToMap(currentQ);
                }
                // todo what to do after end of each section
            }

        } catch (ParseException | IOException e){
            e.printStackTrace();
        }
    }

    public void parseChoices(){
        // local variables
        responseChoices = new HashMap<>();
        List<Option> optionsList; JSONObject currentList; JSONArray optionsArray;

        try {
            Object obj = new JSONParser().parse(new FileReader("C:\\Users\\sumaiyah\\OneDrive - University Of Cambridge\\Group Project\\JSONToObjects\\src\\choices.json"));

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
                    Option newOption = new Option(currentOption.get("name").toString(), currentOption.get("label").toString());
                    optionsList.add(newOption);
                }

                // add list_name and list of options to map
                responseChoices.put(currentList.get("list_name").toString(), optionsList);
            }

        } catch (ParseException | IOException e){
            e.printStackTrace();
        }
    }

    public Map<Question, Response> getQuestions(){
        return questions;
    }
}
