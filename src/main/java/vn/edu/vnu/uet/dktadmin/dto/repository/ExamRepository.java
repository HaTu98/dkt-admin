package vn.edu.vnu.uet.dktadmin.dto.repository;
;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dktadmin.dto.model.Exam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findBySemesterId(Long semesterId);
    List<Exam> findByRoomSemesterIdAndDate(Long roomSemesterId, LocalDate date);
    List<Exam> findBySemesterIdAndSubjectId(Long semesterId, Long subjectId);
    List<Exam> findByIdIn(List<Long> ids);
}
