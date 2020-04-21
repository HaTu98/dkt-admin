package vn.edu.vnu.uet.dktadmin.dto.service.studentSubjectExam;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.vnu.uet.dktadmin.common.Constant;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.dto.dao.exam.ExamDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.studentSubjectExam.StudentSubjectExamDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Exam;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubjectExam;
import vn.edu.vnu.uet.dktadmin.dto.service.exam.ExamService;
import vn.edu.vnu.uet.dktadmin.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemesterExam.StudentSubjectExamRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemesterExam.StudentSubjectExamResponse;

@Service
public class StudentSubjectExamService {

    private final MapperFacade mapperFacade;
    private final StudentSubjectExamDao studentSubjectExamDao;
    private final StudentSubjectService studentSubjectService;
    private final ExamService examService;
    private final ExamDao examDao;

    public StudentSubjectExamService(MapperFacade mapperFacade, StudentSubjectExamDao studentSubjectExamDao, StudentSubjectService studentSubjectService, ExamService examService, ExamDao examDao) {
        this.mapperFacade = mapperFacade;
        this.studentSubjectExamDao = studentSubjectExamDao;
        this.studentSubjectService = studentSubjectService;
        this.examService = examService;
        this.examDao = examDao;
    }

    @Transactional
    public StudentSubjectExamResponse create(StudentSubjectExamRequest request) {
        validateStudentSubjectExam(request);
        StudentSubjectExam studentSubjectExam = mapperFacade.map(request, StudentSubjectExam.class);
        studentSubjectExam.setStatus(Constant.active);

        Exam exam = examDao.getById(request.getExamId());
        Integer numberStudent = exam.getNumberOfStudentSubscribe();
        if (numberStudent == null) {
            numberStudent = 1;
        } else {
            numberStudent += 1;
        }
        exam.setNumberOfStudentSubscribe(numberStudent);
        examDao.store(exam);
        return mapperFacade.map(studentSubjectExamDao.store(studentSubjectExam), StudentSubjectExamResponse.class);
    }

    public void validateStudentSubjectExam(StudentSubjectExamRequest request) {
        if (request.getExamId() == null) {
            throw new BadRequestException(400, "Exam không thể null");
        }
        if (request.getStudentSubjectId() == null) {
            throw new BadRequestException(400, "StudentSubject không thể null");
        }
        if (!studentSubjectService.existStudentSubject(request.getStudentSubjectId())) {
            throw new BadRequestException(400, "StudentSubject không tồn tại");
        }
        if (!examService.isExistExam(request.getExamId())) {
            throw new BadRequestException(400, "Exam không tồn tại");
        }
        if (isExistStudentSubjectExam(request.getExamId(), request.getStudentSubjectId())) {
            throw new BadRequestException(400, "StudentSubjectExam đã tồn tại");
        }
    }

    public boolean isExistStudentSubjectExam(Long examId, Long studentSubjectId) {
        StudentSubjectExam studentSubjectExam = studentSubjectExamDao.getByExamIdAndStudentSubjectId(examId, studentSubjectId);
        return studentSubjectExam != null;
    }

    public boolean isExistStudentSubjectExam(Long id) {
        StudentSubjectExam studentSubjectExam = studentSubjectExamDao.getById(id);
        return studentSubjectExam != null;
    }
}
