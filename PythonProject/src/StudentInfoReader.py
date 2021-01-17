from Student import Student
from os import close
from numpy.core.numeric import NaN
import pandas as pd
import numpy as np


class StudentInfoReader():

    def readStudentList(self, zoomPoll, inputFile):
        bysReader = pd.read_excel(inputFile)
        bysReader = bysReader.replace(np.nan, "", regex=True)
        bysReader.rename(columns={"Unnamed: 2": "StudentNo"}, inplace=True)
        bysReader.rename(columns={"Unnamed: 4": "Name"}, inplace=True)
        bysReader.rename(columns={"Unnamed: 7": "Surname"}, inplace=True)
        bysReader.rename(columns={"Unnamed: 10": "Remark"}, inplace=True)

        for i in range(1, len(bysReader)):

            if(bysReader.loc[i, ["StudentNo", "Name", "Surname", "Remark"]][0].isnumeric()):
                studentNo = bysReader.loc[i, [
                    "StudentNo", "Name", "Surname", "Remark"]][0]
                name = (
                    bysReader.loc[i, ["StudentNo", "Name", "Surname", "Remark"]][1])
                name = name.translate(name.maketrans(
                    "çğıöşüÇĞİÖŞÜ", "cgiosucgiosu")).lower()
                surname = (
                    bysReader.loc[i, ["StudentNo", "Name", "Surname", "Remark"]][2])
                surname = surname.translate(surname.maketrans(
                    "çğıöşüÇĞİÖŞÜ", "cgiosucgiosu")).lower()
                remark = bysReader.loc[i, [
                    "StudentNo", "Name", "Surname", "Remark"]][3]
                student = Student(studentNo, name, surname, None, remark)
                zoomPoll.students.append(student)
