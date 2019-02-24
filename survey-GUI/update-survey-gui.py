#!/usr/bin/python3
# -*- coding: utf-8 -*
from tkinter import Tk, Frame, Menu, ttk
import tkinter
import tkinter as tk

from openpyxl import load_workbook
from openpyxl.utils.dataframe import dataframe_to_rows

# todo why does select option disappear each time

class Example(tkinter.Frame):
    def __init__(self):
        """ initialise UI with menu bar upon opening """
        super().__init__()
        self.QuestionRowsToAdd = [] # accumulate rows of questions to add to the survey sheet
        self.ChoiceRowsToAdd = [] # accumulate list of choice options to add to choices sheet
        self.menubar = tkinter.Menu() # add menu bar to main window

        self.initUI()

    def initUI(self):
        self.master.title("Survey")  # set window title

        self.menubar = tkinter.Menu(self.master)  # add menu bar
        self.master.config(menu=self.menubar)

        # under "File"
        fileMenu = tkinter.Menu(self.menubar, tearoff=0)
        fileMenu.add_command(label="New Question",
                             command=lambda:self.displayAddNewQuestionMenu(self.master))  # form to add new question
        fileMenu.add_command(label="Save", command=self.save)  # save changes
        fileMenu.add_separator()
        fileMenu.add_command(label="Quit", command=self.onExit)  # close window
        self.menubar.add_cascade(label="File", menu=fileMenu)

        # under "View"
        viewMenu = tkinter.Menu(self.menubar, tearoff=0)
        viewMenu.add_command(label="Question Additions", command=self.displayViewQuestionAdditions)
        viewMenu.add_command(label="Choice Additions", command=self.displayViewChoiceAdditions)
        self.menubar.add_cascade(label="View", menu=viewMenu)

        # under "Help"
        helpMenu = tkinter.Menu(self.menubar, tearoff=0)
        helpMenu.add_command(label="About...", command=self.displayHelpMenu)  # display help popup
        self.menubar.add_cascade(label="Help", menu=helpMenu)

    def displayViewQuestionAdditions(self):
        """ Display pop up menu showing current question additions """
        viewPopup = tk.Tk()  # pop-up
        viewPopup.wm_title("Question Additions")

        # TODO
        ttk.Label(viewPopup, text="add some explanation here").pack(side="top", fill="x", pady=10)
        ttk.Label(viewPopup, text="<id>: <label>").pack(side="top", fill="x", pady=20)

        # question = [type, name, label, ...]
        for question in self.QuestionRowsToAdd:
            currentQ = question[1] + ": " + question[2]
            ttk.Label(viewPopup, text=currentQ).pack(side="top", fill="x", pady=10)

    def displayViewChoiceAdditions(self):
        """ Display pop up menu showing current choice additions """
        viewPopup = tk.Tk()  # pop-up
        viewPopup.wm_title("Choice Additions")

        # TODO
        ttk.Label(viewPopup, text="add some explanation here").pack(side="top", fill="x", pady=10)

        # choice = [[[id, index, label],[...]], ....]
        for choiceList in self.ChoiceRowsToAdd:  # each group of choices
            ttk.Label(viewPopup, text=choiceList[0][0]).pack(side="top", fill="x", pady=10)
            for choice in choiceList:
                ttk.Label(viewPopup, text=choice[2]).pack(side="top", fill="x")
            ttk.Label(viewPopup).pack(side="top", fill="x", pady=10)



    def displayHelpMenu(self):
        """ Display pop up menu explaining survey format """
        helpPopup = tk.Tk() # pop-up

        # Parse about message from textfile
        aboutMessage = open("files/about.txt", "r").readline()
        helpPopup.wm_title("About...")
        ttk.Label(helpPopup, text=aboutMessage, wraplength=400).pack(side="top", fill="x", pady=10)
        helpPopup.mainloop()

    def displayAddNewQuestionMenu(self, qFrame):
        """ Display form to add new question to survey """

        # Frame containing question form
        frameQType = tkinter.Frame(qFrame)
        frameQType.pack(fill=tkinter.X)

        # Display options for different types of question
        QuestionTypes = {'text', 'decimal', 'integer', 'select_one', 'select_multiple', 'range'}
        tkinter.Label(frameQType, text="Question Type", width=20).pack(side=tkinter.LEFT, padx=5, pady=5)
        typeOptionChosen = tkinter.StringVar(frameQType)   # typeOptionChosen.get() retrieves chosen option
        typeOptionChosen.set('text')                        # default type
        tkinter.OptionMenu(frameQType, typeOptionChosen, *QuestionTypes).pack(fill=tkinter.X, padx=5, expand=True)

        # Track type of question chosen and display corresponding question form
        typeOptionChosen.trace("w", lambda name, index, mode,
                                           typeOptionChosen=typeOptionChosen: self.checkTypeOptionChosen(typeOptionChosen, qFrame))

        # Button to add question
        buttonFrame = tkinter.Frame(qFrame)
        buttonFrame.pack()

        addButton = tkinter.Button(buttonFrame, text="Add Question",
                                   command=lambda: self.btnAddQuestion(qFrame, typeOptionChosen))
        addButton.pack(side=tkinter.RIGHT, padx=5, pady=5)

        # # button to add range question
        # buttonFrame = tkinter.Frame(questionFrame)
        # buttonFrame.pack()  # add 'parameters in format expected by app'
        # addButton = tkinter.Button(buttonFrame, text="Add Question",
        #                            command=lambda: self.btnAddQuestion(
        #                                'range', entryQuestionName.get(), entryQuestionLabel.get(), "",
        #                                ("start={} end={} step={}".format(entryStart.get(), entryEnd.get(),
        #                                                                  entryStep.get())),  #
        #                                ""))
        # addButton.pack(side=tkinter.RIGHT, padx=5, pady=5)

        # buttonFrame = tkinter.Frame(questionFrame)
        # buttonFrame.pack()
        # addButton = tkinter.Button(buttonFrame, text="Add Question",
        #                            command=lambda: self.btnAddChoices(choicesFrame, questionFrame, typeChosen))
        # addButton.pack(side=tkinter.BOTTOM, padx=5, pady=5)

    def clearFrame(self, qFrame):
        # clears frame contents except question_type selector and final end button
        flag = False
        for widget in qFrame.winfo_children():
            if (isinstance(widget, tkinter.Frame)):
                for frame in widget.winfo_children():
                    if qFrame.master is None:   # main question
                        if not (str(frame).startswith('.!frame.') or (isinstance(frame, tkinter.Button))):
                            frame.pack_forget()
                            frame.destroy()
                            widget.destroy()
                    else:                       #follow up question
                        for f in frame.winfo_children():
                            # TODO GET RID OF WEIRD WHITE SPACE
                            if flag:
                                f.destroy()
                                frame.pack_forget()
                            if (isinstance(f, tkinter.Menu)):
                                flag = True


    def checkTypeOptionChosen(self, typeOptionChosen, qFrame):
        """ Tracks the typeOptionChosen and displays form accordingly """
        typeOption = typeOptionChosen.get()

        self.clearFrame(qFrame)

        # Frame for question details
        questionFrame = tkinter.Frame(qFrame)
        questionFrame.pack(fill=tkinter.X, side=tkinter.TOP)
        # INPUT questionName
        questionNameFrame = tkinter.Frame(questionFrame)
        questionNameFrame.pack(fill=tkinter.X)
        tkinter.Label(questionNameFrame, text="Question Name", width=20).pack(side=tkinter.LEFT, padx=5, pady=5)
        entryQuestionName = tkinter.Entry(questionNameFrame)
        entryQuestionName.pack(fill=tkinter.X, padx=5, expand=True)
        # INPUT questionLabel
        questionLabelFrame = tkinter.Frame(questionFrame)
        questionLabelFrame.pack(fill=tkinter.X)
        tkinter.Label(questionLabelFrame, text="Question Label", width=20).pack(side=tkinter.LEFT, padx=5, pady=5)
        entryQuestionLabel = tkinter.Entry(questionLabelFrame)
        entryQuestionLabel.pack(fill=tkinter.X, padx=5, expand=True)

        if (typeOption == 'range'): # get range parameters
            self.displayRangeOptions(questionFrame, entryQuestionName, entryQuestionLabel)
        elif (typeOption.startswith('select_')): # get select_ choice parameters
            self.displaySelectOptions(questionFrame, typeOption)

    def displayRangeOptions(self, questionFrame, entryQuestionName, entryQuestionLabel):
        """ user must enter "start" "end" "step" parameters for range """
        rangeFrame = tkinter.Frame(questionFrame)
        rangeFrame.pack(fill=tkinter.X)
        tkinter.Label(rangeFrame, text="Enter range parameters", width=20).pack(side=tkinter.LEFT, padx=5, pady=5)

        # label for start
        startFrame = tkinter.Frame(questionFrame)
        startFrame.pack(fill=tkinter.X)
        tkinter.Label(startFrame, text="Start of range", width=20).pack(side=tkinter.LEFT, padx=5, pady=5)
        entryStart = tkinter.Entry(startFrame)
        entryStart.pack(fill=tkinter.X, padx=5, expand=True)

        # label for end
        endFrame = tkinter.Frame(questionFrame)
        endFrame.pack(fill=tkinter.X)
        tkinter.Label(endFrame, text="End of range", width=20).pack(side=tkinter.LEFT, padx=5, pady=5)
        entryEnd = tkinter.Entry(endFrame)
        entryEnd.pack(fill=tkinter.X, padx=5, expand=True)

        # label for step
        stepFrame = tkinter.Frame(questionFrame)
        stepFrame.pack(fill=tkinter.X)
        tkinter.Label(stepFrame, text="Step", width=20).pack(side=tkinter.LEFT, padx=5, pady=5)
        entryStep = tkinter.Entry(stepFrame)
        entryStep.pack(fill=tkinter.X, padx=5, expand=True)

    def displaySelectOptions(self, questionFrame, typeChosen):
        """ Displays questions for select_ types allowing user to add choices for questions.
            user can add as many choices as needed """
        choiceOptionFrame = tkinter.Frame(questionFrame)
        choiceOptionFrame.pack(fill=tkinter.X)
        tkinter.Label(choiceOptionFrame, text="Enter choice options", width=20).pack(side=tkinter.LEFT, padx=5, pady=5)
        tkinter.Button(choiceOptionFrame, text="add choice", command=lambda: self.addChoiceDisplay(questionFrame))\
            .pack(side=tkinter.RIGHT, padx=5, pady=5)

    def addChoiceDisplay(self, questionFrame):
        # add choice and option of follow up question if that choice picked
        choiceOptFrame = tkinter.Frame(questionFrame)
        choiceOptFrame.pack(fill=tkinter.X)

        followUpQuestionFrame = tkinter.Frame(questionFrame)
        followUpQuestionFrame.pack(fill=tkinter.X)

        # Can add a follow up question
        addFollowUpQuestion = tkinter.IntVar(choiceOptFrame)
        followUp = tkinter.Checkbutton(choiceOptFrame, text="Follow Up Question", variable=addFollowUpQuestion,
                                       onvalue=1, offvalue=0)
        followUp.pack(side=tkinter.RIGHT, padx=5, pady=5)
        # Track whether follow up chosen for that question
        addFollowUpQuestion.trace("w", lambda name, index, mode,
                                              addFollowUpQuestion=addFollowUpQuestion: self.displayFollowUpQuestion(
            followUpQuestionFrame, addFollowUpQuestion))

        # input choice option label
        choiceEntry = tkinter.Entry(choiceOptFrame)
        choiceEntry.pack(padx=5, pady=5)


    def displayFollowUpQuestion(self, choiceOptFrame, addFollowUpQuestion):
         # display new frame containing follow up question
         if (addFollowUpQuestion.get()): # if option selected
            self.followUpAddQuestion(choiceOptFrame)
         else:
             # TODO CLEAR CHILD FRAME
             pass

    def followUpAddQuestion(self, followUpPopup):
        # Frame containing question form
        frameQType = tkinter.Frame(followUpPopup)
        frameQType.pack(fill=tkinter.X)

        # Display options for different types of question
        QuestionTypes = {'text', 'decimal', 'integer', 'select_one', 'select_multiple', 'range'}
        lblQuestionType = tkinter.Label(frameQType, text="Question Type", width=20)
        lblQuestionType.pack(side=tkinter.LEFT, padx=5, pady=5)
        typeOptionChosen = tkinter.StringVar(followUpPopup)  # typeOptionChosen.get() retrieves chosen option
        typeOptionChosen.set('text')  # default type
        QuestionTypesOptions = tkinter.OptionMenu(frameQType, typeOptionChosen, *QuestionTypes)
        QuestionTypesOptions.pack(fill=tkinter.X, padx=5, expand=True)

        # Track type of question chosen and display corresponding question form
        typeOptionChosen.trace("w", lambda name, index, mode,
                                           typeOptionChosen=typeOptionChosen: self.checkTypeOptionChosen(
            typeOptionChosen, followUpPopup))

    def getQuestionNameandLabel(self, questionFrame):  # get question name and label from frame
        questionName = questionLabel = ""
        flag = True
        for frame in questionFrame.winfo_children():
            for widget in frame.winfo_children():
                if isinstance(widget, tk.Entry):
                    if flag:
                        questionName += str(widget.get())  # first input is questionName
                        flag = False
                    else:
                        questionLabel += str(widget.get())  # second input is questionLabel
        return questionName, questionLabel

    def btnAddQuestion(self, qFrame, typeOptionChosen):
        qType = typeOptionChosen.get()
        if (qType == 'range'):
            # parse range question
            self.parseRangeQuestion(qFrame, qType)
        elif (str(qType).startswith('select_')):
            # parse select_ question
            self.parseSelectQuestion(qFrame, qType)
        else:
            # parse input question
            self.parseInputQuestion(qFrame, qType)

    def parseRangeQuestion(self, qFrame, qType):
        rangeEntry = [] # = [<qName>, <qLabel>, <start>, <step>, <end>]

        # get contents of entry boxes in range question form
        for widget in qFrame.winfo_children():
            for child_widget in widget.winfo_children():
                for cchild_widget in child_widget.winfo_children():
                    if isinstance(cchild_widget,tkinter.Entry):
                        rangeEntry.append(cchild_widget.get())

        parameters = "start="+rangeEntry[2] + " end="+rangeEntry[3] + " step="+rangeEntry[4]
        self.addQuestionRow(qType, rangeEntry[0], rangeEntry[1], '', parameters, '')
        print(qType)

    def parseInputQuestion(self, qFrame ,qType):
        questionEntry = []  # = [<qName>, <qLabel>]
        # get contents of entry boxes in input question form
        for widget in qFrame.winfo_children():
            for child_widget in widget.winfo_children():
                for cchild_widget in child_widget.winfo_children():
                    if isinstance(cchild_widget,tkinter.Entry):
                        questionEntry.append(cchild_widget.get())

        self.addQuestionRow(qType, questionEntry[0], questionEntry[1], '', '', '')


    def parseSelectQuestion(self, qFrame, qType):
        questionEntry = [] # = [<qName>, <qLabel>]
        choiceOptions = [] # list of options for a select_ question

        for widget in qFrame.winfo_children():
            for child_widget in widget.winfo_children():
                for cchild_widget in child_widget.winfo_children():
                    # questionName and questionLabel
                    if isinstance(cchild_widget,tkinter.Entry):
                        questionEntry.append(cchild_widget.get())
                    else: # read in choices
                        for choice_widget in cchild_widget.winfo_children():
                            if isinstance(choice_widget, tkinter.Entry):
                                choiceOptions.append(choice_widget.get())

        qName = questionEntry[0]
        qLabel = questionEntry[1]

        selectType = qType + " " + qName # e.g. select_one gender
        self.addQuestionRow(selectType, qName, qLabel, '', '', '')
        self.addChoicesRows(qName, choiceOptions)



    def addQuestionRow(self, qType, qName, qLabel, relevant, parameters, media):
        rowValues = [qType, qName, qLabel, relevant, parameters, media]
        self.QuestionRowsToAdd.append(rowValues)

    def addChoicesRows(self, qName, choiceOptions):
        choiceOptionsRows = [] # = [[listName, name, label], [..], ....]
        index = 1
        for choice in choiceOptions:
            choiceOptionsRows.append([qName, index, choice])
            index += 1

        self.ChoiceRowsToAdd.append(choiceOptionsRows)

    def save(self):
        print(self.QuestionRowsToAdd)
        print(self.ChoiceRowsToAdd)
        # # TODO APPEND SURVEY SHEET HERE
        # surveySheet = wb.get_sheet_by_name(wb.get_sheet_names()[0])
        #
        # # append choices sheet
        # choiceSheet = wb.get_sheet_by_name(wb.get_sheet_names()[1])
        #
        # # number of rows/columns in original worksheet
        # nRows = choiceSheet.max_row
        #
        # # edit excel sheet
        # r = nRows + 2  # leave a gap between different lists
        # for row in choiceOptionRows:
        #     c = 1
        #     for choiceOption in row:
        #         choiceSheet.cell(row=r, column=c).value = choiceOption
        #         c += 1
        #     r += 1
        #
        # # save changes
        # wb.save(filename)

        # # Load in the workbook
        # wb = load_workbook(filename)
        # surveySheet = wb.get_sheet_by_name(wb.get_sheet_names()[0])
        #
        # # number of rows/columns in original worksheet
        # nRows = surveySheet.max_row
        # nCols = surveySheet.max_column
        #
        # for index in range(0, nCols):
        #     surveySheet.cell(row=nRows + 1, column=index + 1).value = rowValues[index]
        #
        # # save changes
        # wb.save(filename)

        pass

    def onExit(self):
        self.quit()


