class Student(object):
    def __init__(self, id, name, surname, email, remark):
        self.id =id
        self.name = name
        self.surname = surname
        self.emails = []
        if (len(self.emails) == 0 and email is not None):
            self.emails.append(email)
        self.remark = remark
        self.score = 0
        self.attendance = 0
        self.ownAnswers =[]
        self.attendance_percentage = 0
        self.total_grade = 0
    

