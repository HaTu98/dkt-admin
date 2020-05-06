package vn.edu.vnu.uet.dktadmin.dto.dao.studentSubjectExam;

import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubjectExam;

import java.util.List;

public interface StudentSubjectExamDao {
    StudentSubjectExam store(StudentSubjectExam studentSubjectExam);
    StudentSubjectExam getById(Long id);
    StudentSubjectExam getByExamIdAndStudentSubjectId(Long examId, Long studentSubjectId);
    List<StudentSubjectExam> getBySemesterId(Long semesterId);
    List<StudentSubjectExam> getByStudentSubjectId(Long studentSubjectId);
}
