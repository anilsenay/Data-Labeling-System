class QuestionChoice(object):
    
    def __init__(self, choice_content):
        self.choice_content = choice_content
        self.num_of_selection = 1

    def increase(self):
        self.num_of_selection = self.num_of_selection + 1