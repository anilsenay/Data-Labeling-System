from AnswerMatcher import AnswerMatcher
import os
from AnswerKeyReader import AnswerKeyReader
from PollReader import PollReader
from StudentInfoReader import StudentInfoReader
from ZoomPoll import ZoomPoll
from StudentMatcher import StudentMatcher
from StudentAnalysis import StudentAnalysis
from ExcelWriter import ExcelWriter
# -*- coding: utf-8 -*-


def main():
    studentInfoReader = StudentInfoReader()
    zoomPoll = ZoomPoll()

    filename = "./Data/CES3063_Fall2020_rptSinifListesi.XLS"
    # fill bys students list in ZoomPoll
    studentInfoReader.readStudentList(zoomPoll, filename)

    pollReader = PollReader()
    arr = os.listdir('./Data/Polls')
    for i in range(len(arr)):
        # poll objects are created and polls arraylist will be filled in zoompoll
        pollReader.readPoll(zoomPoll, "./Data/Polls/" + arr[i])

    zoomPoll.count_attendance_and_quiz_polls()

    # match s tudents
    studentMatcher = StudentMatcher()
    for poll in zoomPoll.polls:
        for attended_std in poll.studentsAttended:
            # Add emails of the students.
            std = studentMatcher.find_student(zoomPoll, attended_std)
            if(std == None):
                print(attended_std.name + " " + attended_std.surname + " " +
                      "could not found.")

    # poll name matcher
    answerKeyReader = AnswerKeyReader()
    arr = os.listdir('./Data/AnswerKeys')
    for i in range(len(arr)):
        answerKeyReader.readAnswerKey(
            zoomPoll.polls, "./Data/AnswerKeys/" + arr[i])

    answerMatcher = AnswerMatcher()

    for poll in zoomPoll.polls:
        for question in poll.questions:
            answerMatcher.find_answer(zoomPoll, question)

    studentAnalysis = StudentAnalysis(zoomPoll)
    studentAnalysis.setStudentAnswerTrue()
    studentAnalysis.calculate_attendance()
    studentAnalysis.set_attendance_percentage()
    studentAnalysis.calculate_score()
    studentAnalysis.calculate_total_grade()

    excel_writer = ExcelWriter()
    excel_writer.attendance_writer(zoomPoll)
    excel_writer.student_success_writer(zoomPoll)

    excel_writer.question_success_writer(zoomPoll)
    excel_writer.poll_grades_of_students(zoomPoll)


if __name__ == '__main__':
    main()
