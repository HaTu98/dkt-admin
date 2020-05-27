package vn.edu.vnu.uet.dktadmin.dto.service.studentSubject;

import ma.glasnost.orika.MapperFacade;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnu.uet.dktadmin.common.Constant;
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

    public List<XSSFRow> importStudentSubject(MultipartFile file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<XSSFRow> errors = new ArrayList<>();
        storeImportStudentSubject(sheet, errors);
        return errors;
    }

    public XSSFWorkbook template() throws IOException {
        String templatePath = "\\template\\excel\\import_student_subject.xlsx";
        File templateFile = new ClassPathResource(templatePath).getFile();
        FileInputStream templateInputStream = new FileInputStream(templateFile);
        return new XSSFWorkbook(templateInputStream);
    }

    private void storeImportStudentSubject(XSSFSheet sheet, List<XSSFRow> errors) {
        int rowNumber = sheet.getPhysicalNumberOfRows();
        XSSFRow subjectRow = sheet.getRow(3);
        String subjectCode = ExcelUtil.getValueInCell(subjectRow.getCell(1));
        Subject subject = subjectDao.getBySubjectCode(subjectCode);

        XSSFRow semesterRow = sheet.getRow(5);
        String semesterName = ExcelUtil.getValueInCell(semesterRow.getCell(1));
        Semester semester = semesterDao.getBySemesterName(semesterName);

        SubjectSemester subjectSemester = subjectSemesterDao.getBySubjectIdAndSemesterId(subject.getId(), semester.getId());
        for (int i = 8; i < rowNumber; i++) {
            XSSFRow row = sheet.getRow(i);
            try {
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
