package com.example.feverfinder.questions;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class QuestionParser {

    /**
     * @param questionsFile is a json file containing a list of the sections
     * @param choicesFile   is a json file containing the options for any multiple choice questions
     * @return a list of sections
     */
    public static List<Section> getSections(InputStream questionsFile, InputStream choicesFile) {
        List<Section> sections = new LinkedList<>();
        Map<String, List<Option>> responseChoices = parseChoices(questionsFile);

        try {
            Object obj = new JSONParser().parse(new InputStreamReader(choicesFile));
            JSONArray sectionsJson = (JSONArray) obj;

            // iterate over each section in turn
            for (int i = 0; i < sectionsJson.size(); i++) {
                List<Question> questions = new LinkedList<>();
                JSONArray sectionJson = (JSONArray) sectionsJson.get(i);

                questions = new LinkedList<>();
                getQuestions(sectionJson, responseChoices, new LinkedList<Relevancy>(), questions);

                sections.add(new Section("Section " + String.valueOf(i + 1), questions));
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return sections;
    }

    /**
     * This parses a JSON array into a list of Questions which it appends to the provided list
     *
     * @param questionsJson   JSON array containing the questions to be parsed
     * @param responseChoices A map from the question name to a list of options
     * @param relevancies     A list of relevancies that this group will inherit from their parents
     * @param questions       The final list of questions to append to
     */
    private static void getQuestions(JSONArray questionsJson, Map<String, List<Option>> responseChoices, List<Relevancy> relevancies, List<Question> questions) {
        for (int i = 0; i < questionsJson.size(); i++) {
            JSONObject questionJSON = (JSONObject) questionsJson.get(i);

            String type = questionJSON.get("type").toString();
            String name = questionJSON.get("name").toString();
            String label = questionJSON.get("label").toString();
            String relevant = questionJSON.get("relevant").toString();
            List<Relevancy> questionRelevancy = new LinkedList<>(relevancies);
            //Add to the list of provided relevancies
            if (!relevant.equals("")) questionRelevancy.add(new Relevancy(relevant));
            Question newQuestion = null;

            if (type.equals("text")) {
                newQuestion = new TextQuestion(name, label, questionRelevancy);
            } else if (type.startsWith("select")) {
                newQuestion = new SelectQuestion(name, label, questionRelevancy,
                        type.startsWith("select_multiple"), responseChoices.get(type.split(" ")[1]));
            } else if (type.equals("integer")) {
                newQuestion = new IntegerQuestion(name, label, questionRelevancy);
            } else if (type.equals("decimal")) {
                newQuestion = new DecimalQuestion(name, label, questionRelevancy);
            } else if (type.equals("range")) {
                try {
                    newQuestion = new RangeQuestion(name, label, questionRelevancy,
                            questionJSON.get("parameters").toString());
                } catch (ParameterParseException e) {
                    e.printStackTrace();
                }
            }
            //If type is a group, add all the questions within the group

            else if (type.equals("begin_group")) {
                getQuestions((JSONArray) questionJSON.get("relevant_questions"), responseChoices, questionRelevancy, questions);
            }


            if (newQuestion != null) {
                questions.add(newQuestion);

                //Find out if the question depends on anything - i.e. if it should
                // only be displayed based on other entries and if it does depend on
                //something register it so it can change based on the dependency.
                List<SelectQuestion> dependencies = getDependencies(newQuestion, questions);
                for (SelectQuestion dependency : dependencies) {
                    dependency.addSelectionChangedListener(newQuestion);
                }
            }
        }
    }

    /**
     * This finds the SelectQuestions which the Question provided is dependant on based on the
     * relevancies the question has
     *
     * @param question   The question we are looking for the dependency of
     * @param searchList The list of questions in which we should search
     * @return The list of SelectQuestions which question is dependant on
     */
    private static List<SelectQuestion> getDependencies(Question question, List<Question> searchList) {
        List<SelectQuestion> dependencies = new LinkedList<>();
        List<Relevancy> relevancies = question.getRelevancies();

        for (Relevancy relevancy : relevancies) {
            for (Question q : searchList) {
                if (q.getName().equals(relevancy.getQuestionName()) && q.getClass() == SelectQuestion.class) {
                    dependencies.add((SelectQuestion) q);
                }
            }
        }
        return dependencies;
    }


    /**
     * @param file is a json file containing the options for any multiple choice questions
     * @return map from the question name to a list of options
     */
    private static Map<String, List<Option>> parseChoices(InputStream file) {
        // local variables
        Map<String, List<Option>> responseChoices = new HashMap<>();
        List<Option> optionsList;
        JSONObject currentList;
        JSONArray optionsArray;

        try {
            Object obj = new JSONParser().parse(new InputStreamReader(file));

            // array of JSON objects for each choice type
            JSONArray listsOfChoices = (JSONArray) obj;

            // iterate over different lists of options
            for (int i = 0; i < listsOfChoices.size(); i++) {
                currentList = (JSONObject) listsOfChoices.get(i);

                // add options to a list one by 1
                optionsList = new ArrayList<>();
                optionsArray = (JSONArray) currentList.get("options");

                // add each option for a choice to list of options
                JSONObject currentOption;
                for (int j = 0; j < optionsArray.size(); j++) {
                    currentOption = (JSONObject) optionsArray.get(j);


                    //TODO: fix this - in the JSON 9 is represented as "9.0"
                    String name;
                    if (currentOption.get("name").getClass() == Double.class) {
                        name = String.valueOf(Math.round((Double) currentOption.get("name")));
                    } else {
                        name = (String) currentOption.get("name");
                    }


                    Option newOption = new Option(name, currentOption.get("label").toString());
                    optionsList.add(newOption);
                }

                // add list_name and list of options to map
                responseChoices.put(currentList.get("list_name").toString(), optionsList);
            }

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return responseChoices;
    }
}
