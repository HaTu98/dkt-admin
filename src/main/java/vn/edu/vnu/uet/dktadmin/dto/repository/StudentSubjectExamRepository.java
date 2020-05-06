package vn.edu.vnu.uet.dktadmin.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubjectExam;

import java.util.List;

@Repository
public interface StudentSubjectExamRepository extends JpaRepository<StudentSubjectExam, Long> {
    StudentSubjectExam findByExamIdAndAndStudentSubjectId(Long examId, Long studentSubjectId);
    List<StudentSubjectExam> findBySemesterId(Long semesterId);
    List<StudentSubjectExam> findByStudentSubjectId(Long studentSubjectId);
}
