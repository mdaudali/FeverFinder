package com.example.feverfinder.questions;

/* Each option has a name and label e.g.
 * {"list_name": "see_dump", "options": [{"name": 1.0, "label": "Yes"}, {"name": 2.0, "label": "No"}]} */
public class Option {
    String name;

    public String getName() {
        return name;
    }

    String label;

    public Option(String name, String label){
        this.name = name;
        this.label = label;
    }
}
