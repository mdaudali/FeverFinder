#!/usr/bin/python3
# -*- coding: utf-8 -*
from tkinter import Tk, Frame, Menu, ttk
import tkinter
import tkinter as tk



class Example(tkinter.Frame):
    def __init__(self):
        """ initialise UI with menu bar upon opening """
        super().__init__()
        self.initUI()

    def initUI(self):
        self.master.title("Survey") # set window title

        menubar = tkinter.Menu(self.master) # add menu bar
        self.master.config(menu=menubar)

        # under "File"
        fileMenu = tkinter.Menu(menubar, tearoff=0)
        fileMenu.add_command(label="New Question", command=self.addNewQuestion)
        fileMenu.add_command(label="Save", command=self.save)
        fileMenu.add_separator()
        fileMenu.add_command(label="Quit", command=self.onExit)
        menubar.add_cascade(label="File", menu=fileMenu)

        # under "Help"
        helpMenu = tkinter.Menu(menubar, tearoff=0)
        helpMenu.add_command(label="About...", command=self.displayHelpMenu)
        menubar.add_cascade(label="Help", menu=helpMenu)

    def displayHelpMenu(self):
        helpPopup = tk.Tk()
        helpPopup.wm_title("About...")  # TODO wrap text
        label = ttk.Label(helpPopup,
                          text="add stuff into a textfile explaining the excel survey and maybe about the project too?")
        label.pack(side="top", fill="x", pady=10)
        helpPopup.mainloop()

    def displayRangeOptions(self, questionFrame):
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

    def addChoiceDisplay(self, questionFrame):
        tkinter.Label(questionFrame, text="addchoice stuff here", width=20).pack()

    def displaySelectOptions(self, questionFrame):
        """ Displays follow up questions for select_ types allowing user to add choices for questions.
            can add as many choices as needed """
        choiceFrame = tkinter.Frame(questionFrame)
        choiceFrame.pack(fill=tkinter.X)
        tkinter.Label(choiceFrame, text="Enter choice options", width=20).pack(side=tkinter.LEFT, padx=5, pady=5)
        addButton = tkinter.Button(choiceFrame, text="add choice", command= lambda: self.addChoiceDisplay(questionFrame))
        addButton.pack(side=tkinter.RIGHT, padx=5, pady=5)

    def checkTypeOptionChosen(self, typeOptionChosen):
        """ traces the typeOptionChosen and acts accordingly """
        typeOption = typeOptionChosen.get()

        for widget in self.master.winfo_children():
            if isinstance(widget, tk.Frame):
                for frame in widget.winfo_children():
                    if not(str(frame).startswith('.!frame.')):
                        frame.destroy()

        questionFrame = tkinter.Frame(self.master)
        questionFrame.pack(fill=tkinter.X)

        # questionName
        questionNameFrame = tkinter.Frame(questionFrame)
        questionNameFrame.pack(fill=tkinter.X)
        # Add label asking for question name
        lblQuestionName = tkinter.Label(questionNameFrame, text="Question Name", width=20)
        lblQuestionName.pack(side=tkinter.LEFT, padx=5, pady=5)
        # Add input for question name
        entryQuestionName = tkinter.Entry(questionNameFrame)
        entryQuestionName.pack(fill=tkinter.X, padx=5, expand=True)

        # questionLabel
        questionLabelFrame = tkinter.Frame(questionFrame)
        questionLabelFrame.pack(fill=tkinter.X)
        # Add label asking for question label
        lblQuestionLabel = tkinter.Label(questionLabelFrame, text="Question Label", width=20)
        lblQuestionLabel.pack(side=tkinter.LEFT, padx=5, pady=5)
        # Add input for question label
        entryQuestionLabel = tkinter.Entry(questionLabelFrame)
        entryQuestionLabel.pack(fill=tkinter.X, padx=5, expand=True)

        if (typeOption == 'range'):
            self.displayRangeOptions(questionFrame)
        elif (typeOption.startswith('select_')):
            self.displaySelectOptions(questionFrame)


        # button to add question
        buttonFrame = tkinter.Frame(questionFrame)
        buttonFrame.pack()
        addButton = tkinter.Button(buttonFrame, text="Add")
        addButton.pack(side=tkinter.RIGHT, padx=5, pady=5)

        # print()
        # print()
        # for widget in self.master.winfo_children():
        #     if isinstance(widget, tk.Frame):
        #         for frame in widget.winfo_children():
        #             print(str(frame))

    def addNewQuestion(self):
        frameQType = tkinter.Frame(self.master)
        frameQType.pack(fill=tkinter.X)

        # pick question type - display correct question format accordingly
        lblQuestionType = tkinter.Label(frameQType, text="Question Type", width=20)
        lblQuestionType.pack(side=tkinter.LEFT, padx=5, pady=5)

        QuestionTypes = {'text', 'decimal', 'integer', 'select_one', 'select_multiple', 'range'}
        typeOptionChosen = tkinter.StringVar(self.master)
        typeOptionChosen.set('text')  # set the default option
        QuestionTypesOptions = tkinter.OptionMenu(frameQType, typeOptionChosen, *QuestionTypes)
        QuestionTypesOptions.pack(fill=tkinter.X, padx=5, expand=True)

        # track type of question chosen and display format of question below
        typeOptionChosen.trace("w", lambda name, index, mode,
                                           typeOptionChosen=typeOptionChosen: self.checkTypeOptionChosen(typeOptionChosen))

    def save(self):
        pass

    def onExit(self):
        self.quit()



def main():
    root = tkinter.Tk()
    # root.geometry("250x200+300+300")
    app = Example()
    root.mainloop()

if __name__ == '__main__':
    main()