package vn.edu.vnu.uet.dktadmin.dto.service.student;

import ma.glasnost.orika.MapperFacade;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnu.uet.dktadmin.common.Constant;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.common.model.DktAdmin;
import vn.edu.vnu.uet.dktadmin.common.security.AccountService;
import vn.edu.vnu.uet.dktadmin.common.validator.EmailValidator;
import vn.edu.vnu.uet.dktadmin.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Student;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentResponse;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MapperFacade mapperFacade;

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        if (checkStudentExist(request)) {
            throw new FormValidateException("username/email", "student đã tồn tại");
        }
        DktAdmin admin = accountService.getUserSession();

        String passwordEncode = passwordEncoder.encode(request.getPassword());

        Student student = buildCreateStudent(request, admin);
        student.setPassword(passwordEncode);
        Student saveStudent = studentDao.save(student);
        return getResponse(saveStudent);
    }

    @Transactional
    public StudentResponse updateStudent(StudentRequest request) {
        if (!checkStudentExist(request)) {
            throw new FormValidateException("username/email", "student không tồn tại");
        }
        DktAdmin admin = accountService.getUserSession();

        Student student = buildUpdateStudent(request, admin);
        Student saveStudent = studentDao.save(student);
        return getResponse(saveStudent);
    }


    public StudentListResponse getStudent() {
        List<Student> listStudent =studentDao.getAll();
        List<StudentResponse> studentResponses = listStudent.stream().map(this::getResponse).collect(Collectors.toList());
        return new StudentListResponse(studentResponses);
    }

    public Student buildCreateStudent(StudentRequest studentRequest, DktAdmin dktAdmin) {
        Student student = this.student(studentRequest);

        Instant now = Instant.now();
        student.setCreatedAt(now);
        student.setModifiedAt(now);
        student.setCreatedBy(dktAdmin.getUsername());
        student.setModifiedBy(dktAdmin.getUsername());

        return student;
    }

    public Student buildUpdateStudent(StudentRequest request, DktAdmin dktAdmin) {
        Student student = this.findStudent(request);
        if (!student.getUsername().equals(request.getUsername())) {
            student.setUsername(request.getUsername());
        }
        if (!student.getEmail().equals(request.getEmail())) {
            student.setEmail(request.getEmail());
        }
        if (!student.getCourse().equals(request.getCourse())) {
            student.setCourse(request.getCourse());
        }
        if (!student.getFullName().equals(request.getFullName())) {
            student.setFullName(request.getFullName());
        }
        if (!student.getDateOfBirth().equals(request.getDateOfBirth())) {
            student.setDateOfBirth(request.getDateOfBirth());
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
        for (int i = 1;  i < rowNumber; i++) {
            XSSFRow row = sheet.getRow(i);
            try {
                //String stt = getValueInCell(row.getCell(0)).trim();
                Student student = new Student();
                student.setFullName(getValueInCell(row.getCell(1)).trim());
                student.setGender(getValueInCell(row.getCell(2)).trim());
                student.setDateOfBirth(getValueInCell(row.getCell(3)).trim());
                String username = getValueInCell(row.getCell(4)).trim();
                student.setUsername(username);
                student.setEmail(username+ Constant.BASE_EMAIL);
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
    void storeImport(List<Student> students){
        Map<String,Student> dbStudents = studentDao.getAll().stream().collect(Collectors.toMap(Student::getUsername,s -> s ));
        Map<String, Student> importStudents = students.stream().collect(Collectors.toMap(Student::getUsername,s -> s ));
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

    private Student student(StudentRequest studentRequest) {
        Student student = new Student();
        student.setUsername(studentRequest.getUsername());
        student.setEmail(studentRequest.getEmail());
        student.setCourse(studentRequest.getCourse());
        student.setPassword(studentRequest.getPassword());
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
        return StudentResponse.builder()
                .id(student.getId())
                .username(student.getUsername())
                .email(student.getEmail())
                .course(student.getCourse())
                .dateOfBirth(student.getDateOfBirth())
                .fullName(student.getFullName())
                .studentCode(student.getStudentCode())
                .gender(student.getGender())
                .build();
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
