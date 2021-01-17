from PollNameMatcher import PollNameMatcher
from AnswerKey import AnswerKey
from Student import Student
from os import close
from numpy.core.numeric import NaN
import pandas as pd
import numpy as np


class AnswerKeyReader():

    def readAnswerKey(self, polls, inputFile):
        keyReader = pd.read_excel(inputFile)
        keyReader = keyReader.replace(np.nan, "", regex=True)
        keyReader.rename(columns={"AnswerKeyFile": "Questions"}, inplace=True)
        keyReader.rename(columns={"Unnamed: 1": "Answers"}, inplace=True)

        answerKey = None
        matcher = PollNameMatcher()

        for i in range(0, len(keyReader)):
            if(answerKey is None):
                answerKey = AnswerKey(keyReader.loc[i][0])
            elif(len(keyReader.loc[i][1]) == 0):
                matcher.match(polls, answerKey)
                answerKey = AnswerKey(keyReader.loc[i][0])
            else:
                answers = keyReader.loc[i][1].split(";")
                answerKey.add_question_answer(keyReader.loc[i][0], answers)

        matcher.match(polls, answerKey)
