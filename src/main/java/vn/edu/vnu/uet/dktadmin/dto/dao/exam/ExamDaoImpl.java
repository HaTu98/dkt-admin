package vn.edu.vnu.uet.dktadmin.dto.dao.exam;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dktadmin.dto.model.Exam;
import vn.edu.vnu.uet.dktadmin.dto.repository.ExamRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExamDaoImpl implements ExamDao {
    private final ExamRepository examRepository;

    public ExamDaoImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    public Exam store(Exam exam) {
        return examRepository.save(exam);
    }

    @Override
    public Exam getById(Long id) {
        return examRepository.findById(id).orElse(null);
    }

    @Override
    public Exam getByExamCode(String examCode) {
        return examRepository.findByExamCode(examCode);
    }

    @Override
    public List<Exam> getBySemesterId(Long semesterId) {
        return examRepository.findBySemesterId(semesterId);
    }

    @Override
    public List<Exam> getByRoomAndDate( Long roomSemesterId, LocalDate date) {
        List<Exam> exams = examRepository.findByRoomSemesterIdAndDate(roomSemesterId, date);
        if (CollectionUtils.isEmpty(exams)) {
            return new ArrayList<>();
        }
        return exams;
    }

    @Override
    public List<Exam> getBySemesterIdAndSubjectId(Long semesterId, Long subjectId) {
        List<Exam> exams = examRepository.findBySemesterIdAndSubjectId(semesterId, subjectId);
        if (CollectionUtils.isEmpty(exams)) {
            return new ArrayList<>();
        }
        return exams;
    }

    @Override
    public List<Exam> getExamInListId(List<Long> ids) {
        List<Exam> exams = examRepository.findByIdIn(ids);
        if (CollectionUtils.isEmpty(exams)) {
            return new ArrayList<>();
        }
        return exams;
    }

    @Override
    public void deleteList(List<Exam> exams) {
        examRepository.deleteAll(exams);
    }
}
