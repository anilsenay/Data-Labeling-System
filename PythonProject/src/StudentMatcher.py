class StudentMatcher(object):

    def __init__(self):
        super().__init__()

    def match(self, zoom_poll, attended_student):
        return self.find_student(zoom_poll, attended_student)

    def find_student(self, zoom_poll, attended_student):
        for bys_student in zoom_poll.students:
            poll_student_arr = (attended_student.name).split()
            bys_student_arr = (bys_student.name).split()
            poll_student_surname_arr = (attended_student.surname).split()
            bys_student_surname_arr = (bys_student.surname).split()
            for bys_mail in bys_student.emails:
                stremail = str(attended_student.emails[0])
                stremail2 = str(bys_mail)

                if(attended_student.emails[0] == bys_mail):

                    return bys_student

            for i in poll_student_arr:
                for j in bys_student_arr:
                    for k in poll_student_surname_arr:
                        for l in bys_student_surname_arr:
                            if ((l in k) or (k in l)):
                                if ((i in j) or (j in i)):
                                    for email in attended_student.emails:
                                        bys_student.emails.append(email)

                                    return bys_student

        return None
