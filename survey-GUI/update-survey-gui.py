#!/usr/bin/python3
# -*- coding: utf-8 -*
from tkinter import Tk, Frame, Menu, ttk
import tkinter
import tkinter as tk

from openpyxl import load_workbook
from openpyxl.utils.dataframe import dataframe_to_rows


class Example(tkinter.Frame):
    def __init__(self):
        """ initialise UI with menu bar upon opening """
        super().__init__()
        self.initUI()

    def initUI(self):
        self.master.title("Survey")  # set window title

        menubar = tkinter.Menu(self.master)  # add menu bar
        self.master.config(menu=menubar)

        # under "File"
        fileMenu = tkinter.Menu(menubar, tearoff=0)
        fileMenu.add_command(label="New Question", command=self.displayQuestionMenu)  # form to add new question
        fileMenu.add_command(label="Save", command=self.save)  # save changes
        fileMenu.add_separator()
        fileMenu.add_command(label="Quit", command=self.onExit)  # close window
        menubar.add_cascade(label="File", menu=fileMenu)

        # under "Help"
        helpMenu = tkinter.Menu(menubar, tearoff=0)
        helpMenu.add_command(label="About...", command=self.displayHelpMenu)  # display help popup
        menubar.add_cascade(label="Help", menu=helpMenu)

    def displayHelpMenu(self):
        """ When click about... pop up menu explaining survey format """
        helpPopup = tk.Tk()
        helpPopup.wm_title("About...")  # TODO wrap text
        label = ttk.Label(helpPopup,  # TODO seperate how to do/about??
                          text="add stuff into a textfile explaining the excel survey and maybe about the project too?")
        label.pack(side="top", fill="x", pady=10)
        helpPopup.mainloop()

    def displayQuestionMenu(self):
        frameQType = tkinter.Frame(self.master)
        frameQType.pack(fill=tkinter.X)

        # pick question type
        lblQuestionType = tkinter.Label(frameQType, text="Question Type", width=20)
        lblQuestionType.pack(side=tkinter.LEFT, padx=5, pady=5)

        # display form according to type of question choses
        QuestionTypes = {'text', 'decimal', 'integer', 'select_one', 'select_multiple', 'range'}
        typeOptionChosen = tkinter.StringVar(self.master)
        typeOptionChosen.set('text')  # set the default option
        QuestionTypesOptions = tkinter.OptionMenu(frameQType, typeOptionChosen, *QuestionTypes)
        QuestionTypesOptions.pack(fill=tkinter.X, padx=5, expand=True)

        # track type of question chosen and display format of question
        typeOptionChosen.trace("w", lambda name, index, mode,
                                           typeOptionChosen=typeOptionChosen: self.checkTypeOptionChosen(typeOptionChosen))

    def checkTypeOptionChosen(self, typeOptionChosen):
        """ tracks the typeOptionChosen and displays form accordingly """
        typeOption = typeOptionChosen.get()

        for widget in self.master.winfo_children():
            if isinstance(widget, tk.Frame):
                for frame in widget.winfo_children():
                    if not (str(frame).startswith('.!frame.')):
                        frame.pack_forget()
                        frame.destroy()
                        widget.destroy()

        questionFrame = tkinter.Frame(self.master)
        questionFrame.pack(fill=tkinter.X, side=tkinter.TOP)

        # INPUT questionName
        questionNameFrame = tkinter.Frame(questionFrame)
        questionNameFrame.pack(fill=tkinter.X)
        # Add label asking for question name
        lblQuestionName = tkinter.Label(questionNameFrame, text="Question Name", width=20)
        lblQuestionName.pack(side=tkinter.LEFT, padx=5, pady=5)
        # Add input for question name
        entryQuestionName = tkinter.Entry(questionNameFrame)
        entryQuestionName.pack(fill=tkinter.X, padx=5, expand=True)

        # INPUT questionLabel
        questionLabelFrame = tkinter.Frame(questionFrame)
        questionLabelFrame.pack(fill=tkinter.X)
        # Add label asking for question label
        lblQuestionLabel = tkinter.Label(questionLabelFrame, text="Question Label", width=20)
        lblQuestionLabel.pack(side=tkinter.LEFT, padx=5, pady=5)
        # Add input for question label
        entryQuestionLabel = tkinter.Entry(questionLabelFrame)
        entryQuestionLabel.pack(fill=tkinter.X, padx=5, expand=True)

        # INPUT range parameters
        if (typeOption == 'range'):
            self.displayRangeOptions(questionFrame, entryQuestionName, entryQuestionLabel)
        # INPUT select_ choice parameters
        elif (typeOption.startswith('select_')):
            self.displaySelectOptions(questionFrame, entryQuestionName, entryQuestionLabel)
        else:  # add question
            buttonFrame = tkinter.Frame(questionFrame)
            buttonFrame.pack()
            addButton = tkinter.Button(buttonFrame, text="Add Question",
                                       command=lambda: self.btnAddQuestion(
                                           typeOption, entryQuestionName.get(), entryQuestionLabel.get(), "", "", ""))
            addButton.pack(side=tkinter.RIGHT, padx=5, pady=5)

    def displayRangeOptions(self, questionFrame, entryQuestionName, entryQuestionLabel):
        # user must enter "start" "end" "step" parameters for range
        rangeFrame = tkinter.Frame(questionFrame)
        rangeFrame.pack(fill=tkinter.X)
        tkinter.Label(rangeFrame, text="Enter range parameters", width=20).pack(side=tkinter.LEFT, padx=5, pady=5)

        # label for start
        startFrame = tkinter.Frame(questionFrame)
        startFrame.pack(fill=tkinter.X)
        tkinter.Label(startFrame, text="Start of range", width=20).pack(side=tkinter.LEFT, padx=5, pady=5)
        # Add input for question label
        entryStart = tkinter.Entry(startFrame)
        entryStart.pack(fill=tkinter.X, padx=5, expand=True)

        # label for end
        endFrame = tkinter.Frame(questionFrame)
        endFrame.pack(fill=tkinter.X)
        tkinter.Label(endFrame, text="End of range", width=20).pack(side=tkinter.LEFT, padx=5, pady=5)
        # Add input for question label
        entryEnd = tkinter.Entry(endFrame)
        entryEnd.pack(fill=tkinter.X, padx=5, expand=True)

        # label for step
        stepFrame = tkinter.Frame(questionFrame)
        stepFrame.pack(fill=tkinter.X)
        tkinter.Label(stepFrame, text="Step", width=20).pack(side=tkinter.LEFT, padx=5, pady=5)
        # Add input for question label
        entryStep = tkinter.Entry(stepFrame)
        entryStep.pack(fill=tkinter.X, padx=5, expand=True)

        # button to add range question
        buttonFrame = tkinter.Frame(questionFrame)
        buttonFrame.pack()
        addButton = tkinter.Button(buttonFrame, text="Add Question",
                                   command=lambda: self.btnAddQuestion(
                                       'range', entryQuestionName.get(), entryQuestionLabel.get(), "",
                                       ("start={} end={} step={}".format(entryStart.get(), entryEnd.get(),
                                                                         entryStep.get())),  # add parameters in
                                       ""))
        addButton.pack(side=tkinter.RIGHT, padx=5, pady=5)

    def addChoiceDisplay(self, choiceFrame):
        # add choice and option of follow up question if that choice picked
        choiceOptFrame = tkinter.Frame(choiceFrame)
        choiceOptFrame.pack(fill=tkinter.X)

        addFollowUpQuestion = tkinter.IntVar()
        followUp = tkinter.Checkbutton(choiceOptFrame, text="Follow Up Question", variable=addFollowUpQuestion,
                                       onvalue=1, offvalue=0)
        followUp.pack(side=tkinter.RIGHT, padx=5, pady=5)

        choiceEntry = tkinter.Entry(choiceOptFrame)
        choiceEntry.pack(fill=tkinter.X, padx=5, pady=5)

    def displaySelectOptions(self, questionFrame, entryQuestionName, entryQuestionLabel):
        """ Displays follow up questions for select_ types allowing user to add choices for questions.
            can add as many choices as needed """
        choiceOptionFrame = tkinter.Frame(questionFrame)
        choiceOptionFrame.pack(fill=tkinter.X)
        tkinter.Label(choiceOptionFrame, text="Enter choice options", width=20).pack(side=tkinter.LEFT, padx=5, pady=5)

        # user enters as many choices as would like here
        choicesFrame = tkinter.Frame(questionFrame)
        choicesFrame.pack(fill=tkinter.X)
        addButton = tkinter.Button(choiceOptionFrame, text="add choice",
                                   command=lambda: self.addChoiceDisplay(choicesFrame))
        addButton.pack(side=tkinter.RIGHT, padx=5, pady=5)

        # button to add select question
        # TODO Add Choices to choice sheet in survey
        # TODO add relevancy options
        # TODO select one or select multiple + <choice-name>
        # TODO add option to name choices and so
        buttonFrame = tkinter.Frame(questionFrame)
        buttonFrame.pack()
        addButton = tkinter.Button(buttonFrame, text="Add Question",
                                   command=lambda: self.btnAddChoices(choicesFrame, questionFrame))
        addButton.pack(side=tkinter.BOTTOM, padx=5, pady=5)

    def getQuestionNameandLabel(self, questionFrame): # get question name and label from frame
        questionName = questionLabel = ""
        count = 1
        for frame in questionFrame.winfo_children():
            for widget in frame.winfo_children():
                if isinstance(widget, tk.Entry):
                    if count:
                        questionName += str(widget.get())  # first input is questionName
                        count -= 1
                    else:
                        questionLabel += str(widget.get())  # second input is questionLabel

        return questionName, questionLabel



    def btnAddChoices(self, choicesFrame, questionFrame):
        questionName, questionLabel = self.getQuestionNameandLabel(questionFrame)

        # list of rows to add to choices sheet
        choiceOptionRows = []
        rowCount = 1
        for widget in choicesFrame.winfo_children():
            if isinstance(widget, tk.Entry):    # choiceOptionRows = [[listName, name, label]....]
                choiceOptionRows.append([questionName, rowCount, widget.get()])
            rowCount += 1


        # TODO APPEND SURVEY SHEET HERE
        # append survey sheet
        surveySheet = wb.get_sheet_by_name(wb.get_sheet_names()[0])


        # append choices sheet
        choiceSheet = wb.get_sheet_by_name(wb.get_sheet_names()[1])

        # number of rows/columns in original worksheet
        nRows = choiceSheet.max_row

        # edit excel sheet
        r = nRows + 2   # leave a gap between different lists
        for row in choiceOptionRows:
            c = 1
            for choiceOption in row:
                choiceSheet.cell(row=r, column=c).value = choiceOption
                c += 1
            r += 1


        # save changes
        wb.save('survey-choices-update.xlsx')




    def btnAddQuestion(self, type, questionName, questionLabel, parameters, relevant, media):
        rowValues = [type, questionName, questionLabel, relevant, parameters, media]

        # Load in the workbook
        wb = load_workbook(filename)
        surveySheet = wb.get_sheet_by_name(wb.get_sheet_names()[0])

        # number of rows/columns in original worksheet
        nRows = surveySheet.max_row
        nCols = surveySheet.max_column

        for index in range(0, nCols):
            surveySheet.cell(row=nRows + 1, column=index + 1).value = rowValues[index]

        # save changes
        wb.save('survey-choices-update.xlsx')

    def save(self):
        pass

    def onExit(self):
        self.quit()

# survey file editing
filename = 'survey-choices-update.xlsx'
wb = load_workbook(filename)

def main():
    root = tkinter.Tk()
    # root.geometry("250x200+300+300")
    app = Example()
    root.mainloop()

if __name__ == '__main__':
    main()

