package vn.edu.vnu.uet.dktadmin.dto.service.student;

import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnu.uet.dktadmin.common.Constant;
import vn.edu.vnu.uet.dktadmin.common.enumType.Gender;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.model.DktAdmin;
import vn.edu.vnu.uet.dktadmin.common.security.AccountService;
import vn.edu.vnu.uet.dktadmin.common.utilities.ExcelUtil;
import vn.edu.vnu.uet.dktadmin.common.validator.EmailValidator;
import vn.edu.vnu.uet.dktadmin.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Student;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubject;
import vn.edu.vnu.uet.dktadmin.rest.model.CheckExistRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final EmailValidator emailValidator;
    private final StudentDao studentDao;
    private final StudentSubjectDao studentSubjectDao;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final MapperFacade mapperFacade;

    public StudentService(EmailValidator emailValidator, StudentDao studentDao, StudentSubjectDao studentSubjectDao, AccountService accountService, PasswordEncoder passwordEncoder, MapperFacade mapperFacade) {
        this.emailValidator = emailValidator;
        this.studentDao = studentDao;
        this.studentSubjectDao = studentSubjectDao;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.mapperFacade = mapperFacade;
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        validateCreate(request);
        DktAdmin admin = accountService.getUserSession();

        String passwordEncode = passwordEncoder.encode(request.getStudentCode());

        Student student = buildCreateStudent(request, admin);
        student.setPassword(passwordEncode);
        return mapperFacade.map(studentDao.save(student), StudentResponse.class);
    }

    @Transactional
    public StudentResponse updateStudent(StudentRequest request) {
        validateUpdateStudent(request);
        DktAdmin admin = accountService.getUserSession();

        Student student = buildUpdateStudent(request, admin);
        return mapperFacade.map(studentDao.save(student), StudentResponse.class);
    }

    public StudentListResponse getStudentInSemester(Long semesterId, PageBase pageRequest) {
        List<StudentSubject> studentSubjects = studentSubjectDao.getBySemesterId(semesterId);
        return getListStudentByStudentSubject(studentSubjects, pageRequest);
    }

    public StudentListResponse getStudentInSubject(Long subjectSemesterId, PageBase pageRequest) {
        List<StudentSubject> studentSubjects = studentSubjectDao.getBySubjectSemesterId(subjectSemesterId);
        return getListStudentByStudentSubject(studentSubjects, pageRequest);
    }

    private void validateCreate(StudentRequest request) {
        if (request.getStudentCode() == null ) {
            throw new BaseException(400, "StudentCode không thể null");
        }
        if (studentDao.getByStudentCode(request.getStudentCode()) != null) {
            throw new BaseException(400, "StudentCode đã  tồn tại");
        }
        if (request.getEmail() == null) {
            throw new BaseException(400, "StudentEmail không thể null");
        }
        if (studentDao.getByEmail(request.getEmail()) != null) {
            throw new BaseException(400, "StudentEmail đã tồn tại");
        }
    }

    private StudentListResponse getListStudentByStudentSubject(List<StudentSubject> studentSubjects, PageBase pageRequest) {
        List<Long> listStudentId = new ArrayList<>();
        Integer page = pageRequest.getPage();
        Integer size = pageRequest.getSize();
        int total = studentSubjects.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            listStudentId.add(studentSubjects.get(i).getStudentId());
        }
        List<Student> students = studentDao.getStudentInList(listStudentId);
        StudentListResponse studentListResponse = new StudentListResponse(mapperFacade.mapAsList(students, StudentResponse.class));
        PageResponse pageResponse = new PageResponse(page, size, total);
        studentListResponse.setPageResponse(pageResponse);
        return studentListResponse;
    }

    private StudentListResponse getListStudentPaging(List<Student> students, PageBase pageRequest) {
        List<Student> studentList = new ArrayList<>();
        Integer page = pageRequest.getPage();
        Integer size = pageRequest.getSize();
        int total = students.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            studentList.add(students.get(i));
        }
        StudentListResponse studentListResponse = new StudentListResponse(mapperFacade.mapAsList(studentList, StudentResponse.class));
        PageResponse pageResponse = new PageResponse(page, size, total);
        studentListResponse.setPageResponse(pageResponse);
        return studentListResponse;
    }

    private void validateUpdateStudent(StudentRequest request) {
        if (request.getId() == null || !checkStudentExist(request.getId())) {
            throw new BadRequestException(400, "student không tồn tại");
        }
    }

    public StudentListResponse getAllStudent(PageBase pageBase) {
        List<Student> listStudent = studentDao.getAll();
        return getListStudentPaging(listStudent,pageBase);
    }

    public StudentResponse getStudent(Long id) {
        Student student = studentDao.getById(id);
        if (student == null) {
            throw new BadRequestException(400, "student không tồn tại");
        }
        return mapperFacade.map(student, StudentResponse.class);
    }

    public void deleteStudent(Long id) {
        Student student = studentDao.getById(id);
        if (student == null) {
            throw new BadRequestException(400, "student không tồn tại");
        }
        studentDao.delete(student);
    }

    public void deleteListStudent(List<Long> ids) {
        List<Student> students = studentDao.getStudentInList(ids);
        List<StudentSubject> studentSubjects = studentSubjectDao.getStudentSubjectInStudentIdInList(ids);
        if (!CollectionUtils.isEmpty(studentSubjects)) {
            throw new BaseException(400, "Không thể xóa student");
        }
        studentDao.deleteListStudent(students);
    }

    public Student buildCreateStudent(StudentRequest studentRequest, DktAdmin dktAdmin) {
        Student student = this.generateStudent(studentRequest);

        Instant now = Instant.now();
        student.setCreatedAt(now);
        student.setModifiedAt(now);
        student.setCreatedBy(dktAdmin.getUsername());
        student.setModifiedBy(dktAdmin.getUsername());

        return student;
    }

    public Student buildUpdateStudent(StudentRequest request, DktAdmin dktAdmin) {
        Student student = studentDao.getById(request.getId());
        if (!StringUtils.isEmpty(request.getEmail())) {
            student.setEmail(request.getEmail());
        }
        if (!StringUtils.isEmpty(request.getCourse())) {
            student.setCourse(request.getCourse());
        }
        if (!StringUtils.isEmpty(request.getFullName())) {
            student.setFullName(request.getFullName());
        }
        if (!StringUtils.isEmpty(request.getDateOfBirth())) {
            student.setDateOfBirth(request.getDateOfBirth());
        }
        if (!StringUtils.isEmpty(request.getStudentCode())) {
            student.setStudentCode(request.getStudentCode());
            student.setUsername(request.getStudentCode());
        }
        if (request.getGender() != null) {
            student.setGender(request.getGender());
        }
        Instant now = Instant.now();
        student.setModifiedAt(now);
        student.setModifiedBy(dktAdmin.getUsername());

        return student;
    }

    public List<XSSFRow> importStudent(MultipartFile file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<XSSFRow> errors = new ArrayList<>();
        storeImportStudent(sheet, errors);
        return errors;
    }

    public XSSFWorkbook template() throws IOException {
        String templatePath = "\\template\\excel\\import_student.xlsx";
        File templateFile = new ClassPathResource(templatePath).getFile();
        FileInputStream templateInputStream = new FileInputStream(templateFile);
        return new XSSFWorkbook(templateInputStream);
    }

    private void storeImportStudent(XSSFSheet sheet, List<XSSFRow> errors) {
        int rowNumber = sheet.getPhysicalNumberOfRows();
        for (int i = 5; i < rowNumber; i++) {
            XSSFRow row = sheet.getRow(i);
            try {
                String stt = getValueInCell(row.getCell(0));
                if (stt == null) continue;
                StudentRequest studentRequest = new StudentRequest();
                studentRequest.setFullName(ExcelUtil.getValueInCell(row.getCell(1)).trim());
                studentRequest.setStudentCode(ExcelUtil.getValueInCell(row.getCell(2)));
                studentRequest.setDateOfBirth(ExcelUtil.getValueInCell(row.getCell(3)));
                studentRequest.setEmail(ExcelUtil.getValueInCell(row.getCell(4)));
                studentRequest.setCourse(ExcelUtil.getValueInCell(row.getCell(5)));
                String gender = ExcelUtil.getValueInCell(row.getCell(6));
                studentRequest.setGender(Gender.getValue(gender).getValue());

                this.createStudent(studentRequest);
            } catch (Exception e) {
                errors.add(row);
            }
        }
    }

    @Transactional
    void storeImportStudent(List<Student> students) {
        Map<String, Student> dbStudents = studentDao.getAll().stream().collect(Collectors.toMap(Student::getUsername, s -> s));
        Map<String, Student> importStudents = students.stream().collect(Collectors.toMap(Student::getUsername, s -> s));
        importStudents.forEach((username, student) -> {
            if (dbStudents.containsKey(username)) {
                Student dbStudent = dbStudents.get(username);
                student.setId(dbStudent.getId());
                student.setPassword(dbStudent.getPassword());
                student.setCreatedBy(dbStudent.getCreatedBy());
                student.setCreatedAt(dbStudent.getCreatedAt());
            }
        });
        List<Student> saveStudents = new ArrayList<>(importStudents.values());
        studentDao.saveAll(saveStudents);

    }

    public boolean existStudent(String studentCode) {
        Student student = studentDao.getByStudentCode(studentCode);
        return student != null;
    }

    public boolean existStudent(Long studentId) {
        Student student = studentDao.getById(studentId);
        return student != null;
    }

    public StudentListResponse searchStudent(String query, PageBase pageBase) {
        List<Student> studentWithCode = studentDao.getStudentLikeCode(query);
        List<Student> studentWithName = studentDao.getStudentLikeName(query);
        Map<String, Student> studentMap = new HashMap<>();
        for (Student student : studentWithCode) {
            studentMap.put(student.getStudentCode(), student);
        }
        for (Student student : studentWithName) {
            studentMap.put(student.getStudentCode(), student);
        }
        studentMap = studentMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
        return getListStudentPaging(
                new ArrayList<>(studentMap.values()), pageBase
        );
    }

    public Boolean checkExistStudent(CheckExistRequest checkExistRequest) {
        if (Constant.ADD.equalsIgnoreCase(checkExistRequest.getMode())) {
            Student student = studentDao.getByStudentCode(checkExistRequest.getCode());
            return student != null;
        } else if (Constant.EDIT.equalsIgnoreCase(checkExistRequest.getMode())){
            Student student = studentDao.getByStudentCode(checkExistRequest.getCode());
            Student studentById = studentDao.getById(checkExistRequest.getId());
            if (studentById == null) return true;
            if (student == null) return false;
            if (student.getStudentCode().equals(studentById.getStudentCode())) {
                return false;
            }
            return true;
        }
        throw new BadRequestException(400, "Mode không tồn tại");
    }

    private Student generateStudent(StudentRequest studentRequest) {
        Student student = new Student();
        student.setUsername(studentRequest.getStudentCode());
        student.setEmail(studentRequest.getEmail());
        student.setCourse(studentRequest.getCourse());
        student.setPassword(studentRequest.getStudentCode());
        student.setStudentCode(studentRequest.getStudentCode());
        student.setFullName(studentRequest.getFullName());
        student.setGender(studentRequest.getGender());
        student.setDateOfBirth(studentRequest.getDateOfBirth());

        return student;
    }

    private boolean checkStudentExist(StudentRequest request) {
        Student student;
        String email = request.getEmail();
        if (emailValidator.validateEmail(email)) {
            student = studentDao.getByEmail(email);
        } else {
            String studentCode = request.getStudentCode();
            student = studentDao.getByStudentCode(studentCode);
        }
        return student != null;
    }

    private boolean checkStudentExist(Long id) {
        Student student = studentDao.getById(id);
        return student != null;
    }

    private Student findStudent(StudentRequest request) {
        String email = request.getEmail();
        if (emailValidator.validateEmail(email)) {
            return studentDao.getByEmail(email);
        } else {
            String studentCode = request.getStudentCode();
            return studentDao.getByStudentCode(studentCode);
        }
    }

    private String getValueInCell(Cell cell) {
        if (cell == null) return null;
        CellType type = cell.getCellType();
        if (type == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (type == CellType.NUMERIC) {
            Double cellData = cell.getNumericCellValue();
            return cellData.toString();
        } else {
            return null;
        }
    }
}
