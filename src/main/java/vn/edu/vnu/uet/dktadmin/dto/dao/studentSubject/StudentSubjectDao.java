package vn.edu.vnu.uet.dktadmin.dto.dao.studentSubject;

import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubject;

import java.util.List;

public interface StudentSubjectDao {
    List<StudentSubject> getAll();
    StudentSubject store(StudentSubject studentSubject);
    StudentSubject getByStudentAndSubjectSemesterId(Long studentId, Long subjectSemesterId);
}
