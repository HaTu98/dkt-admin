package vn.edu.vnu.uet.dktadmin.dto.service.studentSubject;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.subject.SubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Semester;
import vn.edu.vnu.uet.dktadmin.dto.model.Student;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubject;
import vn.edu.vnu.uet.dktadmin.dto.model.Subject;
import vn.edu.vnu.uet.dktadmin.rest.model.studentSubject.StudentSubjectRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.studentSubject.StudentSubjectResponse;

import java.util.List;

@Service
public class StudentSubjectService {
    @Autowired
    private StudentSubjectDao studentSubjectDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private SubjectDao subjectDao;
    @Autowired
    private SemesterDao semesterDao;

    @Autowired
    private MapperFacade mapperFacade;

    public StudentSubject create(StudentSubject request) {
        return studentSubjectDao.store(request);
    }

    public List<StudentSubject> getAll() {
        return studentSubjectDao.getAll();
    }

    public StudentSubjectResponse create(StudentSubjectRequest request) {
        String studentCode = request.getStudentCode();
        Student student = studentDao.getByStudentCode(studentCode);
        if (student == null) {
            throw new BadRequestException(400, "student not existed");
        }

        String subjectCode = request.getSubjectCode();
        Subject subject = subjectDao.getBySubjectCode(subjectCode);
        if (subject == null) {
            throw new BadRequestException(400, "subject not exist");
        }

        String semesterCode = request.getSemesterCode();
        Semester semester = semesterDao.getBySemesterCode(semesterCode);
        if (semester == null) {
            throw new BadRequestException(400, "semester not exist");
        }

        if (existStudentSubject(student.getId(), subject.getId(), semester.getId())){
            throw new BadRequestException(400, "StudentSubject already existed");
        }
        StudentSubject studentSubject = buildStudentSubject(student, subject,semester);

        StudentSubject save = studentSubjectDao.store(studentSubject);
        return mapperFacade.map(save, StudentSubjectResponse.class);
    }

    public boolean existStudentSubject(Long studentId, Long subjectId, Long semesterId) {
        StudentSubject studentSubject = studentSubjectDao.getByStudentAndSubjectAndSemester(studentId, subjectId, semesterId);
        return studentSubject != null;
    }

    private StudentSubject buildStudentSubject(Student student, Subject subject, Semester semester) {
        StudentSubject studentSubject = new StudentSubject();
        studentSubject.setStudentId(student.getId());
        studentSubject.setSubjectId(subject.getId());
        studentSubject.setSemesterId(subject.getId());
        studentSubject.setStatus("ACTIVE");

        return studentSubject;
    }
}
