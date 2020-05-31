package vn.edu.vnu.uet.dktadmin.dto.service.studentSubjectExam;

import ma.glasnost.orika.MapperFacade;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.vnu.uet.dktadmin.common.Constant;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.utilities.ExcelUtil;
import vn.edu.vnu.uet.dktadmin.common.utilities.Util;
import vn.edu.vnu.uet.dktadmin.dto.dao.exam.ExamDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.studentSubjectExam.StudentSubjectExamDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Exam;
import vn.edu.vnu.uet.dktadmin.dto.model.Student;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubject;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubjectExam;
import vn.edu.vnu.uet.dktadmin.dto.service.exam.ExamService;
import vn.edu.vnu.uet.dktadmin.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemesterExam.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentSubjectExamService {

    private final MapperFacade mapperFacade;
    private final StudentSubjectExamDao studentSubjectExamDao;
    private final StudentSubjectDao studentSubjectDao;
    private final StudentSubjectService studentSubjectService;
    private final ExamService examService;
    private final StudentDao studentDao;
    private final ExamDao examDao;

    public StudentSubjectExamService(MapperFacade mapperFacade, StudentSubjectExamDao studentSubjectExamDao, StudentSubjectDao studentSubjectDao, StudentSubjectService studentSubjectService, ExamService examService, StudentDao studentDao, ExamDao examDao) {
        this.mapperFacade = mapperFacade;
        this.studentSubjectExamDao = studentSubjectExamDao;
        this.studentSubjectDao = studentSubjectDao;
        this.studentSubjectService = studentSubjectService;
        this.examService = examService;
        this.studentDao = studentDao;
        this.examDao = examDao;
    }

    @Transactional
    public StudentSubjectExamResponse create(StudentSubjectExamRequest request) {
        validateStudentSubjectExam(request);
        Exam exam = examDao.getById(request.getExamId());
        StudentSubjectExam studentSubjectExam = mapperFacade.map(request, StudentSubjectExam.class);
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

    public Workbook export(Long subjectSemesterId) throws IOException {
        List<StudentSubjectExam> studentSubjectExams = studentSubjectExamDao.getByStudentSubjectId(subjectSemesterId);
        List<StudentSubjectExamResponse> studentSubjectExamResponses = generateResponse(studentSubjectExams);
        String templatePath = "\\template\\excel\\export_student_subject.xlsx";
        File templateFile = new ClassPathResource(templatePath).getFile();
        FileInputStream templateInputStream = new FileInputStream(templateFile);
        Workbook workbook  = new XSSFWorkbook(templateInputStream);
        writeExcel(workbook, studentSubjectExamResponses);
        return workbook;
    }

    public StudentSubjectExamResponse getById(Long id) {
        return mapperFacade.map(studentSubjectExamDao.getById(id), StudentSubjectExamResponse.class);
    }

    public AutoRegisterResponse autoRegister(Long semesterId) {
        int success = 0, fail = 0;
        List<StudentSubject> studentSubjects = studentSubjectDao.getByIsNotRegistered(semesterId);
        for (StudentSubject studentSubject : studentSubjects) {
            try {
                Exam exam = autoGetRegisterSubject(studentSubject);
                StudentSubjectExam studentSubjectExam = new StudentSubjectExam();
                studentSubjectExam.setSemesterId(exam.getSemesterId());
                studentSubjectExam.setExamId(exam.getId());
                studentSubjectExam.setStudentSubjectId(studentSubject.getId());
                studentSubjectExam.setStudentId(studentSubject.getStudentId());
                storeStudentSubjectExam(studentSubject, exam, studentSubjectExam);
                success += 1;
            } catch (Exception e) {
                fail += 1;
            }
        }
        return new AutoRegisterResponse(success, fail);
    }

    @Transactional
    public StudentSubjectExam storeStudentSubjectExam(StudentSubject studentSubject, Exam exam, StudentSubjectExam studentSubjectExam) {
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

        return studentSubjectExamDao.store(studentSubjectExam);
    }

    private void writeExcel(Workbook workbook, List<StudentSubjectExamResponse> studentSubjectExamResponses) {
        CellStyle cellStyle = ExcelUtil.createDefaultCellStyle(workbook);
        CellStyle cellStyleLeft = ExcelUtil.createLeftCellStyle(workbook);
        Sheet sheet = workbook.getSheetAt(0);
        /*for (StudentSubjectExamResponse response : studentSubjectExamResponses) {
            Row row = sheet.createRow(i + 8);

            Cell cellStt = row.createCell(0);
            cellStt.setCellValue(i + 1);
            cellStt.setCellStyle(cellStyle);
        }*/
    }

    private Exam autoGetRegisterSubject(StudentSubject studentSubject) {
        List<StudentSubjectExam> studentSubjectExams = studentSubjectExamDao
                .getByStudentIdAndSemesterId(studentSubject.getStudentId(), studentSubject.getSemesterId());
        List<Long> listExamId = studentSubjectExams.stream()
                .map(StudentSubjectExam::getExamId)
                .collect(Collectors.toList());
        List<Exam> examRegistered = examDao.getExamInListId(listExamId);
        List<Exam> exams = examDao.getBySemesterIdAndSubjectId(studentSubject.getSemesterId(), studentSubject.getSubjectId());
        List<Exam> slotExams = exams.stream()
                .filter(exam -> exam.getNumberOfStudentSubscribe() < exam.getNumberOfStudent())
                .collect(Collectors.toList());
        slotExams = slotExams.stream()
                .sorted(Comparator.comparingInt(Exam::getNumberOfStudentSubscribe))
                .collect(Collectors.toList());
        return getSlotExam(slotExams, examRegistered);
    }

    private Exam getSlotExam(List<Exam> slotExams, List<Exam> examRegistered) {
        for (Exam exam : slotExams) {
            if (checkSlotValid(exam,examRegistered))
                return exam;
        }
        return null;
    }

    private boolean checkSlotValid(Exam slotExam, List<Exam> examRegistered) {
        LocalDateTime start = slotExam.getStartTime();
        LocalDateTime end = slotExam.getEndTime();
        for (Exam exam : examRegistered) {
            LocalDateTime startTime = exam.getStartTime();
            LocalDateTime endTime = exam.getEndTime();
            if (!Util.validateTime(start, end, startTime, endTime)) return false;
        }
        return true;
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
        List<StudentSubjectExamResponse> responses = generateResponse(studentSubjectExamList);
        return new ListStudentSubjectExamResponse(responses, pageResponse);
    }

    private List<StudentSubjectExamResponse> generateResponse(List<StudentSubjectExam> studentSubjectExams) {
        List<StudentSubjectExamResponse> responses = new ArrayList<>();
        List<Long> studentIds = studentSubjectExams.stream().map(StudentSubjectExam::getStudentId).collect(Collectors.toList());
        List<Student> students = studentDao.getStudentInList(studentIds);
        Map<Long, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getId, x -> x));

        List<Long> studentSubjectIds = studentSubjectExams.stream().map(StudentSubjectExam::getStudentSubjectId).collect(Collectors.toList());
        List<StudentSubject> studentSubjects = studentSubjectDao.getStudentSubjectInList(studentSubjectIds);
        Map<Long, StudentSubject> studentSubjectMap = studentSubjects.stream().collect(Collectors.toMap(StudentSubject::getId, x -> x));
        for (StudentSubjectExam studentExam : studentSubjectExams) {
            StudentSubjectExamResponse response = new StudentSubjectExamResponse();
            Student student = studentMap.get(studentExam.getStudentId());
            StudentSubject studentSubject = studentSubjectMap.get(studentExam.getStudentSubjectId());
            if (student == null || studentSubject == null) continue;
            response.setId(studentExam.getId());
            response.setExamId(studentExam.getExamId());
            response.setStudentSubjectId(studentExam.getStudentSubjectId());
            response.setStatus(studentSubject.getStatus());
            response.setCourse(student.getCourse());
            response.setDateOfBirth(student.getDateOfBirth());
            response.setFullName(student.getFullName());
            response.setGender(student.getGender());
            response.setStudentCode(student.getStudentCode());
            responses.add(response);
        }
        return responses;
    }
}
