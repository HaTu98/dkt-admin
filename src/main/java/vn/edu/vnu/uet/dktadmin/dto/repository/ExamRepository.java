package vn.edu.vnu.uet.dktadmin.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dktadmin.dto.model.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Exam findByExamCode(String examCode);
}
