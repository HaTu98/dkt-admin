package vn.edu.vnu.uet.dktadmin.dto.service.student;

import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnu.uet.dktadmin.common.Constant;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.model.DktAdmin;
import vn.edu.vnu.uet.dktadmin.common.security.AccountService;
import vn.edu.vnu.uet.dktadmin.common.validator.EmailValidator;
import vn.edu.vnu.uet.dktadmin.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Student;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubject;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBaseRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentResponse;

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
        if (checkStudentExist(request)) {
            throw new BadRequestException(400, "student đã tồn tại");
        }
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

    public StudentListResponse getStudentInSemester(Long semesterId, PageBaseRequest pageRequest) {
        List<StudentSubject> studentSubjects = studentSubjectDao.getBySemesterId(semesterId);
        return getListStudentByStudentSubject(studentSubjects, pageRequest);
    }

    public StudentListResponse getStudentInSubject(Long subjectSemesterId, PageBaseRequest pageRequest) {
        List<StudentSubject> studentSubjects = studentSubjectDao.getBySubjectSemesterId(subjectSemesterId);
        return getListStudentByStudentSubject(studentSubjects, pageRequest);
    }

    private StudentListResponse getListStudentByStudentSubject(List<StudentSubject> studentSubjects, PageBaseRequest pageRequest) {
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

    private StudentListResponse getListStudentPaging(List<Student> students, PageBaseRequest pageRequest) {
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

    public StudentListResponse getAllStudent() {
        List<Student> listStudent = studentDao.getAll();
        List<StudentResponse> studentResponses = listStudent.stream().
                map(student -> mapperFacade.map(student, StudentResponse.class)).
                collect(Collectors.toList());
        return new StudentListResponse(studentResponses);
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
        }
        if (!StringUtils.isEmpty(request.getGender())) {
            student.setGender(request.getGender());
        }
        if (!StringUtils.isEmpty(request.getUsername())) {
            student.setUsername(request.getUsername());
        }
        Instant now = Instant.now();
        student.setModifiedAt(now);
        student.setModifiedBy(dktAdmin.getUsername());

        return student;
    }

    public void importStudent(MultipartFile file) throws IOException {
        String error = "";
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<Student> students = getStudentInSheet(sheet, error);

        storeImport(students);
    }

    private List<Student> getStudentInSheet(XSSFSheet sheet, String error) {
        List<Student> students = new ArrayList<>();
        DktAdmin admin = accountService.getUserSession();

        int rowNumber = sheet.getPhysicalNumberOfRows();
        for (int i = 1; i < rowNumber; i++) {
            XSSFRow row = sheet.getRow(i);
            try {
                //String stt = getValueInCell(row.getCell(0)).trim();
                Student student = new Student();
                student.setFullName(getValueInCell(row.getCell(1)).trim());
                student.setGender(getValueInCell(row.getCell(2)).trim());
                student.setDateOfBirth(getValueInCell(row.getCell(3)).trim());
                String username = getValueInCell(row.getCell(4)).trim();
                student.setUsername(username);
                student.setEmail(username + Constant.BASE_EMAIL);
                student.setCourse(getValueInCell(row.getCell(5)).trim());
                student.setPassword(passwordEncoder.encode(username));
                Instant now = Instant.now();
                student.setCreatedAt(now);
                student.setModifiedAt(now);
                student.setCreatedBy(admin.getUsername());
                student.setModifiedBy(admin.getUsername());

                students.add(student);
            } catch (Exception e) {
                error += i + ", ";
            }
        }
        return students;
    }

    @Transactional
    void storeImport(List<Student> students) {
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

    public StudentListResponse searchStudent(String query, PageBaseRequest pageBaseRequest) {
        List<Student> studentWithCode = studentDao.getStudentLikeCode(query);
        List<Student> studentWithName = studentDao.getStudentLikeName(query);
        Map<String, Student> studentMap = new HashMap<>();
        for (Student student : studentWithCode) {
            studentMap.put(student.getStudentCode(), student);
        }
        for (Student student : studentWithName) {
            studentMap.put(student.getStudentCode(), student);
        }
        studentMap = studentMap.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
        return getListStudentPaging(
                new ArrayList<>(studentMap.values()), pageBaseRequest
        );
    }

    private Student generateStudent(StudentRequest studentRequest) {
        Student student = new Student();
        student.setUsername(studentRequest.getUsername());
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
            String username = request.getUsername();
            student = studentDao.getByUsername(username);
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
            String username = request.getUsername();
            return studentDao.getByUsername(username);
        }
    }

    private StudentResponse getResponse(Student student) {
        return null;
//        return StudentResponse.builder()
//                .id(student.getId())
//                .username(student.getUsername())
//                .email(student.getEmail())
//                .course(student.getCourse())
//                .dateOfBirth(student.getDateOfBirth())
//                .fullName(student.getFullName())
//                .studentCode(student.getStudentCode())
//                .gender(student.getGender())
//                .build();
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
