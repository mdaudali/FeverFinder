from collections import OrderedDict
import simplejson as json

import xlrd


def convertChoicesToJSON(filename):
    """ Convert choices sheet in survey-choices.xlsx into choices.JSON """
    """
    [  
       {  
          "list_name":"",
          "options":[  
             {  
                "name":"",
                "label":""
             },
             {...}, ...
          ]
       }, {...}, ... 
    ]
    """

    # Open workbook
    wb = xlrd.open_workbook(filename)

    # 'choice_sheet' is second sheet in excel file
    choice_sheet = wb.sheet_by_name(wb.sheet_names()[1])

    # Iterate over first row in spreadsheet to get keys
    first_row = choice_sheet.row_values(0)

    # List of choices where each choice has an name and a list of choice options
    choices_list = []

    # for rownum in range(1, choicesSheet.nrows):
    choice = OrderedDict()
    options = []

    for rownum in range(1, choice_sheet.nrows):
        row_values = choice_sheet.row_values(rownum)

        # empty row indicates start of new list_name
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
        option[first_row[1]] = str(int(row_values[1]))
        option[first_row[2]] = row_values[2]
        options.append(option)

    # add last row
    choice['options'] = options
    choices_list.append(choice)

    # Serialize the list of dicts to JSON
    j = json.dumps(choices_list)

    # Write to file
    with open('../Survey/app/src/main/res/raw/questions.json', 'w') as f:
        f.write(j)

def convertQuestionsToJSON(filename):
    """ Convert survey sheet in survey-choices.xlsx into questions.JSON """
    """
    [  
       [  # section
          {  # question with no dependancies
             "type":"",
             "name":"",
             "label":"",
             "relevant":"",
             "parameters":"",
             "media":""
          }, {...} , .... ,
            {  # question with dependancies
               "type":"begin_group",
               "name":"",
               "label":"",
               "relevant":"${<dependant-question>} = <dependant-answer>",
               "parameters":"",
               "media":"",
               "relevant_questions":[  
                  {  
                     "type":"",
                     "name":"",
                     "label":"",
                     "relevant":"",
                     "parameters":"",
                     "media":""
                  }, {...}, ...
                ]
            }

        ], [...], ....
    ]
    """

    # Open workbook
    wb = xlrd.open_workbook(filename)

    # 'survey_sheet' is first sheet in excel file
    survey_sheet = wb.sheet_by_name(wb.sheet_names()[0])

    # Iterate over first row in spreadsheet to get keys
    first_row = survey_sheet.row_values(0)

    # List of sections of questions
    sectionList = readSurvey(first_row, survey_sheet)

    # Convert to JSON and write to file
    writeToFile(sectionList)

def readSurvey(first_row, survey_sheet):
    """ Iterates over rows until it has parsed entire survey
            Returns:
                sectionList: a list of sections made up of lists of questions in each section
    """

    n_rows = survey_sheet.nrows - 1
    current_row = 0

    # List of sections - denoted by 'new_section' in type field
    sectionList = []

    # List to questions in each section
    questionsList = []

    # Iterate over each row in the survey
    while current_row < n_rows:
        current_row += 1
        row_values = survey_sheet.row_values(current_row)

        # if at start group
        if (row_values[0] == "begin_group"):
            current_row, group = parseGroup(first_row, current_row, survey_sheet)
            questionsList.append(group)
            continue

        # if at end of section
        if (row_values[0] == "new_section"):
            # add current section to list of sections
            sectionList.append(questionsList)

            # new list of questions for new section
            questionsList = []
            continue

        # else normal question
        questionsList.append(parseQuestion(first_row, current_row, survey_sheet))

    # add last section
    sectionList.append(questionsList)

    return sectionList

def parseGroup(first_row, current_row, survey_sheet):
    """ Transforms groups of questions that become relevant at a certain point in time
            ([('type', ''), ('name', ''), ('label', ''), ('relevant', ''), ('parameters', ''), ('media', '')])
            Args:
                current_row (int): Index of row being transformed

            Returns:
                current_row: need to update the overall row index
                group: dictionary of elements in the row
    """

    group = parseQuestion(first_row, current_row, survey_sheet)
    relevantQuestions = []

    # iterate over questions in a group until you get to the end of the group or reach a new group inside current group
    while(True):
        current_row += 1
        row_values = survey_sheet.row_values(current_row)

        # end of current group
        if (row_values[0] == "end_group"):
            break

        # nested group
        if (row_values[0] == "begin_group"):
            current_row, newGroup = parseGroup(first_row, current_row, survey_sheet)
            relevantQuestions.append(newGroup)
            continue

        # add question
        relevantQuestions.append(parseQuestion(first_row, current_row, survey_sheet))

    group['relevant_questions'] = relevantQuestions

    return current_row, group

def parseQuestion(first_row, current_row, survey_sheet):
    """ Transforms a row of the Excel file into an dictionary e.g.
        ([('type', ''), ('name', ''), ('label', ''), ('relevant', ''), ('parameters', ''), ('media', '')])
        Args:
            current_row (int): Index of row being transformed

        Returns:
            question: dictionary of elements in the row
        """
    question = OrderedDict()
    row_values = survey_sheet.row_values(current_row)

    # assign each column one at a time
    index = 0
    for key in first_row:
        question[key] = row_values[index]
        index += 1

    return question

def writeToFile(sectionList):
    """ Writes JSON objects to file
    Args:
        sectionList (list): List of sections made up of lists of questions.
    """

    # Serialize the list of dicts to JSON
    j = json.dumps(sectionList)

    # Write to file
    with open('../Survey/app/src/main/res/raw/questions.json', 'w') as f:
        f.write(j)




