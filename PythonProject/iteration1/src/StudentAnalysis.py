from StudentMatcher import StudentMatcher
from AnswerMatcher import AnswerMatcher


class StudentAnalysis(object):

    def __init__(self, zoom_poll):
        super().__init__()
        self.zoom_poll = zoom_poll
        self.sm = StudentMatcher()

    def is_attendance_poll(self, poll):
        for q in poll.questions:
            if(q.question_content == "Are you attending this lecture?"):
                poll.isAttendance = True
                return True
        return False

    # find attendance numbers of students that attended polls
    def calculate_attendance(self):
        for poll in self.zoom_poll.polls:
            if(self.is_attendance_poll(poll)):
                for attended_std in poll.studentsAttended:
                    bys_std = self.sm.find_student(
                        self.zoom_poll, attended_std)
                    if (bys_std is not None):
                        bys_std.attendance += 1

    def set_attendance_percentage(self):
        for std in self.zoom_poll.students:
            std.attendance_percentage = 100.0 * \
                float(std.attendance) / self.zoom_poll.attendance_polls

    def calculate_score(self):
        ans_matcher = AnswerMatcher()
        for poll in self.zoom_poll.polls:

            if(not self.is_attendance_poll(poll)):
                for std_answer in poll.studentAnswers:
                    if (ans_matcher.match(self.zoom_poll, std_answer.question, std_answer.answers)):
                        bys_std = self.sm.find_student(
                            self.zoom_poll, std_answer.student)
                        if (bys_std is not None):
                            bys_std.score += 1
                            std_answer.student.score += 1
                            std_answer.question.successRate += 1
                            poll.totalScore += 1

    def calculate_total_grade(self):
        for std in self.zoom_poll.students:
            std.total_grade = 100.0 * \
                float(std.score) / self.zoom_poll.quiz_polls

    def setStudentAnswerTrue(self):
        for poll in self.zoom_poll.polls:
            for answer in poll.studentAnswers:
                for question in poll.questions:
                    if(answer.question == question):
                        for true_answer in question.trueAnswers:
                            for std_answer in answer.answers:
                                if(std_answer.encode(encoding='UTF-8',errors='strict') 
                                == true_answer.encode(encoding='UTF-8',errors='strict')):
                                    answer.isTrue = True
                        