# survey file editing
filename = 'files/survey-choices-update.xlsx'
wb = load_workbook(filename)

def main():
    root = tkinter.Tk()
    # root.geometry("250x200+300+300")
    app = Example()
    root.mainloop()

if __name__ == '__main__':
    main()


# todo by default add all before end section
# todo instead of addding it to file straight away, accumulate in program then add
# todo view all changes to add on opening program
# todo seperate question names by hyphens if not already
# todo add choices each time add choice clicked - DIFFERENT TO BEFORE

# todo dont do it in pop up winodws do it as part of the same window??

# todo some way of indenting thats more understandable

# todo does adding simple questions work for each type??

# todo move add question button to the right?
# todo maybe indent each follow up q to be more understandable

# todo make add invisible at start
# todo question type disapears from follow ups!!

# todo do i need to convert qType to strings??

#  def btnAddChoices(self, choicesFrame, questionFrame, typeChosen):
#         questionName, questionLabel = self.getQuestionNameandLabel(questionFrame)
#
#         # list of rows to add to choices sheet
#         choiceOptionRows = []
#         rowCount = 1
#         # add choice options from window into list
#         for widget in choicesFrame.winfo_children():
#             for frame in widget.winfo_children():
#                 if isinstance(frame, tk.Entry):
#                     # choiceOptionRows = [[listName, name, label]....]
#                     choiceOptionRows.append([questionName, rowCount, frame.get()])
#                     rowCount += 1
#         self.ChoiceRowsToAdd.append(choiceOptionRows)
#         questionName, questionLabel = self.getQuestionNameandLabel(questionFrame)
#         self.btnAddQuestion((typeChosen + " " + questionName),  # e.g. select_one gender
#                             questionName, questionLabel, "","","")


# # todo change to use current frame
# # Clear contexts on text entries when click "add"
# for widget in self.master.winfo_children():
#     for cwidget in widget.winfo_children():
#         for ccwidget in cwidget.winfo_children():
#             if (isinstance(ccwidget, tkinter.Entry)):
#                 ccwidget.delete(0, 'end')
#             for cccwidget in ccwidget.winfo_children():
#                 if (isinstance(cccwidget, tkinter.Entry)):
#                     cccwidget.delete(0, 'end')