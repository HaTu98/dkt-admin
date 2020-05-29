package vn.edu.vnu.uet.dktadmin.dto.service.studentSubject;

import ma.glasnost.orika.MapperFacade;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnu.uet.dktadmin.common.Constant;
import vn.edu.vnu.uet.dktadmin.common.enumType.Gender;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.model.DktAdmin;
import vn.edu.vnu.uet.dktadmin.common.security.AccountService;
import vn.edu.vnu.uet.dktadmin.common.utilities.ExcelUtil;
import vn.edu.vnu.uet.dktadmin.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.studentSubjectExam.StudentSubjectExamDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.subject.SubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.subjectSemester.SubjectSemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.*;
import vn.edu.vnu.uet.dktadmin.dto.service.student.StudentService;
import vn.edu.vnu.uet.dktadmin.dto.service.subjectSemester.SubjectSemesterService;
import vn.edu.vnu.uet.dktadmin.rest.controller.exam.ExamController;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.studentSubject.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentSubjectService {
    private static final Logger log = LoggerFactory.getLogger(StudentSubjectService.class);
    private final StudentSubjectDao studentSubjectDao;
    private final StudentDao studentDao;
    private final SubjectSemesterDao subjectSemesterDao;
    private final MapperFacade mapperFacade;
    private final StudentService studentService;
    private final SubjectSemesterService subjectSemesterService;
    private final AccountService accountService;
    private final StudentSubjectExamDao studentSubjectExamDao;
    private final SemesterDao semesterDao;
    private final SubjectDao subjectDao;

    public StudentSubjectService(MapperFacade mapperFacade, StudentSubjectDao studentSubjectDao, StudentDao studentDao, SubjectSemesterDao subjectSemesterDao, StudentService studentService, SubjectSemesterService subjectSemesterService, AccountService accountService, StudentSubjectExamDao studentSubjectExamDao, SemesterDao semesterDao, SubjectDao subjectDao) {
        this.mapperFacade = mapperFacade;
        this.studentSubjectDao = studentSubjectDao;
        this.studentDao = studentDao;
        this.subjectSemesterDao = subjectSemesterDao;
        this.studentService = studentService;
        this.subjectSemesterService = subjectSemesterService;
        this.accountService = accountService;
        this.studentSubjectExamDao = studentSubjectExamDao;
        this.semesterDao = semesterDao;
        this.subjectDao = subjectDao;
    }

    public StudentSubject create(StudentSubject request) {
        return studentSubjectDao.store(request);
    }

    public StudentSubjectResponse createByStudentCode(StudentSubjectRequest request) {
        validateCreateOne(request);
        Student student = studentDao.getByStudentCode(request.getStudentCode());
        request.setStudentId(student.getId());
        StudentSubject studentSubject = studentSubjectDao.store(generateStudentSubject(request));
        return mapperFacade.map(studentSubject, StudentSubjectResponse.class);
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

    public StudentSubjectResponse update(StudentSubjectRequest request) {
        validateUpdateStudentSubject(request);
        StudentSubject studentSubject = studentSubjectDao.getById(request.getId());
        SubjectSemester subjectSemester = subjectSemesterDao.getById(request.getSubjectSemesterId());
        studentSubject.setSemesterId(subjectSemester.getSemesterId());
        studentSubject.setSubjectSemesterId(request.getSubjectSemesterId());
        studentSubject.setStudentId(request.getStudentId());
        studentSubject.setSubjectId(studentSubject.getSubjectId());

        studentSubject.setModifiedAt(Instant.now());

        DktAdmin admin = accountService.getUserSession();
        studentSubject.setModifiedBy(admin.getUsername());
        return mapperFacade.map(studentSubjectDao.store(studentSubject), StudentSubjectResponse.class);
    }

    public void updateStatus(StudentSubjectStatus request) {
        StudentSubject studentSubject = studentSubjectDao.getById(request.getId());
        if (studentSubject != null) {
            if (request.getStatus() == Constant.active) {
                studentSubject.setStatus(Constant.active);
            }
            if (request.getStatus() == Constant.inActive) {
                studentSubject.setStatus(Constant.inActive);
            }
            studentSubjectDao.store(studentSubject);
        }
    }

    public ListStudentSubjectResponse getBySubjectSemesterId(Long id, PageBase pageBase) {
        List<StudentSubject> studentSubjects = studentSubjectDao.getBySubjectSemesterId(id);
        return getStudentSubjectPaging(studentSubjects, pageBase);
    }

    public ListStudentSubjectResponse getBySemesterId(Long id, PageBase pageBase) {
        List<StudentSubject> studentSubjects = studentSubjectDao.getBySemesterId(id);
        return getStudentSubjectPaging(studentSubjects, pageBase);
    }

    public void delete(Long id) {
        StudentSubject studentSubject = studentSubjectDao.getById(id);
        if (studentSubject != null) {
            studentSubjectDao.delete(studentSubject);
        }
    }

    public void deleteList(List<Long> ids) {
        List<StudentSubject> studentSubjects = studentSubjectDao.getStudentSubjectInList(ids);
        studentSubjectDao.deleteAll(studentSubjects);
    }

    public StudentSubjectResponse getById(Long id) {
        return mapperFacade.map(studentSubjectDao.getById(id), StudentSubjectResponse.class);
    }

    public ListStudentSubjectResponse getStudentSubjectUnregistered(Long semesterId, PageBase pageBase) {
        List<StudentSubjectExam> studentSubjectExams = studentSubjectExamDao.getBySemesterId(semesterId);
        List<StudentSubject> studentSubjects = studentSubjectDao.getBySemesterId(semesterId);
        Map<Long, StudentSubject> studentSubjectMap = new HashMap<>();
        for (StudentSubject studentSubject : studentSubjects) {
            studentSubjectMap.put(studentSubject.getId(), studentSubject);
        }
        for (StudentSubjectExam studentSubjectExam : studentSubjectExams) {
            if (studentSubjectMap.containsKey(studentSubjectExam.getStudentSubjectId())) {
                studentSubjectMap.remove(studentSubjectExam.getId());
            }
        }
        return getStudentSubjectPaging(new ArrayList<>( studentSubjectMap.values()), pageBase);
    }

    public ListStudentInSubjectResponse getListStudentInSubject(Long subjectSemesterId, PageBase pageBase) {
        List<StudentSubject> studentSubjects = studentSubjectDao.getBySubjectSemesterId(subjectSemesterId);
        return generateStudentInSubject(studentSubjects, pageBase, subjectSemesterId);
    }

    public List<XSSFRow> importStudentSubject(MultipartFile file, Long subjectSemesterId) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<XSSFRow> errors = new ArrayList<>();
        storeImportStudentSubject(sheet, errors, subjectSemesterId);
        return errors;
    }

    public XSSFWorkbook template() throws IOException {
        String templatePath = "\\template\\excel\\import_student_subject.xlsx";
        File templateFile = new ClassPathResource(templatePath).getFile();
        FileInputStream templateInputStream = new FileInputStream(templateFile);
        return new XSSFWorkbook(templateInputStream);
    }

    public Workbook export(Long subjectSemesterId) throws IOException {
        List<StudentSubject> studentSubjects = studentSubjectDao.getBySubjectSemesterId(subjectSemesterId);
        String templatePath = "\\template\\excel\\export_student_subject.xlsx";
        File templateFile = new ClassPathResource(templatePath).getFile();
        FileInputStream templateInputStream = new FileInputStream(templateFile);
        Workbook workbook  = new XSSFWorkbook(templateInputStream);
        writeXSSFSheet(workbook, studentSubjects);
        return workbook;
    }

    private void writeXSSFSheet(Workbook workbook, List<StudentSubject> studentSubjects) {
        Subject subject = subjectDao.getById(studentSubjects.get(0).getSubjectId());

        CellStyle cellStyle = ExcelUtil.createDefaultCellStyle(workbook);
        CellStyle cellStyleLeft = ExcelUtil.createLeftCellStyle(workbook);
        Sheet sheet = workbook.getSheetAt(0);
        Row row2 = sheet.getRow(2);
        Cell subjectNameCell = row2.createCell(1);
        subjectNameCell.setCellValue(subject.getSubjectName());
        subjectNameCell.setCellStyle(cellStyleLeft);

        Row row3 = sheet.getRow(3);
        Cell subjectCodeCell = row3.createCell(1);
        subjectCodeCell.setCellValue(subject.getSubjectCode());
        subjectCodeCell.setCellStyle(cellStyleLeft);

        Row row4 = sheet.getRow(4);
        Cell numberOfCredit = row4.createCell(1);
        numberOfCredit.setCellValue(subject.getNumberOfCredit());
        numberOfCredit.setCellStyle(cellStyleLeft);

        Semester semester = semesterDao.getById(studentSubjects.get(0).getSemesterId());
        Row row5 = sheet.getRow(5);
        Cell semesterCellName = row5.createCell(1);
        semesterCellName.setCellValue(semester.getSemesterName());
        semesterCellName.setCellStyle(cellStyleLeft);

        for (int i = 0; i < studentSubjects.size(); i++) {
            try {
                StudentSubject studentSubject = studentSubjects.get(i);
                Row row = sheet.createRow(i + 8);

                Cell cellStt = row.createCell(0);
                cellStt.setCellValue(i + 1);
                cellStt.setCellStyle(cellStyle);

                Student student = studentDao.getById(studentSubject.getStudentId());
                Cell studentNameCell = row.createCell(1);
                ExcelUtil.setCellValueAndStyle(studentNameCell, student.getFullName(), cellStyleLeft);

                Cell studentCodeCell = row.createCell(2);
                ExcelUtil.setCellValueAndStyle(studentCodeCell, Double.parseDouble(student.getStudentCode()), cellStyle);

                Cell studentGenderCell = row.createCell(3);
                String gender = Gender.getByValue(student.getGender()).getName();
                studentGenderCell.setCellValue(gender);
                studentGenderCell.setCellStyle(cellStyle);

                Cell studentDoBCell = row.createCell(4);
                studentDoBCell.setCellValue(student.getDateOfBirth());
                studentDoBCell.setCellStyle(cellStyle);

                Cell studentEmailCell = row.createCell(5);
                studentEmailCell.setCellValue(student.getEmail());
                studentEmailCell.setCellStyle(cellStyle);

                Cell studentCourseCell = row.createCell(6);
                studentCourseCell.setCellValue(student.getCourse());
                studentCourseCell.setCellStyle(cellStyle);

                Cell statusCell = row.createCell(7);
                String status = studentSubject.getStatus() == 1 ? "" : "Không";
                statusCell.setCellValue(status);
                statusCell.setCellStyle(cellStyle);
            }catch (Exception e) {
                log.error("error export :", e);
            }
        }
    }

    private void storeImportStudentSubject(XSSFSheet sheet, List<XSSFRow> errors, Long subjectSemesterId) {
        int rowNumber = sheet.getPhysicalNumberOfRows();

        SubjectSemester subjectSemester = subjectSemesterDao.getById(subjectSemesterId);
        for (int i = 4; i < rowNumber; i++) {
            XSSFRow row = sheet.getRow(i);
            try {
                if (row == null) continue;
                String stt = ExcelUtil.getValueInCell(row.getCell(0));
                if (stt == null) {
                    continue;
                }
                String studentCode = ExcelUtil.getValueInCell(row.getCell(2));
                StudentSubjectRequest studentSubjectRequest = new StudentSubjectRequest();
                Student student = studentDao.getByStudentCode(studentCode);
                if (student == null) {
                    errors.add(row);
                    continue;
                }
                studentSubjectRequest.setStudentId(student.getId());
                studentSubjectRequest.setSubjectSemesterId(subjectSemester.getId());
                this.create(studentSubjectRequest);
            } catch (Exception e) {
                errors.add(row);
            }
        }
    }

    private ListStudentInSubjectResponse generateStudentInSubject(List<StudentSubject> studentSubjects, PageBase pageBase, Long subjectSemesterId) {
        List<Long> listStudentId = new ArrayList<>();
        Map<Long, StudentSubject> studentSubjectMap = new HashMap<>();
        Integer page = pageBase.getPage();
        Integer size = pageBase.getSize();
        int total = studentSubjects.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            listStudentId.add(studentSubjects.get(i).getStudentId());
            studentSubjectMap.put(studentSubjects.get(i).getStudentId(), studentSubjects.get(i));
        }
        List<Student> students = studentDao.getStudentInList(listStudentId);
        List<StudentInSubjectResponse> studentInSubjectResponses = new ArrayList<>();
        for (Student student : students) {
            StudentInSubjectResponse response = new StudentInSubjectResponse();
            response.setCourse(student.getCourse());
            response.setStudentCode(student.getStudentCode());
            response.setStudentDateOfBirth(student.getDateOfBirth());
            response.setStudentId(student.getId());
            response.setStudentName(student.getFullName());
            response.setStudentGender(student.getGender());
            response.setEmail(student.getEmail());
            response.setSubjectSemesterId(subjectSemesterId);
            if (studentSubjectMap.containsKey(student.getId())) {
                response.setStatus(studentSubjectMap.get(student.getId()).getStatus());
                response.setStudentSubjectId(studentSubjectMap.get(student.getId()).getId());
            }
            studentInSubjectResponses.add(response);
        }
        PageResponse pageResponse = new PageResponse(page, size, total);
        return new ListStudentInSubjectResponse(studentInSubjectResponses, pageResponse);
    }

    private ListStudentSubjectResponse getStudentSubjectPaging(List<StudentSubject> studentSubject, PageBase pageBase) {
        List<StudentSubject> studentSubjectList = new ArrayList<>();
        Integer page = pageBase.getPage();
        Integer size = pageBase.getSize();
        int total = studentSubject.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            studentSubjectList.add(studentSubject.get(i));
        }
        PageResponse pageResponse = new PageResponse(page, size, total);
        return new ListStudentSubjectResponse(
                mapperFacade.mapAsList(studentSubjectList, StudentSubjectResponse.class),
                pageResponse
        );
    }

    private StudentSubject generateStudentSubject(StudentSubjectRequest request) {
        SubjectSemester subjectSemester = subjectSemesterDao.getById(request.getSubjectSemesterId());
        StudentSubject studentSubject = mapperFacade.map(request, StudentSubject.class);
        studentSubject.setSubjectId(subjectSemester.getSubjectId());
        studentSubject.setIsRegistered(false);
        studentSubject.setStatus(Constant.active);
        studentSubject.setCreatedAt(Instant.now());
        studentSubject.setModifiedAt(Instant.now());

        DktAdmin admin = accountService.getUserSession();
        studentSubject.setCreatedBy(admin.getUsername());
        studentSubject.setModifiedBy(admin.getUsername());
        studentSubject.setSemesterId(subjectSemester.getSemesterId());

        return studentSubject;
    }

    public void validateCreateOne(StudentSubjectRequest request) {
        if (request.getStudentCode() == null) {
            throw new BaseException(404, "StudentCode không tồn tại");
        }
        Student student = studentDao.getByStudentCode(request.getStudentCode());
        if (student == null) {
            throw new BaseException(404, "StudentCode không tồn tại");
        }
        StudentSubject studentSubject = studentSubjectDao.getByStudentAndSubjectSemesterId(student.getId(), request.getSubjectSemesterId());
        if (studentSubject != null) {
            throw new BaseException(409, "StudentSubject đã tồn tại");
        }
        if (!subjectSemesterService.existSubjectSemester(request.getSubjectSemesterId())) {
            throw new BadRequestException(404, "Môn học trong học kì không tồn tại");
        }
    }

    private void validateStudentSubject(StudentSubjectRequest request) {
        if (request.getStudentId() == null) {
            throw new BadRequestException(400, "StudentId không thể null");
        }
        if (request.getSubjectSemesterId() == null) {
            throw new BadRequestException(400, "StudentSubjectId không thể null");
        }
        if (!studentService.existStudent(request.getStudentId())) {
            throw new BadRequestException(400, "Sinh viên không tồn tại");
        }
        if (!subjectSemesterService.existSubjectSemester(request.getSubjectSemesterId())) {
            throw new BadRequestException(400, "Môn học trong học kì không tồn tại");
        }
        if (existStudentSubject(request.getStudentId(), request.getSubjectSemesterId())) {
            throw new BadRequestException(400, "Sinh viên môn học đã tồn tại");
        }
    }

    private void validateUpdateStudentSubject(StudentSubjectRequest request) {
        if (!existStudentSubject(request.getId())) {
            throw new BadRequestException(400, "Sinh viên môn học không tồn tại");
        }

        if (request.getStudentId() == null) {
            throw new BadRequestException(400, "StudentId không thể null");
        }
        if (request.getSubjectSemesterId() == null) {
            throw new BadRequestException(400, "StudentSubjectId không thể null");
        }
        if (!studentService.existStudent(request.getStudentId())) {
            throw new BadRequestException(400, "Sinh viên không tồn tại");
        }
        if (!subjectSemesterService.existSubjectSemester(request.getSubjectSemesterId())) {
            throw new BadRequestException(400, "Môn học trong học kì không tồn tại");
        }
        if (!existStudentSubject(request.getStudentId(), request.getSubjectSemesterId())) {
            throw new BadRequestException(400, "Sinh viên môn học không tồn tại");
        }
    }
}
