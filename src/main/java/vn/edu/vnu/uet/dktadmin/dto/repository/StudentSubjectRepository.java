package vn.edu.vnu.uet.dktadmin.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubject;

import java.util.List;

@Repository
public interface StudentSubjectRepository extends JpaRepository<StudentSubject, Long> {
    StudentSubject findByStudentIdAndSubjectSemesterId(Long studentId, Long subjectSemesterId);
    List<StudentSubject> findBySemesterId (Long semesterId);
    List<StudentSubject> findBySubjectSemesterId (Long subjectSemesterId);
    List<StudentSubject> findBySemesterIdAndIsRegisteredIsFalse(Long semesterId);
    List<StudentSubject> findBySemesterIdAndStudentIdAndIsRegisteredIsTrue(Long semesterId, Long studentId);
    Integer countBySubjectSemesterId(Long subjectSemesterId);
    List<StudentSubject> findByIdIn(List<Long> ids);
}
