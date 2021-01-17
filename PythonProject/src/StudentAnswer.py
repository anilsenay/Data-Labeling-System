from Student import Student
class StudentAnswer(object):
  
  def __init__(self, s , question): 
    self.student = s
    self.question = question
    self.answers = []
    self.isTrue = False

  def insertAnswer(self, answer):
    self.answers.append(answer)
    