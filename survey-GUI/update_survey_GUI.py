#!/usr/bin/python3
# -*- coding: utf-8 -*
from tkinter import *
import convert_to_JSON
import write_to_file


class Example(Frame):
    def __init__(self):
        super().__init__()
        self.question_rows_to_add = []  # accumulate rows of questions to add to the survey sheet
        self.choice_rows_to_add   = []  # accumulate list of choice options to add to choices sheet
        self.initUI()

    def initUI(self):
        """ Initialise ui with menu bar upon opening """
        self.master.title("SurveyEditor")          # set window title

        menubar = Menu(self.master)  # add menu bar
        self.master.config(menu=menubar)

        # under "File"
        file_menu = Menu(menubar, tearoff=0)
        file_menu.add_command(label="New Question",
                             command=lambda:self.displayAddNewQuestion(self.master))  # form to add new question
        file_menu.add_command(label="Save", command=self.save)  # save changes
        file_menu.add_separator()
        file_menu.add_command(label="Quit", command=self.onExit)  # close window
        menubar.add_cascade(label="File", menu=file_menu)

        # under "View"
        view_menu = Menu(menubar, tearoff=0)
        view_menu.add_command(label="Question Additions", command=self.displayViewQuestionAdditions)
        view_menu.add_command(label="Choice Additions", command=self.displayViewChoiceAdditions)
        menubar.add_cascade(label="View", menu=view_menu)

        # under "Help"
        help_menu = Menu(menubar, tearoff=0)
        help_menu.add_command(label="About...", command=self.displayHelpMenu)  # display help popup
        menubar.add_cascade(label="Help", menu=help_menu)

    def displayViewQuestionAdditions(self):
        """ Display pop up menu showing current question additions """
        view_popup = Tk()  # pop-up
        view_popup.iconbitmap('images/magnifying_class.ico')  # set icon
        view_popup.wm_title("Question Additions")

        Label(view_popup, text="List of current questions to add to the survey").pack(side="top", fill="x")
        Label(view_popup, text="<question_name>: <question_label>").pack(side="top", fill="x", pady=15)

        # question = [type, name, label, ...]
        for question in self.question_rows_to_add:
            current_q = question[1] + ": " + question[2]
            Label(view_popup, text=current_q).pack(side="top", fill="x", pady=10)

    def displayViewChoiceAdditions(self):
        """ Display pop up menu showing current choice additions """
        view_popup = Tk()  # pop-up
        view_popup.iconbitmap('images/magnifying_class.ico')  # set icon
        view_popup.wm_title("Choice Additions")

        Label(view_popup, text="List of current choice options to add").pack(side="top", fill="x", pady=10)
        Label(view_popup, text="<choice_id>: <choice_label>").pack(side="top", fill="x", pady=15)

        # choice = [[[id, index, label],[...]], ....]
        for choice_list in self.choice_rows_to_add:  # each group of choices
            Label(view_popup, text=choice_list[0][0]).pack(side="top", fill="x", pady=10)
            for choice in choice_list:               # each option in choice_list
                Label(view_popup, text=choice[2]).pack(side="top", fill="x")
            Label(view_popup).pack(side="top", fill="x", pady=10)

    def displayHelpMenu(self):
        """ Display pop up menu explaining survey format """
        help_popup = Tk() # pop-up
        help_popup.iconbitmap('images/magnifying_class.ico')    # set icon

        # Parse about message from textfile
        about_message = open("files/about.txt", "r").readline()
        help_popup.wm_title("About...")
        Label(help_popup, text=about_message, wraplength=400).pack(side="top", fill="x", pady=10)
        help_popup.mainloop()

    def displayAddNewQuestion(self, q_frame):
        self.clearFrame(q_frame)

        # Form to add new question
        q_type_frame = Frame(q_frame)
        q_type_frame.pack(fill=X)

        # Display options for different types of question
        Label(q_type_frame, text="Question Type", width=20).pack(side=LEFT, padx=5, pady=5)
        type_opt_chosen = StringVar(q_type_frame) # type_opt_chosen.get() retrieves chosen option

        question_types = {'text', 'decimal', 'integer', 'select_one', 'select_multiple', 'range'}
        OptionMenu(q_type_frame, type_opt_chosen, *question_types).pack(fill=X, padx=5, expand=True)
        # track type of question chosen and display corresponding question form
        type_opt_chosen.trace("w", lambda name, index, mode,
                                type_opt_chosen=type_opt_chosen: self.checkTypeOptionChosen(type_opt_chosen, q_frame))

    def clearAll(self, q_frame):
        """ Clears entire frame contents """
        for widget in q_frame.winfo_children():  # iterate over widgets in the frame
            # if (isinstance(widget, Frame)):
            for frame in widget.winfo_children():
                if q_frame.master is None:       # main question frame
                    frame.pack_forget()
                if not q_frame.master is None:
                    frame.destroy()



    def clearFrame(self, q_frame):
        """ Clears frame contents except question_type selector """
        flag = False
        for widget in q_frame.winfo_children():  # iterate over widgets in the frame
            if (isinstance(widget, Frame)):
                for frame in widget.winfo_children():
                    if q_frame.master is None:  # main question frame
                        if not(str(frame).startswith('.!frame.')):
                            frame.pack_forget()
                            frame.destroy()
                            widget.destroy()
                    else:                       # follow up question
                        for f in frame.winfo_children():
                            if flag:
                                f.destroy()
                                frame.pack_forget()
                                widget.pack_forget()
                            if (isinstance(f, Menu)):
                                flag = True
        pass

    def checkTypeOptionChosen(self, type_opt_chosen, q_frame):
        """ Tracks the type_opt_chosen and displays form accordingly """
        type_opt = type_opt_chosen.get() # type of current question

        self.clearFrame(q_frame)                       # clears frame if different option chosen
        inner_q_frame = Frame(q_frame)
        inner_q_frame.pack(fill=X, side=TOP)

        q_type_frame = Frame(inner_q_frame)            # question_type
        q_type_frame.pack(fill=X)
        Label(q_type_frame, text="Question Type", width=20).pack(side=LEFT, padx=5, pady=5)
        Label(q_type_frame, text=type_opt).pack(fill="x", padx=5, pady=5)

        q_name_frame = Frame(inner_q_frame)            # question_name
        q_name_frame.pack(fill=X)
        Label(q_name_frame, text="Question Name", width=20).pack(side=LEFT, padx=5, pady=5)
        Entry(q_name_frame).pack(fill=X, padx=5, expand=True)

        q_label_frame = Frame(inner_q_frame)           # question_label
        q_label_frame.pack(fill=X)
        Label(q_label_frame, text="Question Label", width=20).pack(side=LEFT, padx=5, pady=5)
        Entry(q_label_frame).pack(fill=X, padx=5, expand=True)

        if (type_opt == 'range'):                     # get range parameters
            self.displayRangeOptions(inner_q_frame)
        elif (type_opt.startswith('select_')):        # get select_ choice parameters
            self.displaySelectOptions(inner_q_frame)

        if q_frame.master is None: # main question "Add" button
            # Button to add question
            btn_frame = Frame(q_frame)
            btn_frame.pack()
            add_btn = Button(btn_frame, text="Add Question",
                                       command=lambda: self.btnAddQuestion(q_frame, type_opt_chosen))
            add_btn.pack(side=BOTTOM, padx=5, pady=5)

    def displayRangeOptions(self, question_frame):
        """ user must enter "start" "end" "step" parameters for range """
        range_frame = Frame(question_frame)
        range_frame.pack(fill=X)
        Label(range_frame, text="Enter range parameters", width=20).pack(side=LEFT, padx=5, pady=5)

        # label for start
        start_frame = Frame(question_frame)
        start_frame.pack(fill=X)
        Label(start_frame, text="Start of range", width=20).pack(side=LEFT, padx=5, pady=5)
        Entry(start_frame).pack(fill=X, padx=5, expand=True)

        # label for end
        end_frame = Frame(question_frame)
        end_frame.pack(fill=X)
        Label(end_frame, text="End of range", width=20).pack(side=LEFT, padx=5, pady=5)
        Entry(end_frame).pack(fill=X, padx=5, expand=True)

        # label for step
        step_frame = Frame(question_frame)
        step_frame.pack(fill=X)
        Label(step_frame, text="Step", width=20).pack(side=LEFT, padx=5, pady=5)
        Entry(step_frame).pack(fill=X, padx=5, expand=True)

    def displaySelectOptions(self, question_frame):
        # display form for select_ questions allowing user to add in as many choices
        choice_opt_frame = Frame(question_frame)
        choice_opt_frame.pack(fill=X)

        Label(choice_opt_frame, text="Enter choice options", width=20).pack(side=LEFT, padx=5, pady=5)
        Button(choice_opt_frame, text="add choice", command=lambda: self.addChoiceDisplay(question_frame))\
            .pack(side=RIGHT, padx=5, pady=5)

    def addChoiceDisplay(self, question_frame):
        choice_opt_frame = Frame(question_frame)     # Add choice options
        choice_opt_frame.pack(fill=X)
        follow_up_q_frame = Frame(question_frame)    # Add follow up questions for certain choices
        follow_up_q_frame.pack(fill=X)

        add_followup_q = IntVar(choice_opt_frame)
        followup = Checkbutton(choice_opt_frame, text="Follow Up Question", variable=add_followup_q, onvalue=1, offvalue=0)
        followup.pack(side=RIGHT, padx=5, pady=5)
        # Track whether or not follow up chosen for that question
        add_followup_q.trace("w", lambda name, index, mode,
                                              add_followup_q=add_followup_q: self.displayFollowUpQuestion(
            follow_up_q_frame, add_followup_q))
        choice_entry = Entry(choice_opt_frame)  # Input choice option label
        choice_entry.pack(padx=5, pady=5)

    def displayFollowUpQuestion(self, choice_opt_frame, add_followup_q):
        """ Display new frame containing follow up question """
        for widget in choice_opt_frame.winfo_children():  # clear if unchecked
            if (isinstance(widget, Frame)):
                for frame in widget.winfo_children():
                    for f in frame.winfo_children():
                        f.destroy()
                        frame.pack_forget()
                        widget.pack_forget()

        if (add_followup_q.get()):  # if option selected
            self.displayAddNewQuestion(choice_opt_frame)

    def btnAddQuestion(self, q_frame, type_opt_chosen):
        """ Called when "Add Question" button clicked """
        q_type = type_opt_chosen.get()   # parse question according to their type
        self.parseQuestionForm(q_frame, str(q_type))
        # self.clearFrame(q_frame)         # resets frame
        self.clearAll(q_frame)

    def parseQuestionForm(self, q_frame, q_type):
        q_form_entries   = []  # get text entries from form
        followup_frames = []  # get follow-up questions from form
        for widget in q_frame.winfo_children():              # contents of entry boxes
            for child_widget in widget.winfo_children():
                for cchild_widget in child_widget.winfo_children():
                    if isinstance(cchild_widget,Entry):     # top level text entry
                        q_form_entries.append(cchild_widget.get())
                    elif isinstance(cchild_widget, Frame):  # follow-up question
                        followup_frames.append(cchild_widget)

        if q_type=='range':
            self.parseRangeQuestion(q_type, q_form_entries)                   # range question
        elif q_type.startswith('select_'):
            self.parseSelectQuestion(q_type, q_form_entries, followup_frames) # select question with possible follow-ups
        else:
            self.parseInputQuestion(q_type, q_form_entries)                   # integer/decimal/text question

    def parseRangeQuestion(self, q_type, range_entry):
        """ Parse form entries and store relevant rows in self.question_rows_to_add """
        # rangeEntry = [<qName>, <qLabel>, <start>, <step>, <end>]
        parameters = "start="+range_entry[2] + " end="+range_entry[3] + " step="+range_entry[4]

        # question = [q_type, qName, qLabel, relevant, parameters, media]
        self.addQuestionRow(q_type, range_entry[0], range_entry[1], '', parameters, '')

    def parseInputQuestion(self, q_type, input_entry):
        """ Parse form entries and store relevant rows in self.question_rows_to_add """
        # inputEntry = [<qName>, <qLabel>]
        self.addQuestionRow(q_type, input_entry[0], input_entry[1], '', '', '')

    def parseSelectQuestion(self, q_type, select_entry, follow_up_frames):
        """ Parse form entries and store relevant rows in self.question_rows_to_add """
        # select_entry = [<q_name>, <q_label>, <choiceopt1> , <choiceopt2> ....]
        q_name = select_entry[0]
        q_label = select_entry[1]
        choice_entries = select_entry[2:]  # rest of entries are choice options

        select_type = q_type + " " + q_name    # e.g. select_one gender
        self.addQuestionRow(select_type, q_name, q_label, '', '', '')
        self.addChoicesRows(q_name, choice_entries)

        current_choice = 1
        for frame in follow_up_frames:
            for widget in frame.winfo_children():
                for w in widget.winfo_children():
                    if (str(w).endswith('!label2')):
                        followup_type = (w.cget("text"))                    # get type of follow-up question
                        if q_type=='select_one':
                            relevance = "{%s}='%d'" % (q_name, current_choice)             # e.g ${occupation} = '1'
                        else:
                            relevance = "selected(${%s}, '%d')" % (q_name, current_choice) # e.g. selected(${farmer_type}, '4')
                        self.addQuestionRow('begin_group','','', relevance,'','')
                        self.parseQuestionForm(frame.master, followup_type)
                        self.addQuestionRow('end_group', '', '', '', '', '')
                        current_choice += 1

    def addQuestionRow(self, q_type, q_name, q_label, relevant, parameters, media):
        """ Add new question to self.question_rows_to_add """
        rowValues = [q_type, q_name, q_label, relevant, parameters, media]
        self.question_rows_to_add.append(rowValues)

    def addChoicesRows(self, q_name, choice_options):
        """ Add new list of choice options to self.choice_rows_to_add """
        choice_option_rows = [] # = [[listName, name, label], [..], ....]
        index = 1
        for choice in choice_options:
            choice_option_rows.append([q_name, index, choice])
            index += 1
        self.choice_rows_to_add.append(choice_option_rows)

    def save(self):
        # """ Save changes to excel workbook """
        filename = 'files/survey-choices-update.xlsx'

        # Write question additions to file
        write_to_file.writeQuestions(filename, self.question_rows_to_add)
        write_to_file.writeChoices(filename, self.choice_rows_to_add)

        # Generate JSON objects from file
        convert_to_JSON.convertQuestionsToJSON(filename)    # build survey.json
        convert_to_JSON.convertChoicesToJSON(filename)      # build choices.json

        # Clear current cache
        self.question_rows_to_add = []
        self.choice_rows_to_add = []

    def onExit(self):
        self.quit() # close window

def main():
    root = Tk()
    root.iconbitmap('images/magnifying_class.ico')
    app = Example()
    root.mainloop()

if __name__ == '__main__':
    main()

