
class ZoomPoll(object):

    def __init__(self):
        self.polls = []
        self.students = []
        self.attendance_polls = 0
        self.quiz_polls = 0

    def count_attendance_and_quiz_polls(self):
        for poll in self.polls:
            for q in poll.questions:
                if(q.question_content == "Are you attending this lecture?"):
                    self.attendance_polls += 1
                else:
                    self.quiz_polls += 1
