package vn.edu.vnu.uet.dktadmin.dto.dao.studentSubjectExam;

import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubjectExam;
import vn.edu.vnu.uet.dktadmin.dto.repository.StudentSubjectExamRepository;

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
        Optional<StudentSubjectExam> studentSubjectExam = studentSubjectExamRepository.findById(id);
        return studentSubjectExam.orElseGet(studentSubjectExam::get);
    }

    @Override
    public StudentSubjectExam getByExamIdAndStudentSubjectId(Long examId, Long studentSubjectId) {
        return studentSubjectExamRepository.findByExamIdAndAndStudentSubjectId(examId, studentSubjectId);
    }
}
