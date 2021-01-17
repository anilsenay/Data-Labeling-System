from StudentAnswer import StudentAnswer
import pandas as pd
import numpy as np
from AnswerKeyReader import AnswerKeyReader
from PollReader import PollReader
from StudentInfoReader import StudentInfoReader
from ZoomPoll import ZoomPoll
from StudentMatcher import StudentMatcher
from StudentAnalysis import StudentAnalysis
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.ticker import StrMethodFormatter


class ExcelWriter(object):

    def __init__(self):
        pass

    def attendance_writer(self, zoom_poll):
        writer = pd.ExcelWriter(
            './Outputs/Attendance/Attendance.xlsx', engine='xlsxwriter')
        student_name_arr = []
        student_surname_arr = []
        attendance_arr = []
        attendance_perc_arr = []

        for bys_student in zoom_poll.students:
            attendance_arr.append(bys_student.attendance)
            attendance_perc_arr.append(bys_student.attendance_percentage)
            student_name_arr.append(bys_student.name)
            student_surname_arr.append(bys_student.surname)

        df = pd.DataFrame({'Student Name': student_name_arr,
                           'Student Surname': student_surname_arr,
                           'Attendance Poll': zoom_poll.attendance_polls,
                           'Attendance Rate': attendance_arr,
                           'Attendance Percentage': attendance_perc_arr})

        df.to_excel(writer, sheet_name='Sheet1', index=False)
        writer.save()

    def student_success_writer(self, zoom_poll):
        for poll in zoom_poll.polls:
            if(poll.isAttendance):
                continue
            poll_name = "./Outputs/Polls/" + poll.pollName + \
                "-" + poll.submittedDate + ".xlsx"
            writer = pd.ExcelWriter(poll_name, engine='xlsxwriter')
            studentSuccess = []
            for bys_student in zoom_poll.students:
                row = [0] * (len(poll.questions)+6)
                row[0] = bys_student.id
                row[1] = (bys_student.name)
                row[2] = (bys_student.surname)
                row[3] = (bys_student.remark)
                for attended_student in poll.studentsAttended:
                    studentMatcher = StudentMatcher()
                    if(bys_student == studentMatcher.find_student(zoom_poll, attended_student)):
                        q = 1
                        studentRate = 0
                        for question in poll.questions:
                            row[3+q] = 0
                            for answer in poll.studentAnswers:
                                if(answer.question == question and answer.student == attended_student):
                                    isSuccess = answer.isTrue
                                    if(isSuccess):
                                        row[3+q] = 1
                                        studentRate += 1
                                    else:
                                        row[3+q] = 0
                            q += 1
                        row[q+3] = (studentRate)
                        row[q+4] = str(100 * (studentRate /
                                              len(poll.questions))) + "%"

                studentSuccess.append(row)

            studentSuccess = np.array(studentSuccess)

            dic = {'Student Number': studentSuccess[:, 0], 'Student Name': studentSuccess[:, 1],
                   'Student Surname': studentSuccess[:, 2], 'Remark': studentSuccess[:, 3]}
            for i in range(len(poll.questions)):
                dic['Q'+str(i+1)] = studentSuccess[:, i+4]
            numOfQuestions = len(poll.questions)
            dic['Number Of Questions'] = numOfQuestions
            dic['Success Rate'] = studentSuccess[:, numOfQuestions+4]
            dic['Success Percentage'] = studentSuccess[:, numOfQuestions+5]

            df = pd.DataFrame(dic)
            df.to_excel(writer, sheet_name='Sheet1', index=False)

            writer.save()

    def question_success_writer(self, zoomPoll):
        for poll in zoomPoll.polls:
            if(poll.isAttendance):
                continue
            poll_name = "./Outputs/Questions/" + poll.pollName + \
                "-" + poll.submittedDate + ".xlsx"
            writer = pd.ExcelWriter(poll_name, engine='xlsxwriter')
            question_index = -1
            for question in poll.questions:
                question_index += 1
                question_name = ""
                wrong_selec = []
                correct_selec = []
                num_of_wrong_selec = []
                num_of_correct_selec = []
                question_name = question.question_content
                for true_answer in question.trueAnswers:
                    for choice in question.choices:
                        if(str(true_answer) == choice.choice_content):
                            correct_selec.append(choice.choice_content)
                            num_of_correct_selec.append(
                                choice.num_of_selection)
                            choice_per = 100.0 * \
                                float(choice.num_of_selection) / \
                                len(poll.studentsAttended)

                        else:
                            wrong_selec.append(choice.choice_content)
                            num_of_wrong_selec.append(choice.num_of_selection)

                explodes = []
                dic = {'Choice': [], 'Values': []}
                for i in range(len(correct_selec)):
                    dic['Choice'].append(correct_selec[i])
                    dic['Values'].append(num_of_correct_selec[i])
                    explodes.append(
                        {'border': {'color': 'white', 'width': 2}, 'fill': {'color': 'green'}})
                for i in range(len(wrong_selec)):
                    dic['Choice'].append(wrong_selec[i])
                    dic['Values'].append(num_of_wrong_selec[i])
                    explodes.append(
                        {'border': {'color': 'white', 'width': 2}, 'fill': {'color': 'red'}})

                df = pd.DataFrame(dic)
                df.to_excel(writer, sheet_name="Question" +
                            str(question_index))
                workbook = writer.book
                worksheet = writer.sheets["Question" +
                                          str(question_index)]
                chart = workbook.add_chart({'type': 'pie'})
                chart.add_series({
                    'name': question.question_content,
                    'categories': '=' + "Question" + str(question_index) + '!B2:B' + str(len(question.choices)+1),
                    'values': '=' + "Question" + str(question_index) + '!C2:C' + str(len(question.choices)+1),
                    'data_labels': {'percentage': False, 'value': True, 'leader_lines': True},
                    'points': explodes,
                })
                chart.set_size({'x_scale': 2, 'y_scale': 2})

                worksheet.insert_chart(
                    'H4', chart)

            writer.save()

    def poll_grades_of_students(self, zoomPoll):
        writer = pd.ExcelWriter(
            "./Outputs/poll_grades.xlsx", engine='xlsxwriter')
        columns = []
        date_row = [''] * (len(zoomPoll.polls)+4)
        question_row = [''] * (len(zoomPoll.polls)+4)
        percentage = [''] * (len(zoomPoll.polls)+4)
        index = -1
        for poll in zoomPoll.polls:
            if(not poll.isAttendance):
                index += 1

                date_row[index + 4] = (poll.submittedDate)
                question_row[index + 4] = (len(poll.questions))
                percentage[index + 4] = "Success: " + str(100.00 * round(poll.totalScore /
                                                                         (len(poll.questions) * len(poll.studentsAttended)), 2))
        columns.append(date_row)
        columns.append(question_row)
        columns.append(percentage)

        for bys_student in zoomPoll.students:
            i = 1
            row = [0] * (len(zoomPoll.polls)+4)
            for poll in zoomPoll.polls:
                if(not poll.isAttendance):
                    poll_name = poll.pollName
                    poll_date = poll.submittedDate
                    poll_num_of_questions = len(poll.questions)
                    row[0] = bys_student.id
                    row[1] = (bys_student.name)
                    row[2] = (bys_student.surname)
                    row[3] = (bys_student.remark)
                    score_for_each_std = 0
                    for attended_std in poll.studentsAttended:
                        studentMatcher = StudentMatcher()
                        if(bys_student == studentMatcher.find_student(zoomPoll, attended_std)):
                            score_for_each_std = attended_std.score
                            row[3 + i] = score_for_each_std
                    i += 1
            columns.append(row)

        columns = np.array(columns)

        dic = {'Student Number': columns[:, 0], 'Student Name': columns[:, 1],
               'Student Surname': columns[:, 2], 'Remark': columns[:, 3]}
        pollCount = 0
        for i in range(len(zoomPoll.polls)):
            if(not zoomPoll.polls[i].isAttendance):
                dic[zoomPoll.polls[i].pollName +
                    zoomPoll.polls[i].submittedDate] = columns[:, i+4]
                pollCount += 1
        df = pd.DataFrame(dic)
        df.to_excel(writer, sheet_name='Sheet1', index=False)

        writer.save()
