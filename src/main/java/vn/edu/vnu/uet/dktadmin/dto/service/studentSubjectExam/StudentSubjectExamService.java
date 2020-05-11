package vn.edu.vnu.uet.dktadmin.dto.service.studentSubjectExam;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.vnu.uet.dktadmin.common.Constant;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.dto.dao.exam.ExamDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.studentSubjectExam.StudentSubjectExamDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Exam;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubject;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubjectExam;
import vn.edu.vnu.uet.dktadmin.dto.repository.StudentSubjectExamRepository;
import vn.edu.vnu.uet.dktadmin.dto.service.exam.ExamService;
import vn.edu.vnu.uet.dktadmin.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.studentSubject.ListStudentSubjectResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemesterExam.ListStudentSubjectExamResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemesterExam.StudentSubjectExamRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemesterExam.StudentSubjectExamResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentSubjectExamService {

    private final MapperFacade mapperFacade;
    private final StudentSubjectExamDao studentSubjectExamDao;
    private final StudentSubjectDao studentSubjectDao;
    private final StudentSubjectService studentSubjectService;
    private final ExamService examService;
    private final ExamDao examDao;

    public StudentSubjectExamService(MapperFacade mapperFacade, StudentSubjectExamDao studentSubjectExamDao, StudentSubjectDao studentSubjectDao, StudentSubjectService studentSubjectService, ExamService examService, ExamDao examDao) {
        this.mapperFacade = mapperFacade;
        this.studentSubjectExamDao = studentSubjectExamDao;
        this.studentSubjectDao = studentSubjectDao;
        this.studentSubjectService = studentSubjectService;
        this.examService = examService;
        this.examDao = examDao;
    }

    @Transactional
    public StudentSubjectExamResponse create(StudentSubjectExamRequest request) {
        validateStudentSubjectExam(request);
        Exam exam = examDao.getById(request.getExamId());
        StudentSubjectExam studentSubjectExam = mapperFacade.map(request, StudentSubjectExam.class);
        studentSubjectExam.setStatus(Constant.active);
        studentSubjectExam.setSemesterId(exam.getSemesterId());

        StudentSubject studentSubject = studentSubjectDao.getById(request.getStudentSubjectId());
        studentSubjectExam.setStudentId(studentSubject.getStudentId());
        Integer numberStudent = exam.getNumberOfStudentSubscribe();
        if (numberStudent == null) {
            numberStudent = 1;
        } else {
            numberStudent += 1;
        }
        exam.setNumberOfStudentSubscribe(numberStudent);
        examDao.store(exam);

        studentSubject.setIsRegistered(true);
        studentSubjectDao.store(studentSubject);

        StudentSubjectExamResponse response = mapperFacade.map(studentSubjectExamDao.store(studentSubjectExam), StudentSubjectExamResponse.class);
        response.setLocationId(exam.getLocationId());
        return response;
    }

    @Transactional
    public void delete(Long id) {
        StudentSubjectExam studentSubjectExam = studentSubjectExamDao.getById(id);
        Exam exam = examDao.getById(studentSubjectExam.getExamId());
        int numberStudent = exam.getNumberOfStudentSubscribe() == null ? 0 : exam.getNumberOfStudentSubscribe();
        if (numberStudent == 0) {
            exam.setNumberOfStudentSubscribe(0);
        } else {
            exam.setNumberOfStudentSubscribe(numberStudent - 1);
        }
        examDao.store(exam);

        StudentSubject studentSubject = studentSubjectDao.getById(studentSubjectExam.getStudentSubjectId());
        studentSubject.setIsRegistered(false);
        studentSubjectDao.store(studentSubject);

        studentSubjectExamDao.delete(studentSubjectExam);
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
        Exam exam = examDao.getById(request.getExamId());
        StudentSubject studentSubject = studentSubjectDao.getById(request.getStudentSubjectId());

        if (exam.getSubjectSemesterId() != studentSubject.getSubjectSemesterId()) {
            throw new BadRequestException(400, "StudentSubject và Exam không hợp lệ!");
        }
        if (exam.getNumberOfStudentSubscribe() != null && exam.getNumberOfStudentSubscribe() >= exam.getNumberOfStudent()) {
            throw new BadRequestException(400, "Số lượng sinh viên trong phòng đã đầy");
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

    public ListStudentSubjectExamResponse getBySemester(Long semesterId, PageBase pageBase) {
        List<StudentSubjectExam> studentSubjectExams = studentSubjectExamDao.getBySemesterId(semesterId);
        return getStudentExamPaging(studentSubjectExams, pageBase);
    }

    public ListStudentSubjectExamResponse getBySubjectSemester(Long subjectSemesterId, PageBase pageBase) {
        List<StudentSubjectExam> studentSubjectExams = studentSubjectExamDao.getByStudentSubjectId(subjectSemesterId);
        return getStudentExamPaging(studentSubjectExams, pageBase);
    }

    public StudentSubjectExamResponse getById(Long id) {
        return mapperFacade.map(studentSubjectExamDao.getById(id), StudentSubjectExamResponse.class);
    }

    public ListStudentSubjectResponse autoRegister(Long id) {
        return null;
    }

    private ListStudentSubjectExamResponse getStudentExamPaging(List<StudentSubjectExam> studentSubjectExams, PageBase pageBase) {
        List<StudentSubjectExam> studentSubjectExamList = new ArrayList<>();
        Integer page = pageBase.getPage();
        Integer size = pageBase.getSize();
        int total = studentSubjectExams.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            studentSubjectExamList.add(studentSubjectExams.get(i));
        }
        PageResponse pageResponse = new PageResponse(page, size, total);
        ListStudentSubjectExamResponse studentSubjectExamResponse = new ListStudentSubjectExamResponse(
                mapperFacade.mapAsList(studentSubjectExamList, StudentSubjectExamResponse.class),
                pageResponse
        );
        return studentSubjectExamResponse;
    }
}
