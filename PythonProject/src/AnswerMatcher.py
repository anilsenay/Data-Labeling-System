class AnswerMatcher(object):

    def __init__(self):
        pass

    def match(self, zoom_poll, question, std_answers):
        if(self.find_answer(zoom_poll, question) == std_answers):
            return True
        else:
            return False

    def find_answer(self, zoom_poll, question):
        for p in zoom_poll.polls:
            for q in p.questions:
                if(q == question and p.answer_key is not None):  # not a question object
                    question.trueAnswers = (
                        p.answer_key.question_answer[question.question_content])
                    return p.answer_key.question_answer[question.question_content]
