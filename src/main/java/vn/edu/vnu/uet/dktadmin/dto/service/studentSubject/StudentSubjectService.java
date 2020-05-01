package vn.edu.vnu.uet.dktadmin.dto.service.studentSubject;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.common.Constant;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.model.DktAdmin;
import vn.edu.vnu.uet.dktadmin.common.security.AccountService;
import vn.edu.vnu.uet.dktadmin.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.subjectSemester.SubjectSemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.*;
import vn.edu.vnu.uet.dktadmin.dto.service.student.StudentService;
import vn.edu.vnu.uet.dktadmin.dto.service.subjectSemester.SubjectSemesterService;
import vn.edu.vnu.uet.dktadmin.rest.model.studentSubject.StudentSubjectRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.studentSubject.StudentSubjectResponse;

import java.time.Instant;
import java.util.List;

@Service
public class StudentSubjectService {
    private final StudentSubjectDao studentSubjectDao;
    private final StudentDao studentDao;
    private final SubjectSemesterDao subjectSemesterDao;
    private final MapperFacade mapperFacade;
    private final StudentService studentService;
    private final SubjectSemesterService subjectSemesterService;
    private final AccountService accountService;

    public StudentSubjectService(MapperFacade mapperFacade, StudentSubjectDao studentSubjectDao, StudentDao studentDao, SubjectSemesterDao subjectSemesterDao, StudentService studentService, SubjectSemesterService subjectSemesterService, AccountService accountService) {
        this.mapperFacade = mapperFacade;
        this.studentSubjectDao = studentSubjectDao;
        this.studentDao = studentDao;
        this.subjectSemesterDao = subjectSemesterDao;
        this.studentService = studentService;
        this.subjectSemesterService = subjectSemesterService;
        this.accountService = accountService;
    }

    public StudentSubject create(StudentSubject request) {
        return studentSubjectDao.store(request);
    }

    public List<StudentSubject> getAll() {
        return studentSubjectDao.getAll();
    }

    public StudentSubjectResponse create(StudentSubjectRequest request) {
        validateStudentSubject(request);
        StudentSubject studentSubject = studentSubjectDao.store(generateStudentSubject(request));
        return mapperFacade.map(studentSubject, StudentSubjectResponse.class);
    }

    public boolean existStudentSubject(Long studentId, Long subjectSemesterId) {
        StudentSubject studentSubject = studentSubjectDao.getByStudentAndSubjectSemesterId(studentId, subjectSemesterId);
        return studentSubject != null;
    }
    public boolean existStudentSubject(Long id) {
        StudentSubject studentSubject = studentSubjectDao.getById(id);
        return studentSubject != null;
    }
    private StudentSubject generateStudentSubject(StudentSubjectRequest request) {
        SubjectSemester subjectSemester = subjectSemesterDao.getById(request.getSubjectSemesterId());
        StudentSubject studentSubject = mapperFacade.map(request, StudentSubject.class);
        studentSubject.setStatus(Constant.active);
        studentSubject.setCreatedAt(Instant.now());
        studentSubject.setModifiedAt(Instant.now());

        DktAdmin admin = accountService.getUserSession();
        studentSubject.setCreatedBy(admin.getUsername());
        studentSubject.setModifiedBy(admin.getUsername());
        studentSubject.setSemesterId(subjectSemester.getSemesterId());

        return studentSubject;
    }

    private void validateStudentSubject(StudentSubjectRequest request) {
        if (request.getStudentId() == null){
            throw new BadRequestException(400, "StudentId không thể null");
        }
        if (request.getSubjectSemesterId() == null){
            throw new BadRequestException(400, "StudentSubjectId không thể null");
        }
        if (!studentService.existStudent(request.getStudentId())){
            throw new BadRequestException(400, "Sinh viên không tồn tại");
        }
        if (!subjectSemesterService.existSubjectSemester(request.getSubjectSemesterId())) {
            throw  new BadRequestException(400, "Môn học trong học kì không tồn tại");
        }
        if (existStudentSubject(request.getStudentId(), request.getSubjectSemesterId())){
            throw new BadRequestException(400, "Sinh viên môn học đã tồn tại");
        }
    }
}
