package vn.edu.vnu.uet.dktadmin.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dktadmin.dto.model.Subject;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findBySubjectCode(String subjectCode);
    List<Subject> findBySubjectCodeContains(String subjectCode);
    List<Subject> findBySubjectNameContains(String subjectName);
    List<Subject> findByIdIn(List<Long> ids);
    List<Subject> findAllByOrderByIdAsc();
}
