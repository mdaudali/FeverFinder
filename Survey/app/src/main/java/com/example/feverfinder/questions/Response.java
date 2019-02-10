package com.example.feverfinder.questions;

import java.util.List;

/* All questions will have a type
 * Questions with a type of the form select_... will also have a list of options */
public class Response {
    String type;
    List<Option> options;

    public Response(String type, List<Option> options){
        this.type = type;
        this.options = options;
    }
}
