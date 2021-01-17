from QuestionChoice import QuestionChoice

class Question(object):
    def __init__(self, question_content):
        self.question_content = question_content
        self.trueAnswers = [] # content of the correct answers 
        self.choices = []
        self.successRate = 0
    
    def insertChoice(self, choice: QuestionChoice):
        choiceFound: QuestionChoice = None
        for i in range(len(self.choices)):
            if(self.choices[i].choice_content == choice.choice_content):
                choiceFound = self.choices[i]
        if(choiceFound is None):
            self.choices.append(choice)
        else:
            choiceFound.increase()
