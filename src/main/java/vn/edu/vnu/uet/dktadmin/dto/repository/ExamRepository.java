package vn.edu.vnu.uet.dktadmin.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dktadmin.dto.model.Exam;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Exam findByExamCode(String examCode);
    List<Exam> findBySemesterId(Long semesterId);
    List<Exam> findByRoomSemesterIdAndDate(Long roomSemesterId, LocalDateTime date);
}
