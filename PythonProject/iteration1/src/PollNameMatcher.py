
class PollNameMatcher(object):

    def __init__(self):
        pass

    def match(self, poll_list, answer_key):
        for poll in poll_list:
            if(poll.isAttendance is True): continue
            check = False
            if (len(poll.questions) == len(answer_key.question_answer)):
                for question in poll.questions:
                    for question_in_key in answer_key.question_answer.keys():
                        if (question.question_content == question_in_key):
                            poll.answer_key = answer_key
                            check = True
