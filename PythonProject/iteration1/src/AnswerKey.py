class AnswerKey(object):
    def __init__(self, pollName):
        super().__init__()
        self.pollName = pollName
        self.question_answer = dict()
    pass

    def add_question_answer(self, question, answer):
        self.question_answer[question] = answer
