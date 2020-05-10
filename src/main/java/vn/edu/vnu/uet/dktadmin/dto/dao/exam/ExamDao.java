package vn.edu.vnu.uet.dktadmin.dto.dao.exam;

import vn.edu.vnu.uet.dktadmin.dto.model.Exam;

import java.time.LocalDateTime;
import java.util.List;


public interface ExamDao {
    Exam store(Exam exam);
    Exam getById(Long id);
    Exam getByExamCode(String examCode);
    List<Exam> getBySemesterId(Long semesterId);
    List<Exam> getByRoomAndDate(Long roomSemesterId, LocalDateTime date);
}
