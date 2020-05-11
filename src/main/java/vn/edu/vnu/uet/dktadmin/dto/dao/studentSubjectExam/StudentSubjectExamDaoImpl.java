package vn.edu.vnu.uet.dktadmin.dto.dao.studentSubjectExam;

import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubjectExam;
import vn.edu.vnu.uet.dktadmin.dto.repository.StudentSubjectExamRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentSubjectExamDaoImpl implements StudentSubjectExamDao {
    private final StudentSubjectExamRepository studentSubjectExamRepository;

    public StudentSubjectExamDaoImpl(StudentSubjectExamRepository studentSubjectExamRepository) {
        this.studentSubjectExamRepository = studentSubjectExamRepository;
    }

    @Override
    public StudentSubjectExam store(StudentSubjectExam studentSubjectExam) {
        return studentSubjectExamRepository.save(studentSubjectExam);
    }

    @Override
    public StudentSubjectExam getById(Long id) {
        return studentSubjectExamRepository.findById(id).orElse(null);
    }

    @Override
    public StudentSubjectExam getByExamIdAndStudentSubjectId(Long examId, Long studentSubjectId) {
        return studentSubjectExamRepository.findByExamIdAndAndStudentSubjectId(examId, studentSubjectId);
    }

    @Override
    public List<StudentSubjectExam> getBySemesterId(Long semesterId) {
        return studentSubjectExamRepository.findBySemesterId(semesterId);
    }

    @Override
    public List<StudentSubjectExam> getByStudentSubjectId(Long studentSubjectId) {
        return studentSubjectExamRepository.findByStudentSubjectId(studentSubjectId);
    }

    @Override
    public void delete(StudentSubjectExam studentSubjectExam) {
        studentSubjectExamRepository.delete(studentSubjectExam);
    }
}
