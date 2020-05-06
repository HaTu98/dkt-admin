package vn.edu.vnu.uet.dktadmin.dto.dao.studentSubject;

import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubject;

import java.util.List;

public interface StudentSubjectDao {
    List<StudentSubject> getAll();
    StudentSubject store(StudentSubject studentSubject);
    StudentSubject getByStudentAndSubjectSemesterId(Long studentId, Long subjectSemesterId);
    StudentSubject getById(Long id);
    List<StudentSubject> getBySemesterId(Long semesterId);
    List<StudentSubject> getBySubjectSemesterId(Long subjectSemesterId);
    void delete(StudentSubject studentSubject);
}
