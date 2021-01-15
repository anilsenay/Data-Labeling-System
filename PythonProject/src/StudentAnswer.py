import Student
class StudentAnswer(object):
  isTrue = False
  def __init__(self, s , question, answer): 
    self.student = s
    self.question = question
    self.answer = answer

  ##check the answer correctness
  if question.answer == answer:
    isTrue = True
  pass
