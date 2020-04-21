package vn.edu.vnu.uet.dktadmin.dto.dao.studentSubjectExam;

import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubjectExam;

public interface StudentSubjectExamDao {
    StudentSubjectExam store(StudentSubjectExam studentSubjectExam);
    StudentSubjectExam getById(Long id);
    StudentSubjectExam getByExamIdAndStudentSubjectId(Long examId, Long studentSubjectId);
}
