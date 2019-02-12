import xlrd
from collections import OrderedDict
import simplejson as json

# Open the excel sheet
wb = xlrd.open_workbook('survey-questions.xlsx')

# ----------------------- Convert Questions to JSON ----------------------------
# TODO do we need to get rid of question numbers?
"""
Each section made up of a list of questions
[[{
    "type": "", 
    "name": "", 
    "label": "", 
    "relevant": "", 
    "hint": "", 
    "parameters": "", 
    "constraint": "", 
    "constraint_message": "", 
    "media::image": ""}, ...], [...], ...]]
"""
surveySheet = wb.sheet_by_name('survey')

# List to hold dictionaries of each question
questions_list = []

# Iterate over first row in spreadsheet to get keys
first_row = surveySheet.row_values(0)

# group questions into sections - denoted by 'note' in type field
section = []

for rownum in range(1, surveySheet.nrows):
    question = OrderedDict()
    row_values = surveySheet.row_values(rownum)

    # add components of each question
    index = 0

    # if at end of section
    if (row_values[index] == "note"):
        questions_list.append(section)
        section = []
        continue

    for key in first_row:
        question[key] = row_values[index]
        index += 1

    # add question to list
    section.append(question)

# add last section
questions_list.append(section)

# Serialize the list of dicts to JSON
j = json.dumps(questions_list)

# Write to file
with open('questions.json', 'w') as f:
    f.write(j)

# --------------------------------------------------------------------------------

# ----------------------- Convert Choices to JSON --------------------------------
"""
Each choice made up of a list_name and a dictionary of options
[`  {
    "list_name": "gender", 
    "options": [
        {"name": 2.0, "label": "Female"}, 
        {"name": 1.0, "label": "Male"}
        ]
    }, {...}, ...]
"""

choicesSheet = wb.sheet_by_name("choices")

# Iterate over first row in spreadsheet to get keys
first_row = choicesSheet.row_values(0)

choices_list = []

# for rownum in range(1, choicesSheet.nrows):
choice = OrderedDict()
options = []

for rownum in range(1, choicesSheet.nrows):
    row_values = choicesSheet.row_values(rownum)

    # empty row indicates new list_name
    if row_values[0] == "":
        # add list of choice options and add choice to dictionary
        choice['options'] = options
        choices_list.append(choice)

        # reinitialise variables
        choice = OrderedDict()
        options = []
        continue

    # list_name
    choice[first_row[0]] = row_values[0]

    # add each option to list of options for each list_name
    option = OrderedDict()
    option[first_row[1]] = row_values[1]
    option[first_row[2]] = row_values[2]
    options.append(option)

# add last row
choice['options'] = options
choices_list.append(choice)

# Serialize the list of dicts to JSON
j = json.dumps(choices_list)

# Write to file
with open('choices.json', 'w') as f:
    f.write(j)

# --------------------------------------------------------------------------------
