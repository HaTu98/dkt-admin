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
import vn.edu.vnu.uet.dktadmin.dto.dao.subjectSemester.SubjectSemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.*;
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
    private SubjectSemesterDao subjectSemesterDao;

    @Autowired
    private MapperFacade mapperFacade;

    public StudentSubject create(StudentSubject request) {
        return studentSubjectDao.store(request);
    }

    public List<StudentSubject> getAll() {
        return studentSubjectDao.getAll();
    }

    public StudentSubjectResponse create(StudentSubjectRequest request) {
        Long studentId = request.getStudentId();
        Student student = studentDao.getById(studentId);
        if (student == null) {
            throw new BadRequestException(400, "student not existed");
        }

        Long subjectId = request.getSubjectSubjectId();
        SubjectSemester subjectSemester = subjectSemesterDao.getById(subjectId);
        if (subjectSemester == null) {
            throw new BadRequestException(400, "subject semester not exist");
        }

        if (existStudentSubject(student.getId(), subjectSemester.getSemesterId())){
            throw new BadRequestException(400, "StudentSubject already existed");
        }
        StudentSubject studentSubject = buildStudentSubject(student, subjectSemester);

        StudentSubject save = studentSubjectDao.store(studentSubject);
        return mapperFacade.map(save, StudentSubjectResponse.class);
    }

    public boolean existStudentSubject(Long studentId, Long subjectSemesterId) {
        StudentSubject studentSubject = studentSubjectDao.getByStudentAndSubjectSemesterId(studentId, subjectSemesterId);
        return studentSubject != null;
    }

    private StudentSubject buildStudentSubject(Student student, SubjectSemester subjectSemester) {
        StudentSubject studentSubject = new StudentSubject();
        studentSubject.setStudentId(student.getId());
        studentSubject.setStudentSubjectId(subjectSemester.getId());
        studentSubject.setStatus("ACTIVE");

        return studentSubject;
    }
}
