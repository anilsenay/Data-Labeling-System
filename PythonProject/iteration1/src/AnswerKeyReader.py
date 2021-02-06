from PollNameMatcher import PollNameMatcher
from AnswerKey import AnswerKey
from Student import Student
from os import close
from numpy.core.numeric import NaN
import pandas as pd
import numpy as np


class AnswerKeyReader():

    def readAnswerKey(self, polls, inputFile):
        file = open(inputFile, "r", encoding='utf-8').read().splitlines()

        answerKey = None
        question = None
        answers = []
        matcher = PollNameMatcher()

        for line in file:
            if(len(line) == 0 or line == "\n"):
                continue
            if(line.lstrip(' ')[:4] == "Poll"):
                if(answerKey is not None):
                    answerKey.add_question_answer(question, answers)
                    answers = []
                    question = None
                    matcher.match(polls, answerKey)

                answerKey = AnswerKey(line.lstrip(' ').split("\t")[0][line.find(":"):])
            elif(answerKey is None):
                continue
            elif(line.lstrip(' ')[:6] != "Answer"):
                if(question is not None):
                    answerKey.add_question_answer(question, answers)
                    answers = []
                question = line.split(" ", 1)[1].replace(" ( Single Choice)", "").replace(" ( Multiple Choice)", "")
            else:
                answers.append(line[line.find(":")+1:].lstrip(' '))

        answerKey.add_question_answer(question, answers)
        matcher.match(polls, answerKey)

        return
        

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
