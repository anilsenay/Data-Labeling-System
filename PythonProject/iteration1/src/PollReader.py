
from StudentMatcher import StudentMatcher
from QuestionChoice import QuestionChoice
from ZoomPoll import ZoomPoll
from StudentAnswer import StudentAnswer
from numpy.core.numeric import full
from Question import Question
from Student import Student
from Poll import Poll
from os import close
import pandas as pd
import numpy as np
import chardet
import locale
locale.setlocale(locale.LC_ALL, 'tr_TR.utf8')


class PollReader():
    def readPoll(self, zoomPoll: ZoomPoll, inputFile):
        pollReader = pd.read_csv(inputFile, sep=",")
        pd.set_option('display.max_rows', 500)
        pd.set_option('display.max_columns', 500)
        pd.set_option('display.width', 20000)
        QuestionCounter = 0
        AnswerCounter = 0

        for i in range(len(pollReader.columns)):
            if (i >= 4):
                if(i % 2 == 0):
                    pollReader.rename(
                        columns={"Unnamed: "+str(i): "Question"+str(QuestionCounter)}, inplace=True)
                    QuestionCounter += 1
                else:
                    pollReader.rename(
                        columns={"Unnamed: "+str(i): "Answer"+str(AnswerCounter)}, inplace=True)
                    AnswerCounter += 1

        pollReader = pollReader.replace(np.nan, "", regex=True)

        poll = None
        questions = []

        for i in range(0, len(pollReader)):

            fullName = pollReader.loc[i][1].split()
            name = (" ".join(fullName[0:-1]))
            name = ''.join(i for i in name if not i.isdigit()).lstrip()
            name = (name.translate(name.maketrans(
                "çğıöşüÇĞİÖŞÜ", "cgiosucgiosu"))).lower()
            surname = (fullName[-1])
            surname = (surname.translate(surname.maketrans(
                "çğıöşüÇĞİÖŞÜ", "cgiosucgiosu"))).lower()
            email = pollReader.loc[i][2]
            date = pollReader.loc[i][3].split(",")[0]
            student = Student(0, name, surname, email, "")

            if(poll is None):
                poll = Poll()

            questionsInRow = []

            for j in range(4, len(pollReader.columns)):

                if(j % 2 == 0 and len(pollReader.loc[i][j]) > 0):
                    questionsInRow.append(pollReader.loc[i][j])

            if(not all(q in questions for q in questionsInRow)):
                if(len(questions) > 0):
                    zoomPoll.polls.append(poll)
                poll = Poll()
                questions.clear()

            poll.studentsAttended.append(student)
            if(len(poll.submittedDate) == 0):
                poll.submittedDate = date

            question = None
            for j in range(4, len(pollReader.columns)):
                if(j % 2 == 0):
                    # question
                    question = None
                    if(len(pollReader.loc[i][j]) == 0):
                        continue

                    for k in range(len(poll.questions)):
                        if (poll.questions[k].question_content == pollReader.loc[i][j]):
                            question = poll.questions[k]

                    if question is None:
                        question = Question(pollReader.loc[i][j])
                        poll.questions.append(question)
                        questions.append(pollReader.loc[i][j])
                else:
                    if(question is not None):
                        answers = pollReader.loc[i][j].split(";")
                        studentAnswer = StudentAnswer(student, question)

                        for k in range(len(answers)):
                            studentAnswer.insertAnswer(answers[k])
                            questionChoice = QuestionChoice(answers[k])
                            question.insertChoice(questionChoice)

                        poll.studentAnswers.append(studentAnswer)

        zoomPoll.polls.append(poll)
